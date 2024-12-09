package org.bank.core.cash;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Money {
    BigDecimal credit;

    public Money() {
        credit = BigDecimal.ZERO;
    }
}
