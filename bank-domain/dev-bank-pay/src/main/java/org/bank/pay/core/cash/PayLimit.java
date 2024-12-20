package org.bank.pay.core.cash;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Embeddable
public class PayLimit {

    private BigDecimal perOnce;
    private BigDecimal perDaily;

    private static final BigDecimal MAX_PAYMENT_LIMIT = new BigDecimal("99999999999999999999.99");

    public PayLimit() {
        setToMaxLimits();
    }

    public void updateLimits(BigDecimal perOnceAmount, BigDecimal perDailyAmount) {

        this.perOnce = perOnceAmount;
        this.perDaily = perDailyAmount;
    }

    public void setToMaxLimits() {
        this.perOnce = MAX_PAYMENT_LIMIT;
        this.perDaily = MAX_PAYMENT_LIMIT;
    }
}
