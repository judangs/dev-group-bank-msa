package org.bank.pay.core.event.family.kafka;

import lombok.Getter;
import org.bank.core.kafka.KafkaEvent;

import java.util.UUID;

@Getter
public class FamilyEvent extends KafkaEvent {

    protected UUID familyId;

    protected FamilyEvent(UUID familyId) {
        super();
        this.familyId = familyId;
    }
}
