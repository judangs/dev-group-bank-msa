package org.bank.pay.core.domain.cash.service;

import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.pay.core.domain.owner.PaymentCard;
import org.bank.pay.global.domain.card.PayCard;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface CashChargeSerivce {

    void charge(AuthClaims user, PayCard card, BigDecimal amount);
    void charge(PayCard card, BigDecimal amount);
    void charge(PayCard card, Money amount);
    void reserve(AuthClaims user, PaymentCard card, BigDecimal amount, LocalDateTime scheduled);
    void reserve(AuthClaims user, PaymentCard card, BigDecimal amount, BigDecimal balance);
    void cancel(UUID scheduledId);
}
