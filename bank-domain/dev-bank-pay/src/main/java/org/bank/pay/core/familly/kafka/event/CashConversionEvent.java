package org.bank.pay.core.familly.kafka.event;

import org.bank.core.cash.Money;
import org.bank.pay.core.familly.MemberClaims;

import java.util.UUID;

public class CashConversionEvent extends FamilyEvent {

    private UUID familyId;
    private MemberClaims from;
    private Money amount;


    public CashConversionEvent(UUID familyId, MemberClaims from, Money amount) {
        super(familyId);
        this.from = from;
        this.amount = amount;
    }

}


