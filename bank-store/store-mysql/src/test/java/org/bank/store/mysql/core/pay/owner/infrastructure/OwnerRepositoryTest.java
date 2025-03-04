package org.bank.store.mysql.core.pay.owner.infrastructure;

import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.pay.core.domain.owner.OwnerClaims;
import org.bank.pay.core.domain.owner.PaymentCard;
import org.bank.pay.core.domain.owner.repository.PayOwnerReader;
import org.bank.pay.fixture.UserFixture;
import org.bank.pay.global.domain.card.PayCard;
import org.bank.store.mysql.core.pay.unit.OwnerRepositoryUnitTest;
import org.bank.store.mysql.core.pay.unit.init.TestInitializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = OwnerRepositoryUnitTest.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OwnerRepositoryTest {

    @Autowired
    private PayOwnerReader payOwnerReader;
    @Autowired
    private TestInitializer testInitializer;

    private final AuthClaims user = UserFixture.authenticated();

    private PayCard card;

    @BeforeAll
    public void setUp() {
        testInitializer.init(user);
        card = testInitializer.getCard(user);
    }

    @Test
    public void 데이터베이스에서_유저_아이디_정보와_일치하는_payOnwer를_조회합니다() {
        assertThat(payOwnerReader.findByUserId(user.getUserid())).isPresent();

        payOwnerReader.findByUserId(user.getUserid()).ifPresent(payOwner -> {
            assertAll(
                    () -> assertThat(payOwner).isNotNull(),
                    () -> assertThat(payOwner.getClaims()).isEqualTo(OwnerClaims.of(user)),
                    () -> assertThat(payOwner.getPaymentCards().isEmpty()).isFalse()
            );
        });
    }

    @Test
    public void 데이터베이스에서_유저_정보와_일치하는_payOnwer를_조회합니다() {

        assertThat(payOwnerReader.findByUserClaims(user)).isPresent();

        payOwnerReader.findByUserClaims(user).ifPresent(payOwner -> {
            assertAll(
                    () -> assertThat(payOwner).isNotNull(),
                    () -> assertThat(payOwner.getClaims()).isEqualTo(OwnerClaims.of(user)),
                    () -> assertThat(payOwner.getPaymentCards().isEmpty()).isFalse()
            );
        });
    }

    @Test
    public void 데이터베이스에서_유저가_등록한_결제_카드_정보를_조회할_수_있습니다() {

        assertThat(payOwnerReader.findPaymentCardByOwnerAndCard(user, card.getCardId())).isPresent();

        payOwnerReader.findPaymentCardByOwnerAndCard(user, card.getCardId()).ifPresent(payCard -> {

                assertAll(
                        () -> assertThat(payCard.getCardName()).isEqualTo(card.getCardName()),
                        () -> assertThat(payCard.getCardNumber()).isEqualTo(card.getCardNumber()),
                        () -> assertThat(payCard.getCvc()).isEqualTo(card.getCvc()),
                        () -> assertThat(payCard.getPayOwner().getClaims()).isEqualTo(OwnerClaims.of(user)),
                        () -> assertThat(payCard.getCash().getCredit()).isEqualTo(new Money(0))
                );
        });
    }

    @Test
    public void 데이터베이스에서_유저가_등록한_결제_카드를_정보를_모두_조회할_수_있습니다() {

        assertThat(payOwnerReader.findPaymentCardsByUser(user).isEmpty()).isFalse();

        if(card instanceof PaymentCard paymentCard) {
            assertThat(payOwnerReader.findPaymentCardsByUser(user).stream())
                    .contains(paymentCard);
        }
    }
}