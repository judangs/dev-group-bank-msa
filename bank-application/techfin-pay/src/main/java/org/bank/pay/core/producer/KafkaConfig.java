package org.bank.pay.core.producer;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.bank.core.kafka.KafkaEvent;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@PropertySource("classpath:application-kafka-producer.properties")
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaConfig {


    @Bean
    public ProducerFactory<String, KafkaEvent> producerFactory(KafkaProperties kafkaProperties) {

        KafkaProperties.Producer producerProperties = kafkaProperties.getProducer();

        Map<String, Object> factoryProperties = new HashMap<>();
        factoryProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerProperties.getBootstrapServers().get(0));
        factoryProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, producerProperties.getKeySerializer());
        factoryProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, producerProperties.getValueSerializer());
        factoryProperties.put(ProducerConfig.ACKS_CONFIG, producerProperties.getAcks());

        return new DefaultKafkaProducerFactory<>(factoryProperties);
    }

    @Bean
    public KafkaTemplate<String, KafkaEvent> kafkaTemplate(ProducerFactory<String, KafkaEvent> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
