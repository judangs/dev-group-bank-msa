package org.bank.core.cash;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(Money balance) {
        super("잔액이 부족합니다. 현재 잔액: " + balance.getBalance());
    }
}
