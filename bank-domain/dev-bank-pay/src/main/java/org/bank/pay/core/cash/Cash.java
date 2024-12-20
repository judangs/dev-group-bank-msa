package org.bank.pay.core.cash;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.core.cash.Money;
import org.bank.pay.core.onwer.PayOwner;
import org.bank.pay.global.domain.DomainEntity;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cash")
public class Cash extends DomainEntity {

    @Id
    @GeneratedValue
    private UUID cashId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "balance", column = @Column(name = "credit"))
    })
    private Money credit;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "balance", column = @Column(name = "currency", precision = 30, scale = 10))
    })
    private Money dailyCurrency;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "perOnce", column = @Column(name = "perOnce", precision = 30, scale = 10)),
            @AttributeOverride(name = "perDaily", column = @Column(name = "perDaily", precision = 30, scale = 10))
    })
    private PayLimit limits;

    @OneToOne(cascade = CascadeType.ALL)
    private PayOwner payOwner;

    public Cash(PayOwner payOwner) {

        this.payOwner = payOwner;
        this.payOwner.setCash(this);

        credit = new Money();
        dailyCurrency = new Money();
        limits = new PayLimit();
    }

    public void charge(BigDecimal amount) {
        credit.deposit(amount);
    }


    public void pay(BigDecimal amount) {
        CashConstraints.validatePayLimits(limits, amount);
        credit.withdraw(amount);
    }

    public void refreshDailyUsageCredit() {
        dailyCurrency = new Money();
    }


    public void updatePaymentLimits(BigDecimal perOnceAmount, BigDecimal perDailyAmount) {
        CashConstraints.validatePayLimits(perOnceAmount, perDailyAmount);
        limits.updateLimits(perOnceAmount, perDailyAmount);
    }

    public void clearPaymentLimits() {
        limits.setToMaxLimits();
    }

}
