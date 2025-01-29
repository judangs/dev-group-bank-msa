package org.bank.pay.core.cash;

import org.bank.pay.core.domain.cash.Cash;
import org.bank.pay.core.domain.cash.PayLimit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CashTest {

    Cash cash = new Cash();

    @Test
    @DisplayName("[CASH] 잔고를 충전합니다.")
    void charge() {

        BigDecimal balance = cash.getCredit().getBalance();
        BigDecimal balanceAfterCharge = balance.add(new BigDecimal(10000));
        cash.charge(new BigDecimal(10000));
        assertTrue(cash.getCredit().getBalance().equals(balanceAfterCharge));
    }

    @Test
    @DisplayName("[CASH] 가격을 지불합니다.")
    void pay() {
        cash.charge(new BigDecimal(10000));
        BigDecimal balanceAfterCharge = cash.getCredit().getBalance().subtract(new BigDecimal(1000));
        cash.pay(new BigDecimal(1000));
        assertTrue(cash.getCredit().getBalance().equals(balanceAfterCharge));

    }

    @Test
    @DisplayName("[CASH] 결제 한도를 설정합니다.")
    void updatePaymentLimits() {

        BigDecimal perOnce = new BigDecimal(10000);
        BigDecimal perDailly = new BigDecimal(20000);
        cash.updatePaymentLimits(perOnce, perDailly);

        PayLimit payLimit = cash.getLimits();
        assertEquals(payLimit.getPerOnce().compareTo(perOnce), 0);
        assertEquals(payLimit.getPerDaily().compareTo(perDailly), 0);
    }

    @Test
    @DisplayName("[CASH] 결제 한도를 초기화합니다.")
    void clearPaymentLimits() {

        BigDecimal perOnce = new BigDecimal(10000);
        BigDecimal perDailly = new BigDecimal(20000);

        cash.updatePaymentLimits(perOnce, perDailly);
        cash.clearPaymentLimits();

        assertEquals(cash.getLimits().getPerOnce().compareTo(BigDecimal.valueOf(Double.MAX_VALUE)), 0);
        assertEquals(cash.getLimits().getPerDaily().compareTo(BigDecimal.valueOf(Double.MAX_VALUE)), 0);
    }
}