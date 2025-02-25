package org.bank.pay.core.domain.cash.service;

import org.bank.core.auth.AuthClaims;
import org.bank.pay.core.domain.cash.Cash;
import org.bank.pay.core.domain.owner.PaymentCard;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface CashChargeSerivce {

    void charge(AuthClaims user, PaymentCard card, BigDecimal amount);
    void charge(Cash cash, PaymentCard card, BigDecimal amount);
    void reserve(AuthClaims user, PaymentCard card, BigDecimal amount, LocalDateTime scheduled);
    void reserve(AuthClaims user, PaymentCard card, BigDecimal amount, BigDecimal balance);
    void cancel(UUID scheduledId);
}
