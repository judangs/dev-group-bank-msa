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
import org.bank.pay.core.domain.familly.MemberClaims;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@DiscriminatorValue("order")
@SuperBuilder
@Entity
public class OrderPayHistory extends PayHistory {

    private String storeName;


    public static OrderPayHistory of(AuthClaims user, String paymentId, Product product) {
        return OrderPayHistory.builder()
                .buyer(MemberClaims.of(user))
                .paymentId(paymentId)
                .payName(product.getName())
                .method(PayMethod.CARD)
                .payMoney(new Money(product.price()))
                .transactionDate(LocalDateTime.now())
                .rollbackDate(LocalDate.of(9999, 12, 31).atTime(23, 59))
                .build();
    }
}