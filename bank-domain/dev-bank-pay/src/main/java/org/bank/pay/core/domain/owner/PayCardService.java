package org.bank.pay.core.domain.owner;

import org.bank.core.auth.AuthClaims;

import java.util.List;
import java.util.UUID;

public interface PayCardService {

    PaymentCard register(AuthClaims user, PaymentCard card);
    List<PaymentCard> gets(AuthClaims user);
    PaymentCard get(AuthClaims user, UUID cardId);
    void updateAlias(AuthClaims user, UUID cardId, String cardAlias);
    void remove(AuthClaims user, UUID cardId);

}
