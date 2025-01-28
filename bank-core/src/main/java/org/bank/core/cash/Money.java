package org.bank.core.cash;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Embeddable
public class Money implements Serializable {

    private BigDecimal balance;

    public Money() {
        balance = new BigDecimal(0).setScale(10, RoundingMode.HALF_UP);
    }

    public Money(Integer balance) {
        this.balance = new BigDecimal(balance).setScale(10, RoundingMode.HALF_UP);
    }

    public Money(BigDecimal balance) {
        this.balance = balance;
    }

    public Integer toInteger() {
        return balance.intValue();
    }

    public void deposit(Money amount) {
        validatePositiveAmount(amount.getBalance());
        this.balance = this.balance.add(amount.getBalance());
    }

    public void deposit(BigDecimal amount) {
        validatePositiveAmount(amount);
        this.balance = this.balance.add(amount);
    }

    public void withdraw(Money amount) {
        validatePositiveAmount(amount.getBalance());

        BigDecimal afterBalance = this.balance.subtract(amount.getBalance());
        if(afterBalance.compareTo(new Money(0).getBalance()) < 0) {
            throw new InsufficientBalanceException(this);
        }

        this.balance = this.balance.subtract(amount.getBalance());
    }

    public void withdraw(BigDecimal amount) {
        validatePositiveAmount(amount);
        this.balance = this.balance.subtract(amount);
    }

    private void validatePositiveAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("금액은 양수여야 합니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BigDecimal money = ((Money) o).getBalance();
        return balance.equals(money);
    }
}
