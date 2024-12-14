package org.bank.pay.core.cash.repository;

import org.bank.pay.core.cash.Cash;

public interface CashStore {
    void save(Cash cash);
}
