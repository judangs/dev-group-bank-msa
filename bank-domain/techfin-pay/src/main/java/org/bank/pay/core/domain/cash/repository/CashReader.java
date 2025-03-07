package org.bank.pay.core.domain.cash.repository;

import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.pay.core.domain.cash.Cash;
import org.bank.pay.core.domain.card.PaymentCard;

import java.util.UUID;

public interface CashReader {

    Cash findByClaimsAndCardId(AuthClaims user, UUID cardId);
    Cash findByClaimsAndCard(AuthClaims user, PaymentCard card);
    Money findBalanceByOwnerClaims(AuthClaims user, UUID cardId);
}
