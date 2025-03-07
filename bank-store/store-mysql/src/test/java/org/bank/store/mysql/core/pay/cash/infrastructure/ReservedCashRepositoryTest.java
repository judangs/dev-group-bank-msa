package org.bank.store.mysql.core.pay.cash.infrastructure;

import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.pay.core.domain.cash.ReservationType;
import org.bank.pay.core.domain.cash.ReservedCharge;
import org.bank.pay.core.domain.owner.PayOwner;
import org.bank.pay.fixture.UserFixture;
import org.bank.pay.core.domain.card.PayCard;
import org.bank.store.mysql.core.pay.unit.CashRepositoryUnitTest;
import org.bank.store.mysql.core.pay.unit.init.TestInitializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CashRepositoryUnitTest.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReservedCashRepositoryTest {


    @Autowired
    private ReservedCashCommandRepository reservedCashCommandRepository;
    @Autowired
    private ReservedCashQueryRepository reservedCashQueryRepository;
    @Autowired
    private TestInitializer testInitializer;

    private final AuthClaims user = UserFixture.authenticated();
    private final PayOwner owner = new PayOwner(user);

    private final Money amount = new Money(10000);

    private PayCard card;


    @BeforeAll
    void setup() {
        testInitializer.card(owner);
        card = testInitializer.getCard(user);
    }

    @Test
    void 데이터베이스에_예약_충전_조건을_저장할_수_있다() {
        LocalDate scheduled = LocalDate.now().plusWeeks(1);

        ReservedCharge reservedCharge = ReservedCharge.of(card, amount, scheduled);
        assertThatCode(() -> reservedCashCommandRepository.save(reservedCharge))
                .doesNotThrowAnyException();
    }

    @Test
    void 데이터베이스에_날짜를_기준으로_예약_충전_조건을_저장할_수_있다() {

        LocalDate scheduled = LocalDate.now().plusWeeks(1);

        ReservedCharge reservedCharge = ReservedCharge.of(card, amount, scheduled);
        assertThatCode(() -> reservedCashCommandRepository.save(reservedCharge))
                .doesNotThrowAnyException();

        reservedCashQueryRepository.findByScheduledId(reservedCharge.getId())
                .ifPresent(reserve -> {

                    assertAll(
                            () -> assertThat(reserve).isNotNull(),
                            () -> assertThat(reserve.getAmount()).isEqualTo(amount),
                            () -> assertThat(reserve.getCardId()).isEqualTo(card.getCardId()),
                            () -> assertThat(reserve.getType()).isEqualTo(ReservationType.DATE),
                            () -> assertThat(reserve.getScheduledDate()).isEqualTo(scheduled)
                    );
                });
    }

    @Test
    void 데이터베이스에_잔금을_기준으로_예약_충전_조건을_저장할_수_있다() {

        Money balance = new Money(5000);

        ReservedCharge reservedCharge = ReservedCharge.of(card, amount, balance);
        assertThatCode(() -> reservedCashCommandRepository.save(reservedCharge))
                .doesNotThrowAnyException();

        reservedCashQueryRepository.findByScheduledId(reservedCharge.getId())
                .ifPresent(reserve -> {

                    assertAll(
                            () -> assertThat(reserve).isNotNull(),
                            () -> assertThat(reserve.getAmount()).isEqualTo(amount),
                            () -> assertThat(reserve.getCardId()).isEqualTo(card.getCardId()),
                            () -> assertThat(reserve.getType()).isEqualTo(ReservationType.BALANCE),
                            () -> assertThat(reserve.getTriggerBalance()).isEqualTo(balance)
                    );
                });

    }

    @Test
    void 데이터베이스에_저장된_예약_충전_조건을_삭제할_수_있다() {

        ReservedCharge reservedCharge = ReservedCharge.of(card, amount, new Money(5000));
        assertThatCode(() -> reservedCashCommandRepository.save(reservedCharge))
                .doesNotThrowAnyException();

        reservedCashCommandRepository.deleteByScheduledId(reservedCharge.getId());
        assertThat(reservedCashQueryRepository.findByScheduledId(reservedCharge.getId()).isEmpty()).isTrue();
    }


    @Test
    void 예약_결제와_관련된_정보를_모두_조회할_수_있다() {
        for(int idx=0; idx<50; idx++) {
            ReservedCharge reservedCharge = new ReservedCharge();
            org.assertj.core.api.Assertions.assertThatCode(() -> reservedCashCommandRepository.save(reservedCharge))
                            .doesNotThrowAnyException();
        }

        assertAll(
                () -> assertThat(reservedCashQueryRepository.findAllReservedCharges(0).getContent().size()).isEqualTo(20),
                () -> assertThat(reservedCashQueryRepository.findAllReservedCharges(1).getContent().size()).isEqualTo(20),
                () -> assertThat(reservedCashQueryRepository.findAllReservedCharges(2).hasNext()).isFalse()
        );
    }
}