package org.bank.pay.core.familly.event.kafka;

import lombok.Getter;
import org.bank.core.auth.AuthClaims;
import org.bank.pay.core.familly.MemberClaims;

import java.util.UUID;

@Getter
public class InviteEvent extends FamilyEvent {

    private MemberClaims to;


    public InviteEvent(UUID familyId, AuthClaims to) {
        super(familyId);
        this.to = MemberClaims.of(to);
    }
}
