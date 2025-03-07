package org.bank.pay.core.domain.cash.service;

import org.bank.pay.core.domain.cash.Cash;

import java.math.BigDecimal;

public interface CashLimitService {

    void limit(Cash cash, BigDecimal perOnce, BigDecimal perDaily) throws IllegalArgumentException;
    void clear(Cash cash);
}
