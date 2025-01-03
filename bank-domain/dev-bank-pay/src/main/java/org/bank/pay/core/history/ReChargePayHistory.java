package org.bank.pay.core.history;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.core.dto.pay.ChargeResponse;

import java.time.LocalDate;

@Getter
@SuperBuilder
@NoArgsConstructor
@DiscriminatorValue("recharge")
@Entity
public class ReChargePayHistory extends PayHistory {

    private String cardNumber;


    public static ReChargePayHistory of(AuthClaims authClaims, ChargeResponse chargeResponse, String cardNumber) {
        return ReChargePayHistory.builder()
                .userId(authClaims.getUserid())
                .payName(chargeResponse.getProductName())
                .method(chargeResponse.getMethod())
                .payMoney(new Money(chargeResponse.getTotalpayAmount()))
                .transactionDate(chargeResponse.getCompletedAt())
                .cardNumber(cardNumber)
                .rollbackDate(LocalDate.of(9999, 12, 31).atTime(23, 59))
                .build();
    }
}
