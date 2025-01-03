package org.bank.pay.core.history;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.PayMethod;
import org.bank.core.dto.pay.ChargeResponse;
import org.bank.pay.core.history.repository.HistoryReader;
import org.bank.pay.core.history.repository.HistoryStore;
import org.bank.pay.core.onwer.OwnerClaims;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryReader historyReader;
    private final HistoryStore historyStore;

    public void saveReChargeHistory(AuthClaims authClaims, ChargeResponse chargeResponse, String paymentCardNumber) {
        PayHistory reChargePayHistory = ReChargePayHistory.of(authClaims, chargeResponse, paymentCardNumber);
        historyStore.saveHistory(reChargePayHistory);
    }

    public List<PayHistory> getPayRecords(AuthClaims authClaims, PayMethod method, Instant startDate, Instant endDate, int page) {

        OwnerClaims ownerClaims = (OwnerClaims) authClaims;
        Page<PayHistory> payHistories = historyReader.findAllRecordsByMethodAndBetweenDate(ownerClaims, method, startDate, endDate, page);
        return payHistories.getContent();
    }

    public PayHistory getPayRecordDetails(AuthClaims authClaims, Long transactionId) {
        OwnerClaims ownerClaims = (OwnerClaims) authClaims;
        return historyReader.findRecordById(ownerClaims, transactionId);
    }
}
