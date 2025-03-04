package org.bank.consumer.core.user;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.pay.core.domain.owner.PayOwner;
import org.bank.pay.core.domain.owner.repository.PayOwnerStore;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRegisterTask {

    private final PayOwnerStore payOwnerStore;

    public void initialize(AuthClaims user) {
        payOwnerStore.save(new PayOwner(user));
    }
}
