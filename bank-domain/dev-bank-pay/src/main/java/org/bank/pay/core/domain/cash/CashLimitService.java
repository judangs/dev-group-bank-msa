package org.bank.pay.core.domain.cash;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.pay.core.domain.cash.repository.CashReader;
import org.bank.pay.core.domain.onwer.OwnerClaims;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CashLimitService {

    private final CashReader cashReader;

    @Transactional
    public void setPaymentLimits(AuthClaims authClaims, BigDecimal perOnceLimitAmount, BigDecimal perDailyLimitAmount) {

        Cash cash = cashReader.findByOwnerClaims((OwnerClaims) authClaims);
        validateLimits(perOnceLimitAmount, perDailyLimitAmount);
        cash.updatePaymentLimits(perOnceLimitAmount, perDailyLimitAmount);
    }


    @Transactional
    public void clearPaymentLimits(AuthClaims authClaims) {
        Cash cash = cashReader.findByOwnerClaims((OwnerClaims) authClaims);
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
