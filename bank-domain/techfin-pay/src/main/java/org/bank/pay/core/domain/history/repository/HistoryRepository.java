package org.bank.pay.core.domain.history.repository;

import org.bank.core.cash.PayMethod;
import org.bank.pay.core.domain.history.PayHistory;
import org.bank.pay.core.domain.owner.OwnerClaims;
import org.springframework.data.domain.Page;

import java.time.Instant;

public interface HistoryRepository {

    PayHistory findRecordById(OwnerClaims claims, Long id);
    Page<PayHistory> findAllRecordsByMethodAndBetweenDate(OwnerClaims claims, PayMethod method, Instant from, Instant to, int page);
    void saveHistory(PayHistory history);
}
