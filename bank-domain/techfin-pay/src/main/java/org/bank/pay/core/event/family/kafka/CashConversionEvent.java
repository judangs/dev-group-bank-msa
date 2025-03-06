package org.bank.pay.core.event.family.kafka;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.pay.core.domain.familly.MemberClaims;

import java.util.UUID;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@JsonTypeName(value = "CASH_CONVERSION")
public class CashConversionEvent extends FamilyEvent {

    private AuthClaims from;
    private UUID cardId;
    private Money amount;

    public CashConversionEvent(UUID familyId, UUID cardId, MemberClaims from, Money amount) {
        super(CashConversionEvent.class, familyId);
        this.cardId = cardId;
        this.from = from;
        this.amount = amount;
    }

}


