package org.bank.pay.core.domain.cash.service;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.pay.core.domain.cash.ChargeConstraints;
import org.bank.pay.core.domain.cash.ReservedCharge;
import org.bank.pay.core.domain.cash.repository.CashStore;
import org.bank.pay.core.domain.cash.repository.ReservedCashStore;
import org.bank.pay.core.domain.card.PaymentCard;
import org.bank.pay.core.domain.card.PayCard;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CashCharger implements CashChargeSerivce {

    private final CashStore cashStore;
    private final ReservedCashStore reservedCashStore;

    @Override
    public void charge(AuthClaims user, PayCard card, BigDecimal amount) {
        ChargeConstraints.validate(card, amount);
        card.getCash().charge(new Money(amount));
        cashStore.save(card.getCash());
    }

    @Override
    public void charge(PayCard card, Money amount) {
        ChargeConstraints.validate(card, amount.getBalance());
        card.getCash().charge(amount);
        cashStore.save(card.getCash());
    }

    @Override
    public void charge(PayCard card, BigDecimal amount) {
        ChargeConstraints.validate(card, amount);
        card.getCash().charge(new Money(amount));
        cashStore.save(card.getCash());
    }

    @Override
    public void reserve(AuthClaims authClaims, PaymentCard card, BigDecimal amount, LocalDateTime scheduled) {
        ReservedCharge reservedCharge = ReservedCharge.of(card, amount, scheduled.toLocalDate());
        ChargeConstraints.validate(reservedCharge);
        reservedCashStore.save(reservedCharge);
    }

    public void reserve(AuthClaims authClaims, PaymentCard card, BigDecimal amount, BigDecimal balance) {
        ReservedCharge reservedCharge = ReservedCharge.of(card, amount, balance);
        ChargeConstraints.validate(reservedCharge);
        reservedCashStore.save(reservedCharge);
    }

    @Override
    public void cancel(UUID scheduledId) {
        reservedCashStore.deleteByScheduledId(scheduledId);
    }
}
