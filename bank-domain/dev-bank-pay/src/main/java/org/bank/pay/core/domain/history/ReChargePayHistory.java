package org.bank.pay.core.domain.history;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.core.cash.PayMethod;
import org.bank.core.payment.Product;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor
@DiscriminatorValue("recharge")
@Entity
public class ReChargePayHistory extends PayHistory {


    public static ReChargePayHistory of(AuthClaims authClaims, String paymentId, Product product, PayMethod method) {
        return ReChargePayHistory.builder()
                .userId(authClaims.getUserid())
                .paymentId(paymentId)
                .payName(product.getName())
                .method(method)
                .payMoney(new Money(product.price()))
                .transactionDate(LocalDateTime.now())
                .rollbackDate(LocalDate.of(9999, 12, 31).atTime(23, 59))
                .build();
    }
}
