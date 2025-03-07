package org.bank.store.mysql.core.pay.cash.infrastructure;

import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.pay.core.domain.cash.Cash;
import org.bank.pay.core.domain.owner.PayOwner;
import org.bank.pay.core.domain.card.PaymentCard;
import org.bank.pay.fixture.CardFixture;
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

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CashRepositoryUnitTest.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CashRepositoryTest {

    @Autowired
    private CashComandRepository cashCommandRepository;
    @Autowired
    private CashQueryRepository cashQueryRepository;
    @Autowired
    private TestInitializer testInitializer;

    private final AuthClaims user = UserFixture.authenticated();
    private PayCard card;

    @BeforeAll
    void setup() {
        testInitializer.card(new PayOwner(user));
        card = testInitializer.getCard(user);
    }


    @Test
    public void 데이터베이스에_캐시_엔티티를_저장할_수_있다() {
        assertThatCode(() -> cashCommandRepository.save(CardFixture.cashable(false).getCash()))
                .doesNotThrowAnyException();
    }

    @Test
    public void 데이터베이스에서_사용자가_가지고_있는_카드의_아이디로_캐시를_조회할_수_있다() {

        Cash cash = cashQueryRepository.findByClaimsAndCardId(user, card.getCardId());
        assertAll(
                () -> assertThat(cash).isNotNull(),
                () -> assertThat(cash.getOwnerId()).isEqualTo(user.getUserid()),
                () -> assertThat(cash.getCredit()).isEqualTo(new Money(0)),
                () -> assertThat(cash.getDailyCurrency()).isEqualTo(new Money(0)),
                () -> assertThat(cash.getLimits().getPerOnce()).isEqualTo(new BigDecimal("99999999999999999999.99")),
                () -> assertThat(cash.getLimits().getPerDaily()).isEqualTo(new BigDecimal("99999999999999999999.99"))

        );
    }

    @Test
    public void 데이터베이스에서_사용자가_가지고_있는_카드로_캐시를_조회할_수_있다() {

        if(card instanceof PaymentCard paymentCard) {
            Cash cash = cashQueryRepository.findByClaimsAndCard(user, paymentCard);
            assertAll(
                    () -> assertThat(cash).isNotNull(),
                    () -> assertThat(cash.getCredit()).isEqualTo(new Money(0))
            );
        }

    }
  
}