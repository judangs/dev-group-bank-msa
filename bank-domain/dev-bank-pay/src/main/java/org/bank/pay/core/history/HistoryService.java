package org.bank.pay.core.history;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.PayMethod;
import org.bank.core.dto.pay.ChargeResponse;
import org.bank.pay.core.history.repository.HistoryRepository;
import org.bank.pay.core.onwer.OwnerClaims;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;


    public void saveReChargeHistory(AuthClaims authClaims, ChargeResponse chargeResponse, String paymentCardNumber) {
        PayHistory reChargePayHistory = ReChargePayHistory.of(authClaims, chargeResponse, paymentCardNumber);
        historyRepository.saveHistory(reChargePayHistory);
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
