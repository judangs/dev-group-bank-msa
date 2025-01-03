package org.bank.pay.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.bank.core.cash.Money;
import org.bank.core.cash.PayMethod;
import org.bank.core.dto.response.ResponseDto;
import org.bank.pay.core.history.PayHistory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
public class HistoryRecordResponse extends ResponseDto {

    List<HistoryRecord> records = new ArrayList<>();

    @Data
    @Builder
    public static class HistoryRecord {
        private String payName;
        private Money payMoney;
        private PayMethod method;
        private LocalDateTime transactionDate;
        private LocalDateTime rollbackDate;

        public static HistoryRecord from(PayHistory history) {
            return HistoryRecord.builder()
                    .payName(history.getPayName())
                    .method(history.getMethod())
                    .payMoney(history.getPayMoney())
                    .transactionDate(history.getTransactionDate())
                    .rollbackDate(history.getRollbackDate())
                    .build();
        }
    }


    public static HistoryRecordResponse from(List<PayHistory> histories) {


        List<HistoryRecord> records = histories.stream().map(HistoryRecord::from).toList();
        return HistoryRecordResponse.builder()
                .records(records)
                .build();
    }
}
