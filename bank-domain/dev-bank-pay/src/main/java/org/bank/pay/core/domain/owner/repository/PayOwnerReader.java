package org.bank.pay.core.domain.owner.repository;

import org.bank.core.auth.AuthClaims;
import org.bank.pay.core.domain.owner.OwnerClaims;
import org.bank.pay.core.domain.owner.PayOwner;
import org.bank.pay.core.domain.owner.PaymentCard;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PayOwnerReader {

    Optional<PayOwner> findByUserClaims(OwnerClaims claims);
    Optional<PayOwner> findByUserClaims(AuthClaims claims);
    List<PaymentCard> findAllPaymentCardsByOwner(PayOwner payOwner);
    List<PaymentCard> findPaymentCardsByUser(AuthClaims user);
    Optional<PaymentCard> findPaymentCardByOwnerAndCard(AuthClaims user, UUID cardId);
    Optional<PaymentCard> findPaymentCardByOwnerAndCard(PayOwner payOwner, UUID cardId);
}
