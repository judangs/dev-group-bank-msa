package org.bank.pay.core.domain.owner.repository;

import org.bank.core.auth.AuthClaims;
import org.bank.pay.core.domain.owner.PayOwner;
import org.bank.pay.core.domain.owner.PaymentCard;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PayOwnerReader {

    Optional<PayOwner> findByUserId(String userid);
    Optional<PayOwner> findByUserClaims(AuthClaims claims);
    List<PaymentCard> findPaymentCardsByUser(AuthClaims user);
    Optional<PaymentCard> findPaymentCardByOwnerAndCard(AuthClaims user, UUID cardId);
}
