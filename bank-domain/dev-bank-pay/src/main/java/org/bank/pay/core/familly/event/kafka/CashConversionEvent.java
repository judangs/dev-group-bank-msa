package org.bank.pay.core.familly.event.kafka;

import lombok.Getter;
import org.bank.core.cash.Money;
import org.bank.pay.core.familly.MemberClaims;

import java.util.UUID;

@Getter
public class CashConversionEvent extends FamilyEvent {

    private MemberClaims from;
    private Money amount;


    public CashConversionEvent(UUID familyId, MemberClaims from, Money amount) {
        super(familyId);
        this.from = from;
        this.amount = amount;
    }

}


