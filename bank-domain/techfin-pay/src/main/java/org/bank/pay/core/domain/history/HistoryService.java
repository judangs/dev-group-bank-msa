package org.bank.pay.core.domain.history;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.PayMethod;
import org.bank.pay.core.domain.history.repository.HistoryRepository;
import org.bank.pay.core.domain.owner.OwnerClaims;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;


    public void saveHistory(PayHistory history) {
        historyRepository.saveHistory(history);
    }

    public List<PayHistory> getPayRecords(AuthClaims authClaims, PayMethod method, Instant startDate, Instant endDate, int page) {

        OwnerClaims ownerClaims = (OwnerClaims) authClaims;
        Page<PayHistory> payHistories = historyRepository.findAllRecordsByMethodAndBetweenDate(ownerClaims, method, startDate, endDate, page);
        return payHistories.getContent();
    }

    public PayHistory getPayRecordDetails(AuthClaims authClaims, Long transactionId) {
        OwnerClaims ownerClaims = (OwnerClaims) authClaims;
        return historyRepository.findRecordById(ownerClaims, transactionId);
    }
}
