package org.bank.pay.core.domain.cash.service;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.pay.core.domain.cash.Cash;
import org.bank.pay.core.domain.cash.repository.CashStore;
import org.bank.pay.core.domain.owner.PaymentCard;
import org.bank.pay.global.domain.card.PayCard;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CashPayServiceImpl implements CashPayService {

    private final CashStore cashStore;

    @Override
    public void pay(AuthClaims user, PayCard card, Money amount) {
        if(card instanceof PaymentCard paymentCard) {
            if(paymentCard.getPayOwner().getClaims().equals(user)) {
                card.getCash().pay(amount);
                cashStore.save(card.getCash());
            }
        }
    }

    @Override
    public void pay(PayCard card, Money amount) {
        card.getCash().pay(amount);
        cashStore.save(card.getCash());
    }

    @Override
    public void pay(Cash cash, BigDecimal amount) {
        cash.pay(amount);
        cashStore.save(cash);
    }

    @Override
    public void pay(Cash cash, Money amount) {
        cash.pay(amount);
        cashStore.save(cash);
    }
}
