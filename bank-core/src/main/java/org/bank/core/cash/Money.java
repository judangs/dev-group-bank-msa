package org.bank.core.cash;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Money {
    private BigDecimal balance;

    public Money() {
        balance = BigDecimal.ZERO;
    }

    public void deposit(BigDecimal amount) {
        validatePositiveAmount(amount);
        this.balance = this.balance.add(amount);
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
        Money money = (Money) o;
        return balance.compareTo(money.balance) == 0;
    }
}
