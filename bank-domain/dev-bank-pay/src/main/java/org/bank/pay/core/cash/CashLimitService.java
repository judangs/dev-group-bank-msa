package org.bank.pay.core.cash;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CashLimitService {



    public void setPaymentLimits(Cash cash, BigDecimal perOnceLimitAmount, BigDecimal perDailyLimitAmount) {
        validateLimits(perOnceLimitAmount, perDailyLimitAmount);
        cash.updatePaymentLimits(perOnceLimitAmount, perDailyLimitAmount);
    }


    public void clearPaymentLimits(Cash cash) {
        cash.clearPaymentLimits();
    }

    private void validateLimits(BigDecimal perOnceLimitAmount, BigDecimal perDailyLimitAmount) {
        if (perOnceLimitAmount.compareTo(BigDecimal.ZERO) < 0 ||
                perDailyLimitAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("결제 한도는 음수일 수 없습니다.");
        }

        if (perDailyLimitAmount.compareTo(perOnceLimitAmount) < 0) {
            throw new IllegalArgumentException("1일 한도는 1회 한도보다 크거나 같아야 합니다.");
        }
    }
}
