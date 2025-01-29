package org.bank.pay.core.domain.cash.repository;

import org.bank.core.cash.Money;
import org.bank.pay.core.domain.cash.Cash;
import org.bank.pay.core.domain.onwer.OwnerClaims;
import org.springframework.data.domain.Page;

public interface CashReader {

    Cash findByOwnerClaims(OwnerClaims ownerClaims);
    Money findBalanceByOwnerClaims(OwnerClaims ownerClaims);
    Page<Cash> findAll(int page);
}
