package org.bank.pay.core.familly.kafka.event;

import lombok.Getter;
import org.bank.pay.core.familly.MemberClaims;

import java.util.UUID;

@Getter
public class InviteEvent extends FamilyEvent {

    private MemberClaims to;


    public InviteEvent(UUID familyId, MemberClaims to) {
        super(familyId);
        this.to = to;
    }
}
