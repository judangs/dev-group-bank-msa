package org.bank.pay.core.domain.familly.service;

import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.pay.core.domain.cash.Cash;
import org.bank.pay.core.domain.card.PaymentCard;

import java.util.UUID;

public interface FamilyPaymentService {

    Cash check(AuthClaims user, UUID cardId);
    Cash check(UUID familyId);
    void deposit(UUID familyId, AuthClaims from, PaymentCard card, Money amount);
    void withdraw(UUID familyId, Money amount);
}
