package org.bank.pay.core.onwer;

import org.bank.core.auth.AuthClaims;
import org.bank.pay.core.domain.owner.PayCardService;
import org.bank.pay.core.domain.owner.PayOwner;
import org.bank.pay.core.domain.owner.PaymentCard;
import org.bank.pay.core.domain.owner.repository.PayOwnerReader;
import org.bank.pay.fixture.CardFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CardServiceTest.UnitTest.class)
public class CardServiceTest {

    @Configuration
    @ComponentScan(basePackages = "org.bank.pay.core.domain.owner")
    static class UnitTest {}

    private final AuthClaims user = new AuthClaims.ConcreteAuthClaims("user-01", "fixture", "user@email.com");


    @Autowired
    private PayCardService payCardService;
    @MockBean
    private PayOwnerReader payOwnerReader;

    private final PayOwner owner = new PayOwner();

    @BeforeEach
    void setup() {
        when(payOwnerReader.findByUserClaims(user)).thenReturn(Optional.of(owner));
        when(payOwnerReader.findPaymentCardsByUser(user)).thenReturn(owner.getPaymentCards());
    }

    @Test
    void 사용자가_이용할_결제_카드를_등록합니다() {
        payCardService.register(user, new PaymentCard());
        assertThat(owner.getPaymentCards().isEmpty()).isFalse();
    }

    @Test
    void 사용자가_등록한_결제_카드를_해지합니다() {
        PaymentCard card = CardFixture.card();
        owner.addPaymentCard(card);

        payCardService.remove(user, card.getCardId());
        assertThat(owner.getPaymentCards().isEmpty()).isTrue();
    }

    @Test
    void 사용자가_등록한_카드_별칭을_수정합니다() {
        PaymentCard card = CardFixture.naming("픽스처");

        owner.addPaymentCard(card);
        payCardService.updateAlias(user, card.getCardId(), "수정된 카드 별칭");
        assertThat(card.getCardName()).isNotEqualTo("픽스처");
    }

    @Test
    void 사용자가_등록한_결제_카드_정보들을_모두_불러옵니다() {

        PaymentCard[] cards = new PaymentCard[]{
                CardFixture.card(),
                CardFixture.card(),
                CardFixture.card()
        };

        for(PaymentCard card: cards) {
            owner.addPaymentCard(card);
        }

        assertThat(payCardService.gets(user).size()).isEqualTo(3);
    }

    @Test
    void 사용자가_등록한_결제_카드중_카드번호와_일치하는_카드_정보를_가져옵니다() {
        PaymentCard[] cards = new PaymentCard[]{
                CardFixture.card(),
                CardFixture.card(),
                CardFixture.card()
        };

        for(PaymentCard card: cards) {
            owner.addPaymentCard(card);
            assertThat(payCardService.get(user, card.getCardId())).isNotNull();
        }
    }
}
