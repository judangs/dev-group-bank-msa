package org.bank.consumer.core.family;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bank.consumer.global.kafka.EventDeserializerContext;
import org.bank.consumer.global.kafka.EventType;
import org.bank.consumer.global.kafka.KafkaConfiguration;
import org.bank.core.kafka.KafkaEvent;
import org.bank.pay.core.event.family.kafka.FamilyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

@Configuration
class FamilyEventConsumerConfiguration extends KafkaConfiguration {

    @Bean(name = "familyListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, KafkaEvent> accountListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, KafkaEvent> facotry = new ConcurrentKafkaListenerContainerFactory<>();
        kafkaDynamicPropertyInjector.inject(facotry, consumerFactory(EventType.FAMILY, FamilyEvent.class));

        return facotry;
    }

    @Bean(name = "kafkaFamilyEventObjectMapper")
    public ObjectMapper kafkaFamilyEventObjectMapper() {
        String[] topics = kafkaDynamicPropertyInjector.topic(EventType.FAMILY);
        EventDeserializerContext.add(EventType.FAMILY, topics, eventMapper(FamilyEvent.class));
        return EventDeserializerContext.event(EventType.FAMILY);
    }
}
