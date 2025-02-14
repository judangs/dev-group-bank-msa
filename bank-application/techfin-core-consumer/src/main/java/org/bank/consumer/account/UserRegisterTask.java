package org.bank.consumer.account;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.pay.core.domain.cash.CashChargeService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRegisterTask {

    private final CashChargeService cashChargeService;

    public void initialize(AuthClaims authClaims) {
        cashChargeService.initializeCash(authClaims);
    }
}
