package org.bank.store.mysql.core.pay.cash.infrastructure;

import lombok.RequiredArgsConstructor;
import org.bank.core.cash.Money;
import org.bank.core.domain.DomainNames;
import org.bank.pay.core.domain.cash.Cash;
import org.bank.pay.core.domain.cash.repository.CashReader;
import org.bank.pay.core.domain.onwer.OwnerClaims;
import org.bank.store.mysql.core.pay.cash.JpaCashRepository;
import org.bank.store.source.DataSourceType;
import org.bank.store.source.NamedRepositorySource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@NamedRepositorySource(domain = DomainNames.PAY, type = DataSourceType.READWRITE)
@RequiredArgsConstructor
public class CashQueryRepository implements CashReader {

    private final JpaCashRepository jpaCashRepository;

    public Optional<Cash> findByCash(Cash cash) {
        return jpaCashRepository.findById(cash.getCashId());
    }

    @Override
    public Cash findByOwnerClaims(OwnerClaims ownerClaims) {
        return jpaCashRepository.findByClaimsFromPayOwner(ownerClaims);
    }

    @Override
    public Money findBalanceByOwnerClaims(OwnerClaims ownerClaims) {
        return jpaCashRepository.findBalanceByClaimsFromPayOwner(ownerClaims);
    }

    @Override
    public Page<Cash> findAll(int page) {
        return null;
    }
}
