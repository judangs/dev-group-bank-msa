package org.bank.store.mysql.core.pay.history;

import org.bank.core.cash.PayMethod;
import org.bank.pay.core.history.PayHistory;
import org.bank.pay.core.history.repository.HistoryReader;
import org.bank.pay.core.history.repository.HistoryStore;
import org.bank.pay.core.onwer.OwnerClaims;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Collections;

@Repository
public class MockHistoryRepository implements HistoryReader, HistoryStore {
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
