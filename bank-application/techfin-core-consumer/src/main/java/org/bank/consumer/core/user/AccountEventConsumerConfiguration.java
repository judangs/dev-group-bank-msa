package org.bank.consumer.core.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bank.consumer.global.kafka.EventDeserializerContext;
import org.bank.consumer.global.kafka.EventType;
import org.bank.consumer.global.kafka.KafkaConfiguration;
import org.bank.core.kafka.KafkaEvent;
import org.bank.pay.core.event.family.kafka.FamilyEvent;
import org.bank.user.core.event.registration.AccountCreatedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

@Configuration
class AccountEventConsumerConfiguration extends KafkaConfiguration {

    @Bean(name = "accountListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, KafkaEvent> accountListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, KafkaEvent> facotry = new ConcurrentKafkaListenerContainerFactory<>();
        kafkaDynamicPropertyInjector.inject(facotry, consumerFactory(EventType.ACCOUNT, AccountCreatedEvent.class));
        return facotry;
    }

    @Bean(name = "kafkaAccountEventObjectMapper")
    public ObjectMapper kafkaAccountEventObjectMapper() {
        String[] topics = kafkaDynamicPropertyInjector.topic(EventType.ACCOUNT);
        EventDeserializerContext.add(EventType.ACCOUNT, topics, eventMapper(FamilyEvent.class));
        return EventDeserializerContext.event(EventType.ACCOUNT);
    }
}
