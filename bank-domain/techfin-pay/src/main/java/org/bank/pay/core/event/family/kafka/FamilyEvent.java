package org.bank.pay.core.event.family.kafka;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.bank.core.kafka.KafkaEvent;

import java.util.UUID;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
        property = "eventType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = InviteEvent.class, name = "INVITE"),
        @JsonSubTypes.Type(value = PaymentEvent.class, name = "PAYMENT_REQUEST"),
        @JsonSubTypes.Type(value = CashConversionEvent.class, name = "CASH_CONVERSION")
})
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class FamilyEvent extends KafkaEvent {

    protected UUID familyId;

    protected FamilyEvent(Class<? extends KafkaEvent> classType, UUID familyId) {
        super(classType);
        this.familyId = familyId;
    }
}
