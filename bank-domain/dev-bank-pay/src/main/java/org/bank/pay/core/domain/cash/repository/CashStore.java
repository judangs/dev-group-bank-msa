package org.bank.pay.core.domain.cash.repository;

import org.bank.pay.core.domain.cash.Cash;

public interface CashStore {
    void save(Cash cash);
}
