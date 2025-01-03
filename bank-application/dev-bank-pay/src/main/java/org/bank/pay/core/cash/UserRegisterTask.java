package org.bank.pay.core.cash;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRegisterTask {

    private final CashChargeService cashChargeService;

    public void initialize(AuthClaims authClaims) {
        cashChargeService.initializeCash(authClaims);
    }
}
