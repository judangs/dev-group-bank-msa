package org.bank.pay.core.payment;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CashChargeDetail extends PaymentDetail {

    private UUID cardId;

    private CashChargeDetail(UUID cardId, PaymentDetail detail) {
        super(detail);
        this.cardId = cardId;
    }

    public static CashChargeDetail of(UUID cardId, PaymentDetail detail) {
        return new CashChargeDetail(cardId, detail);
    }
}
