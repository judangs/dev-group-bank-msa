package org.bank.pay.core.history.repository;

import org.bank.pay.core.history.PayHistory;
import org.bank.pay.core.history.PayMethod;
import org.bank.pay.core.onwer.OwnerClaims;
import org.springframework.data.domain.Page;

import java.time.Instant;

public interface HistoryReader {

    PayHistory findRecordById(OwnerClaims claims, Long id);
    Page<PayHistory> findAllRecordsByMethodAndBetweenDate(OwnerClaims claims, PayMethod method, Instant from, Instant to);
}
