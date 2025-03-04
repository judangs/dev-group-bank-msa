package org.bank.consumer.global.kafka;

import lombok.SneakyThrows;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;
import org.bank.core.kafka.KafkaEvent;

import java.nio.ByteBuffer;
import java.util.Map;

public class KafkaEventDeserializer implements Deserializer<KafkaEvent> {


    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Deserializer.super.configure(configs, isKey);
    }

    @Override
    public KafkaEvent deserialize(String s, byte[] bytes) {
        return null;
    }

    @SneakyThrows
    @Override
    public KafkaEvent deserialize(String topic, Headers headers, byte[] data) {
        Class<? extends KafkaEvent> eventClass = EventDeserializerContext.delegation(topic).readValue(data, KafkaEvent.class).getClassType();
        return EventDeserializerContext.delegation(topic).readValue(data, eventClass);
    }

    @Override
    public KafkaEvent deserialize(String topic, Headers headers, ByteBuffer data) {
        return Deserializer.super.deserialize(topic, headers, data);
    }

    @Override
    public void close() {
        Deserializer.super.close();
    }
}
