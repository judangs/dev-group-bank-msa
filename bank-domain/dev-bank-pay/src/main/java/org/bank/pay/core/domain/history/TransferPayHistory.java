package org.bank.pay.core.domain.history;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.bank.core.cash.PayMethod;
import org.bank.pay.core.event.family.kafka.CashConversionEvent;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor
@DiscriminatorValue("transfer")
@Entity
public class TransferPayHistory extends PayHistory {

    private String transferTo;

    public static TransferPayHistory of(CashConversionEvent event) {
        return TransferPayHistory.builder()
                .transferTo(event.getFamilyId().toString())
                .payName("그룹 캐시 전환")
                .payMoney(event.getAmount())
                .transactionDate(LocalDateTime.now())
                .method(PayMethod.TRANSFER)
                .userId(event.getFrom().getUserid())
                .build();
    }
    public static TransferPayHistory of(CashConversionEvent event, boolean rollback) {
        TransferPayHistory history = TransferPayHistory.builder()
                .transferTo(event.getFamilyId().toString())
                .payName("그룹 캐시 전환")
                .payMoney(event.getAmount())
                .transactionDate(LocalDateTime.now())
                .method(PayMethod.TRANSFER)
                .userId(event.getFrom().getUserid())
                .build();

        if(rollback)
            history.isDeleted();

        return history;
    }

}
