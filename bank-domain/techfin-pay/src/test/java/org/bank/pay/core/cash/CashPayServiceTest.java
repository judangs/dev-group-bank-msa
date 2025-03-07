package org.bank.pay.core.cash;


import org.bank.core.cash.Money;
import org.bank.pay.core.domain.cash.service.CashPayService;
import org.bank.pay.core.domain.card.PaymentCard;
import org.bank.pay.core.unit.CashUnitTest;
import org.bank.pay.fixture.CardFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class CashPayServiceTest extends CashUnitTest {

    @Autowired
    private CashPayService cashPayService;

    private final PaymentCard card = CardFixture.cashable();
    private final Money balance = new Money(100000);
    private final Money usage = new Money(10000);

    @BeforeEach
    void setup() {
        card.getCash().charge(balance);
        balance.withdraw(usage);
    }

    @Test
    void 지불할_금액에_대해_카드의_소유자를_확인하고_카드로_결제를_시도합니다() {

        Money usage = new Money(10000);

        cashPayService.pay(card.getPayOwner().getClaims(), card, usage);
        assertThat(card.getCash().getCredit()).isEqualTo(balance);
    }

    @Test
    void 지불할_금액에_대해_카드로_결제를_시도합니다() {
        Money usage = new Money(10000);

        cashPayService.pay(card, usage);
        assertThat(card.getCash().getCredit()).isEqualTo(balance);
    }

    @Test
    void 지불할_금액에_대해_결제를_시도합니다() {
        Money usage = new Money(10000);

        cashPayService.pay(card.getCash(), usage);
        assertThat(card.getCash().getCredit()).isEqualTo(balance);
    }
}
