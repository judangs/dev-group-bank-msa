package org.bank.pay.core.cash;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bank.core.cash.Money;
import org.bank.pay.core.onwer.PayOwner;
import org.bank.pay.global.domain.DomainEntity;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Entity
public class Cash extends DomainEntity {

    @Id
    @GeneratedValue
    private UUID cashId;

    @Embedded
    private Money credit;

    @Embedded
    private PayLimit limits;

    @OneToOne
    @JoinColumn(name = "owner_id")
    private PayOwner payOwner;

    public Cash() {
        credit = new Money();
        limits = new PayLimit();
    }

    public void charge(BigDecimal amount) {
        credit.deposit(amount);
    }


    public void pay(BigDecimal amount) {
        CashConstraints.validatePayLimits(limits, amount);
        credit.withdraw(amount);
    }


    public void updatePaymentLimits(BigDecimal perOnceAmount, BigDecimal perDailyAmount) {
        CashConstraints.validatePayLimits(perOnceAmount, perDailyAmount);
        limits.updateLimits(perOnceAmount, perDailyAmount);
    }

    public void clearPaymentLimits() {
        limits.setToMaxLimits();
    }

}
