package org.bank.pay.core.event.family.kafka;

import lombok.Getter;
import org.bank.core.cash.Money;
import org.bank.pay.core.domain.familly.MemberClaims;

import java.util.UUID;

@Getter
public class CashConversionEvent extends FamilyEvent {

    private MemberClaims from;
    private UUID cardId;
    private Money amount;


    public CashConversionEvent(UUID familyId, UUID cardId, MemberClaims from, Money amount) {
        super(familyId);
        this.cardId = cardId;
        this.from = from;
        this.amount = amount;
    }

}


