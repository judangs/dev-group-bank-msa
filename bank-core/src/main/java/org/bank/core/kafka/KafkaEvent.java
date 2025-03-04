package org.bank.core.kafka;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class KafkaEvent implements Serializable {

    @EqualsAndHashCode.Include
    protected UUID eventId;
    protected Class<? extends KafkaEvent> classType;
    protected long timestamp;

    protected KafkaEvent() {
        this.classType = KafkaEvent.class;
        this.eventId = UUID.randomUUID();
        this.timestamp = System.currentTimeMillis();
    }

    protected KafkaEvent(Class<? extends KafkaEvent> classType) {
        this.classType = classType;
        this.eventId = UUID.randomUUID();
        this.timestamp = System.currentTimeMillis();
    }


    @NoArgsConstructor
    public static class ConcreteKafkaEvent extends KafkaEvent {
    }
}
