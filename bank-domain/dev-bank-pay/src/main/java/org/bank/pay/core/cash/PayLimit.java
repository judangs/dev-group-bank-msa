package org.bank.pay.core.cash;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Embeddable
public class PayLimit {

    private BigDecimal perOnce;
    private BigDecimal perDaily;

    public PayLimit() {
        setToMaxLimits();
    }

    public void updateLimits(BigDecimal perOnceAmount, BigDecimal perDailyAmount) {

        this.perOnce = perOnceAmount;
        this.perDaily = perDailyAmount;
    }

    public void setToMaxLimits() {
        this.perOnce = BigDecimal.valueOf(Double.MAX_VALUE); // 최대값 설정
        this.perDaily = BigDecimal.valueOf(Double.MAX_VALUE);
    }
}
