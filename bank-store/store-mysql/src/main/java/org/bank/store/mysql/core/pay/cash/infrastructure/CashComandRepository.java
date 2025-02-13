package org.bank.store.mysql.core.pay.cash.infrastructure;

import lombok.RequiredArgsConstructor;
import org.bank.core.domain.DomainNames;
import org.bank.pay.core.domain.cash.Cash;
import org.bank.pay.core.domain.cash.repository.CashStore;
import org.bank.store.mysql.core.pay.cash.JpaCashRepository;
import org.bank.store.source.DataSourceType;
import org.bank.store.source.NamedRepositorySource;
import org.springframework.stereotype.Repository;

@Repository
@NamedRepositorySource(domain = DomainNames.PAY, type = DataSourceType.READWRITE)
@RequiredArgsConstructor
public class CashComandRepository implements CashStore {

    private final JpaCashRepository jpaCashRepository;

    @Override
    public void save(Cash cash) {
        jpaCashRepository.save(cash);
    }
}
