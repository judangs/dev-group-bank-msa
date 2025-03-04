package org.bank.consumer.integration.kafka;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.serialization.Serializer;
import org.bank.core.kafka.KafkaEvent;
import org.bank.pay.core.event.family.kafka.CashConversionEvent;
import org.bank.pay.core.event.family.kafka.InviteEvent;
import org.bank.pay.core.event.family.kafka.PaymentEvent;
import org.bank.user.core.event.registration.AccountCreatedEvent;

import java.util.Map;

public class KafkaEventSerializer implements Serializer<KafkaEvent> {

    private final ObjectMapper kafkaEventObjectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .activateDefaultTyping(
                    new ObjectMapper().getPolymorphicTypeValidator(),
                    ObjectMapper.DefaultTyping.NON_FINAL,
                    JsonTypeInfo.As.PROPERTY
            )
            .registerModule(new JavaTimeModule())
            .registerModule(
                    new SimpleModule()
                            .registerSubtypes(AccountCreatedEvent.class, InviteEvent.class, PaymentEvent.class, CashConversionEvent.class)
            );

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serializer.super.configure(configs, isKey);
    }

    @Override
    public byte[] serialize(String s, KafkaEvent kafkaEvent) {
        try {
            return kafkaEventObjectMapper.writeValueAsBytes(kafkaEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}

