package org.bank.pay.fixture;

import org.bank.core.cash.Money;
import org.bank.pay.core.domain.cash.ReservedCharge;
import org.bank.pay.global.domain.card.PayCard;

import java.time.LocalDate;

public class ReserveCashFixture {

    public static ReservedCharge scheduled(PayCard card, Money amount, LocalDate schedule) {
        return ReservedCharge.of(card, amount, schedule);
    }

    public static ReservedCharge balance(PayCard card, Money amount, Money balance) {
        return ReservedCharge.of(card, amount, balance);
    }
}
