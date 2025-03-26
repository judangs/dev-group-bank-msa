package org.bank.pay.core.domain.cash;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bank.core.cash.Money;
import org.bank.pay.core.domain.owner.PayOwner;
import org.bank.pay.global.domain.DomainEntity;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Table(name = "pay_cash_tb")
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Cash extends DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)", unique = true)
    @EqualsAndHashCode.Include
    private UUID cashId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "balance", column = @Column(name = "credit", precision = 30, scale = 2))
    })
    private Money credit;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "balance", column = @Column(name = "currency", precision = 30, scale = 2))
    })
    private Money dailyCurrency;

    @Embedded
    private PayLimit limits;

    private String ownerId;

    public Cash() {
        credit = new Money();
        dailyCurrency = new Money();
        limits = new PayLimit();
    }

    public Cash(PayOwner payOwner) {
        this();
        this.ownerId = payOwner.getClaims().getUserid();
    }

    public void charge(Money amount) {
        credit.deposit(amount);
    }

    public void charge(BigDecimal amount) {
        credit.deposit(amount);
    }


    public void pay(Money amount) {
        CashConstraints.validatePayLimits(limits, amount.getBalance());
        credit.withdraw(amount);
    }

    public void pay(BigDecimal amount) {
        CashConstraints.validatePayLimits(limits, amount);
        credit.withdraw(amount);
    }

    public void refreshDailyCurrency() {
        dailyCurrency = new Money();
    }


    public void limits(BigDecimal perOnceAmount, BigDecimal perDailyAmount) {
        CashConstraints.validatePayLimits(perOnceAmount, perDailyAmount);
        limits.updateLimits(perOnceAmount, perDailyAmount);
    }

    public void clearPaymentLimits() {
        limits.setToMaxLimits();
    }

}
