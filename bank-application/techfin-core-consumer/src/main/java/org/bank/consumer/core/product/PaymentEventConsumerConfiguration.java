package org.bank.consumer.core.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bank.consumer.global.kafka.EventDeserializerContext;
import org.bank.consumer.global.kafka.EventType;
import org.bank.consumer.global.kafka.KafkaConfiguration;
import org.bank.core.kafka.KafkaEvent;
import org.bank.pay.core.event.family.kafka.FamilyEvent;
import org.bank.pay.core.event.product.PurchasedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

@Configuration
public class PaymentEventConsumerConfiguration extends KafkaConfiguration  {

    @Bean(name = "paymentListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, KafkaEvent> accountListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, KafkaEvent> facotry = new ConcurrentKafkaListenerContainerFactory<>();
        kafkaDynamicPropertyInjector.inject(facotry, consumerFactory(EventType.PAYMENT, FamilyEvent.class));

        return facotry;
    }

    @Bean(name = "kafkaPaymentEventObjectMapper")
    public ObjectMapper kafkaFamilyEventObjectMapper() {
        String[] topics = kafkaDynamicPropertyInjector.topic(EventType.PAYMENT);
        EventDeserializerContext.add(EventType.PAYMENT, topics, eventMapper(PurchasedEvent.class));
        return EventDeserializerContext.event(EventType.PAYMENT);
    }
}
