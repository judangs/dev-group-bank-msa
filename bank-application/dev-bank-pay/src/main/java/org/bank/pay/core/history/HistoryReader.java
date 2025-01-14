package org.bank.pay.core.history;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.dto.response.ResponseDto;
import org.bank.pay.dto.request.HistoryRecordRequest;
import org.bank.pay.dto.response.HistoryRecordDetailResponse;
import org.bank.pay.dto.response.HistoryRecordResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryReader {


    private final HistoryService historyService;

    public ResponseDto getRecords(AuthClaims authClaims, HistoryRecordRequest request) {

        List<PayHistory> payHistories = historyService.getPayRecords(authClaims, request.getMethod(), request.getStartDate(), request.getEndDate(), request.getPage());
        return HistoryRecordResponse.from(payHistories);
    }

    public ResponseDto getRecordDetails(AuthClaims authClaims, HistoryRecordRequest request) {

        PayHistory history = historyService.getPayRecordDetails(authClaims, request.getTransactionId());
        return HistoryRecordDetailResponse.from(history);
    }










}
