package org.bank.pay.core.cash;

import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.pay.core.domain.cash.service.CashChargeSerivce;
import org.bank.pay.core.domain.cash.ReservedCharge;
import org.bank.pay.core.domain.cash.repository.CashReader;
import org.bank.pay.core.domain.cash.repository.ReservedCashReader;
import org.bank.pay.core.domain.cash.repository.ReservedCashStore;
import org.bank.pay.core.domain.owner.PayOwner;
import org.bank.pay.core.domain.owner.PaymentCard;
import org.bank.pay.fixture.CardFixture;
import org.bank.pay.fixture.UserFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CashChargeServiceTest.UnitTest.class)
public class CashChargeServiceTest {

    @Configuration
    @ComponentScan(basePackages = "org.bank.pay.core.domain.cash")
    static class UnitTest {}

    @Autowired
    private CashChargeSerivce cashChargeSerivce;
    @MockBean
    private ReservedCashStore reservedCashStore;
    @MockBean
    private CashReader cashReader;
    @MockBean
    private ReservedCashReader reservedCashReader;

    private final AuthClaims user = UserFixture.authenticated();
    private final PaymentCard card = CardFixture.card();
    private final PayOwner owner = new PayOwner(user);

    @BeforeEach
    void setup() {
        doNothing().when(reservedCashStore).save(any());
        owner.addPaymentCard(card);
    }

    @Test
    void 등록된_사용자의_카드에_캐시를_충전합니다() {
        cashChargeSerivce.charge(user, card, new BigDecimal(10000));
        assertThat(card.getCash().getCredit()).isEqualTo(new Money(10000));
    }

    @Test
    void 예약_충전이_실행되면서_등록된_사용자의_카드에_캐시를_충전합니다() {
        cashChargeSerivce.charge(card.getCash(), card, new BigDecimal(20000));
        assertThat(card.getCash().getCredit()).isEqualTo(new Money(20000));
    }

    @Test
    void 사용자가_사용하는_결제_카드에_특정_날을_기준으로_정기_충전을_예약합니다() {
        LocalDateTime scheduled = LocalDateTime.now().plusWeeks(1);

        cashChargeSerivce.reserve(user, card, new BigDecimal(10000), scheduled);
        verify(reservedCashStore, times(1)).save(any(ReservedCharge.class));
    }

    @Test
    void 사용자가_사용하는_결제_카드에_잔액을_기준으로_정기_충전을_예약합니다() {
        cashChargeSerivce.reserve(user, card, new BigDecimal(10000), new BigDecimal(50000));
        verify(reservedCashStore, times(1)).save(any(ReservedCharge.class));
    }
}
