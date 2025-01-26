package org.bank.core.kafka;

import lombok.Data;

import java.util.UUID;

@Data
public abstract class KafkaEvent {

    protected UUID eventId;
    protected long timestamp;

    protected KafkaEvent() {
        this.eventId = UUID.randomUUID();
        this.timestamp = System.currentTimeMillis();
    }
}
