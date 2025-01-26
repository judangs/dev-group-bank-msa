package org.bank.core.kafka;

public interface KafkaMessageToEventConverter<T> {
    public KafkaEvent convert(T message);
}
