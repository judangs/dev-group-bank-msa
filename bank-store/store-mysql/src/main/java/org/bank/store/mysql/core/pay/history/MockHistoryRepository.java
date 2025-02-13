package org.bank.store.mysql.core.pay.history;

import org.bank.core.cash.PayMethod;
import org.bank.core.domain.DomainNames;
import org.bank.pay.core.domain.history.PayHistory;
import org.bank.pay.core.domain.history.repository.HistoryRepository;
import org.bank.pay.core.domain.onwer.OwnerClaims;
import org.bank.store.source.DataSourceType;
import org.bank.store.source.NamedRepositorySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Collections;

@Repository
@NamedRepositorySource(domain = DomainNames.PAY, type = DataSourceType.READWRITE)
public class MockHistoryRepository implements HistoryRepository {
    @Override
    public PayHistory findRecordById(OwnerClaims claims, Long id) {
        return new PayHistory(){};
    }

    @Override
    public Page<PayHistory> findAllRecordsByMethodAndBetweenDate(OwnerClaims claims, PayMethod method, Instant from, Instant to, int page) {
        return new PageImpl<>(Collections.emptyList(), Pageable.ofSize(10), 10);
    }

    @Override
    public void saveHistory(PayHistory history) {

    }
}
