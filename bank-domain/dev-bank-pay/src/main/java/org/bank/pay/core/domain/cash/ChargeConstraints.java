package org.bank.pay.core.domain.cash;

import org.bank.pay.core.domain.owner.PaymentCard;

import java.math.BigDecimal;

public class ChargeConstraints {

    public static void validate(PaymentCard card, BigDecimal amount) {
        if (card == null) {
            throw new IllegalArgumentException("유효하지 않은 카드입니다.");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("충전 금액은 0보다 커야 합니다.");
        }
    }

    public static void validate(ReservedCharge reservedCharge) {
        if(reservedCharge.getCard() == null || reservedCharge.getCash() == null) {
            throw new IllegalArgumentException("카드와 사용자 정보가 필요합니다.");
        }
        if(reservedCharge.getTriggerBalance() == null && reservedCharge.getScheduledDate() == null) {
            throw new IllegalArgumentException("예약 조건이 부적절합니다.");
        }
    }

}
