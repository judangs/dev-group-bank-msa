package org.bank.consumer.global.kafka;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.bank.core.kafka.KafkaEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Map;
import java.util.stream.Collectors;

public abstract class KafkaConfiguration {

    @Autowired
    protected KafkaDynamicPropertyInjector kafkaDynamicPropertyInjector;
    @Primary
    @Bean(name = "kafkaEventObjectMapper")
    public ObjectMapper kafkaEventObjectMapper() {
        return eventMapper(KafkaEvent.ConcreteKafkaEvent.class);
    }

    protected ConsumerFactory<String, KafkaEvent> consumerFactory(EventType type, Class<? extends KafkaEvent> mixing) {
        kafkaDynamicPropertyInjector.inject(type);
        return  new DefaultKafkaConsumerFactory<>(kafkaDynamicPropertyInjector.getConsumerProps());
    }

    protected ObjectMapper eventMapper(Class<? extends KafkaEvent> mixing) {
         return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .activateDefaultTyping(
                        new ObjectMapper().getPolymorphicTypeValidator(),
                        ObjectMapper.DefaultTyping.NON_FINAL,
                        JsonTypeInfo.As.PROPERTY
                )
                .registerModule(
                        new SimpleModule()
                                .addAbstractTypeMapping(KafkaEvent.class, mixing)
                );
    }

    @Configuration
    @PropertySource("classpath:/application-kafka-dynamic.properties")
    @EnableConfigurationProperties(KafkaDynamicProperties.class)
    public static class KafkaDynamicPropertiesConfiguration {

        @Bean
        public KafkaDynamicPropertyInjector kafkaDynamicPropertyInjector(KafkaProperties kafkaProperties, KafkaDynamicProperties kafkaDynamicProperties) {

            Map<String, Object> props = kafkaProperties.getConsumer().getProperties().entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey, Map.Entry::getValue));

            return new KafkaDynamicPropertyInjector(kafkaProperties, kafkaDynamicProperties, props);
        }
    }

    @Configuration
    @PropertySource("classpath:/application-kafka-consumer.properties")
    @EnableConfigurationProperties(KafkaProperties.class)
    public static class KafkaPropertiesConfiguration {

        @Primary
        @Bean
        public KafkaProperties kafkaProperties() {
            return new KafkaProperties();
        }
    }


}
