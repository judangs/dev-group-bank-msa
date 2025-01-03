package org.bank.pay.core.cash.repository;

import org.bank.core.cash.Money;
import org.bank.pay.core.cash.Cash;
import org.bank.pay.core.onwer.OwnerClaims;
import org.springframework.data.domain.Page;

public interface CashReader {

    Cash findByOwnerClaims(OwnerClaims ownerClaims);
    Money findBalanceByOwnerClaims(OwnerClaims ownerClaims);
    Page<Cash> findAll(int page);
}
