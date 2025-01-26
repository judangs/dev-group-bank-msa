package org.bank.pay.core.familly.kafka.event;

import lombok.Data;
import org.bank.core.kafka.KafkaEvent;

import java.util.UUID;

@Data
public class FamilyEvent extends KafkaEvent {

    private UUID familyId;

    protected FamilyEvent(UUID familyId) {
        super();
        this.familyId = familyId;
    }
}
