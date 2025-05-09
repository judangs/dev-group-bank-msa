package org.bank.pay.dto.service.response;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.bank.core.cash.Money;
import org.bank.core.cash.PayMethod;
import org.bank.core.dto.response.ResponseDto;
import org.bank.pay.core.domain.history.OrderPayHistory;
import org.bank.pay.core.domain.history.PayHistory;
import org.bank.pay.core.domain.history.ReChargePayHistory;
import org.bank.pay.core.domain.history.TransferPayHistory;

import java.time.LocalDateTime;

@Data
@SuperBuilder
public class HistoryRecordDetailResponse extends ResponseDto {

    private String payName;
    private Money amount;
    private PayMethod method;
    private LocalDateTime transactionDate;
    private LocalDateTime rollbackDate;

    private Order order;
    private ReCharge reCharge;
    private Transfer transfer;

    @Data
    @Builder
    public static class Order {
        private String orderId;
        private String storeName;
        private String username;

        public static Order from(OrderPayHistory orderPayHistory) {
            return Order.builder()
                    .orderId(orderPayHistory.getPaymentId())
                    .storeName(orderPayHistory.getStoreName())
                    .username(orderPayHistory.getBuyer().getUsername())
                    .build();
        }
    }

    @Data
    @Builder
    public static class ReCharge {
        private String cardNumber;

        public static ReCharge from(ReChargePayHistory rechargePayHistory) {
            return ReCharge.builder()
                    .build();
        }
    }

    @Data
    @Builder
    public static class Transfer {
        private String transferTo;

        public static Transfer from(TransferPayHistory transferPayHistory) {
            return Transfer.builder()
                    .transferTo(transferPayHistory.getTransferTo())
                    .build();
        }
    }

    public static HistoryRecordDetailResponse from(PayHistory history) {

        HistoryRecordDetailResponse.HistoryRecordDetailResponseBuilder builder  = HistoryRecordDetailResponse.builder()
                .payName(history.getPayName())
                .method(history.getMethod())
                .amount(history.getPayMoney())
                .transactionDate(history.getTransactionDate())
                .rollbackDate(history.getRollbackDate());

        if(history instanceof ReChargePayHistory reChargePayHistory) {
            builder.reCharge(ReCharge.from(reChargePayHistory));
        }
        if(history instanceof OrderPayHistory orderPayHistory) {
            builder.order(Order.from(orderPayHistory));
        }
        if(history instanceof TransferPayHistory transferPayHistory) {
            builder.transfer(Transfer.from(transferPayHistory));
        }

        return builder.build();
    }

}
