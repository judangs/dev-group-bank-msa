package org.bank.pay.dto.service.request;

import lombok.Data;
import org.bank.core.cash.PayMethod;

import java.time.Instant;

@Data
public class HistoryRecordRequest {

    private Long transactionId;
    private PayMethod method;
    private Instant startDate;
    private Instant endDate;
    private int page;

}
