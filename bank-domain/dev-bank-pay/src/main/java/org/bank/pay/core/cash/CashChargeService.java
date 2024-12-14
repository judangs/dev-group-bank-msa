package org.bank.pay.core.cash;

import lombok.RequiredArgsConstructor;
import org.bank.pay.core.cash.repository.CashStore;
import org.bank.pay.core.onwer.PaymentCard;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class CashChargeService {

    private final DailyCashScheduler dailyCashScheduler;
    private final CashStore cashStore;

    @Transactional
    public void chargeCash(Cash cash, PaymentCard card, BigDecimal amount) {
        validateCharge(card, amount);
        cash.charge(amount);
    }


    private void validateCharge(PaymentCard card, BigDecimal amount) {
        if (card == null) {
            throw new IllegalArgumentException("유효하지 않은 카드입니다.");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("충전 금액은 0보다 커야 합니다.");
        }
    }
}
