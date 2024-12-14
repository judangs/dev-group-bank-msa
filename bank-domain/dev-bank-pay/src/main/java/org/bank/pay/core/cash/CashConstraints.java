package org.bank.pay.core.cash;

import java.math.BigDecimal;

public class CashConstraints {

    // 결제 한도 조건을 체크합니다.
    public static void validatePayLimits(PayLimit payLimit, BigDecimal amount) {
        if (amount.compareTo(payLimit.getPerOnce()) > 0) {
            throw new IllegalArgumentException("1회 결제 한도를 초과했습니다.");
        }

        if (amount.compareTo(payLimit.getPerDaily()) > 0) {
            throw new IllegalArgumentException("1일 결제 한도를 초과했습니다.");
        }
    }

    // 결제 한도 등록 조건을 제크합니다.
    public static void validatePayLimits(BigDecimal perOnce, BigDecimal perDailly) {
        if(perOnce.compareTo(BigDecimal.ZERO) < 0 || perDailly.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("결제 한도는 0원보다 커야 합니다.");
        }

        if(perDailly.compareTo(perOnce) < 0) {
            throw new IllegalArgumentException("1일 한도는 1회 한도보다 크거나 같아야 합니다.");
        }

    }

}
