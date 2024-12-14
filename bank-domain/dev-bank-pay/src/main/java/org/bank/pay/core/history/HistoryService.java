package org.bank.pay.core.history;

import lombok.RequiredArgsConstructor;
import org.bank.pay.core.history.repository.HistoryReader;
import org.bank.pay.core.onwer.OwnerClaims;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryReader historyReader;

    public Page<PayHistory> getPayRecords(OwnerClaims claims, PayMethod method, Instant startDate, Instant endDate) {
        return historyReader.findAllRecordsByMethodAndBetweenDate(claims, method, startDate, endDate);
    }

    public PayHistory getPayRecordDetails(OwnerClaims claims, Long transactionId) {
        return historyReader.findRecordById(claims, transactionId);
    }
}
