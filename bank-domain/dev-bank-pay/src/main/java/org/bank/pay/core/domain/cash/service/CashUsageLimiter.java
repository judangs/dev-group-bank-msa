package org.bank.pay.core.domain.cash.service;

import lombok.RequiredArgsConstructor;
import org.bank.pay.core.domain.cash.Cash;
import org.bank.pay.core.domain.cash.repository.CashStore;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CashUsageLimiter implements CashLimitService {

    private final CashStore cashStore;

    @Override
    public void limit(Cash cash, BigDecimal perOnce, BigDecimal perDaily) {
        validate(perOnce, perDaily);
        cash.limits(perOnce, perDaily);
        cashStore.save(cash);
    }


    @Override
    public void clear(Cash cash) {
        cash.clearPaymentLimits();
        cashStore.save(cash);
    }

    private void validate(BigDecimal perOnce, BigDecimal perDaily) {
        if (perOnce.compareTo(BigDecimal.ZERO) < 0 ||
                perDaily.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("결제 한도는 음수일 수 없습니다.");
        }

        if (perDaily.compareTo(perOnce) < 0) {
            throw new IllegalArgumentException("1일 한도는 1회 한도보다 크거나 같아야 합니다.");
        }
    }
}
