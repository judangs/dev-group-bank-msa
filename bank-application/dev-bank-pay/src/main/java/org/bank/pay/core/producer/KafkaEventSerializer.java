package org.bank.pay.core.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;
import org.bank.core.kafka.KafkaEvent;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.Map;

public class KafkaEventSerializer implements Serializer<KafkaEvent> {

    private final ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
            .modulesToInstall(new JavaTimeModule())
            .build();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serializer.super.configure(configs, isKey);
    }

    @Override
    public byte[] serialize(String s, KafkaEvent kafkaEvent) {
        try {
            return objectMapper.writeValueAsBytes(kafkaEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] serialize(String topic, Headers headers, KafkaEvent data) {
        return Serializer.super.serialize(topic, headers, data);
    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}
