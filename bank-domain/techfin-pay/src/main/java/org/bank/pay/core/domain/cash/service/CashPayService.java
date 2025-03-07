package org.bank.pay.core.domain.cash.service;

import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.pay.core.domain.cash.Cash;
import org.bank.pay.core.domain.card.PayCard;

import java.math.BigDecimal;

public interface CashPayService {

    void pay(AuthClaims user, PayCard card, Money amount);
    void pay(PayCard card, Money amount);

    void pay(Cash cash, BigDecimal amount);
    void pay(Cash cash, Money amount);
}
