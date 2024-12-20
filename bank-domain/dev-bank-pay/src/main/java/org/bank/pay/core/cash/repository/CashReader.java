package org.bank.pay.core.cash.repository;

import org.bank.pay.core.cash.Cash;
import org.springframework.data.domain.Page;

public interface CashReader {

    Page<Cash> findAll(int page);
}
