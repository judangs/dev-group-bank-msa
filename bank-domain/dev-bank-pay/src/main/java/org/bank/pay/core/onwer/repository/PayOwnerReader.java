package org.bank.pay.core.onwer.repository;

import org.bank.core.auth.AuthClaims;
import org.bank.pay.core.onwer.OwnerClaims;
import org.bank.pay.core.onwer.PayOwner;
import org.bank.pay.core.onwer.PaymentCard;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PayOwnerReader {

    Optional<PayOwner> findByUserClaims(OwnerClaims claims);
    Optional<PayOwner> findByUserClaims(AuthClaims claims);
    Optional<PayOwner> findByUserClaimsAndRoles(AuthClaims claims, String roles);
    List<PaymentCard> findAllPaymentCardsByOwner(PayOwner payOwner);
    Optional<PaymentCard> findPaymentCardByOwnerAndCard(PayOwner payOwner, UUID cardId);
}
