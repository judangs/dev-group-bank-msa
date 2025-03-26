package org.bank.pay.core.domain.cash;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import org.bank.core.cash.Money;

import java.math.BigDecimal;

@Getter
@Embeddable
public class PayLimit {

    @AttributeOverrides({
            @AttributeOverride(name = "balance", column = @Column(name = "perOnce", precision = 30, scale = 2)),
    })
    private Money perOnce;

    @AttributeOverrides({
            @AttributeOverride(name = "balance", column = @Column(name = "perDaily", precision = 30, scale = 2))
    })
    private Money perDaily;

    private static final BigDecimal MAX_PAYMENT_LIMIT = new BigDecimal("99999999999999999999.99");

    public PayLimit() {
        setToMaxLimits();
    }

    void updateLimits(BigDecimal perOnceAmount, BigDecimal perDailyAmount) {
        this.perOnce = new Money(perOnceAmount);
        this.perDaily = new Money(perDailyAmount);

        Money zero = new Money();
        if(perOnce.equals(zero) && perDaily.equals(zero)) {
            setToMaxLimits();
        }
    }

    void setToMaxLimits() {
        this.perOnce = new Money(MAX_PAYMENT_LIMIT);
        this.perDaily = new Money(MAX_PAYMENT_LIMIT);
    }
}
