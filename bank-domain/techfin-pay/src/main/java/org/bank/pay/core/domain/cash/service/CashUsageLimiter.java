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
        cash.limits(perOnce, perDaily);
        cashStore.save(cash);
    }


    @Override
    public void clear(Cash cash) {
        cash.clearPaymentLimits();
        cashStore.save(cash);
    }
}
