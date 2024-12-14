package org.bank.pay.core.onwer.repository;

import org.bank.pay.core.onwer.OwnerClaims;
import org.bank.pay.core.onwer.PayOwner;
import org.bank.pay.core.onwer.PaymentCard;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PayOwnerReader {

    Optional<PayOwner> findByUserClaims(OwnerClaims claims);
    List<PaymentCard> findAllPaymentCardsByOwner(PayOwner payOwner);
    Optional<PaymentCard> findPaymentCardByOwnerAndCard(PayOwner payOwner, UUID cardId);
}
