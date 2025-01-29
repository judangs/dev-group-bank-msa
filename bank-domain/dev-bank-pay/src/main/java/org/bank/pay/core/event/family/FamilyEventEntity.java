package org.bank.pay.core.event.family;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.bank.pay.global.domain.DomainEntity;

import java.util.UUID;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class FamilyEventEntity extends DomainEntity {

    protected UUID familyId;
    protected FamilyEventStatus status;

    public void accept() {
        status = FamilyEventStatus.ACCEPTED;
    }

    public void reject() {
        status = FamilyEventStatus.REJECTED;
    }

    public boolean isAccepted() {
        return status.equals(FamilyEventStatus.ACCEPTED);
    }

}
