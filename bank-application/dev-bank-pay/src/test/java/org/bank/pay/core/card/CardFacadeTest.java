package org.bank.pay.core.card;

import org.bank.core.auth.AuthClaims;
import org.bank.core.dto.response.ResponseCodeV2;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.pay.core.domain.owner.PayOwner;
import org.bank.pay.core.domain.owner.PaymentCard;
import org.bank.pay.core.domain.owner.repository.PayOwnerReader;
import org.bank.pay.core.domain.owner.repository.PayOwnerStore;
import org.bank.pay.core.domain.owner.service.PayCardService;
import org.bank.pay.dto.service.request.CardPaymentRequest;
import org.bank.pay.dto.service.response.PaymentCardsListResponse;
import org.bank.pay.fixture.CardFixture;
import org.bank.pay.fixture.UserFixture;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CardFacadeTest.IntegrationTest.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CardFacadeTest {

    @Configuration
    @ComponentScan(basePackages = {
            "org.bank.pay.core.card",
            "org.bank.pay.core.domain.owner",
            "org.bank.store.domain.pay",
            "org.bank.store.mysql.core.pay"
    })
    static class IntegrationTest {}

    @Autowired
    private CardFacade cardFacade;
    @Autowired
    private PayCardService payCardService;
    @Autowired
    private PayOwnerStore payOwnerStore;
    @Autowired
    private PayOwnerReader payOwnerReader;

    private final AuthClaims user = UserFixture.authenticated();
    private final PayOwner owner = new PayOwner(user);


    @BeforeAll
    void setup() {
        payOwnerStore.save(owner);
    }


    @Test
    void 사용자_소유의_카드를_등록합니다() {
        CardPaymentRequest request = new CardPaymentRequest("1234-1111-1432-1234", "123", "1122", LocalDate.now().plusYears(5), "카드 별칭");
        assertThat(cardFacade.registerCard(user, request).getCode())
                .isEqualTo(ResponseCodeV2.SUCCESS);

        assertThat(payOwnerReader.findPaymentCardsByUser(user).isEmpty()).isFalse();
    }

    @Test
    void 사용자_소유의_카드_정보를_불러옵니다() {
        payCardService.register(user, CardFixture.cashable(false));

        ResponseDtoV2 response = cardFacade.getRegisteredCards(user);

        if(response instanceof PaymentCardsListResponse paymentCardsListResponse) {
            assertThat(paymentCardsListResponse.getCards().isEmpty()).isFalse();
            assertThat(payOwnerReader.findPaymentCardsByUser(user).isEmpty()).isFalse();
        }
    }

    @Test
    void 사용자_소유의_카드_별칭을_변경합니다() {
        PaymentCard card = CardFixture.naming("결제 카드", false);
        payCardService.register(user, card);

        cardFacade.updateCardAlias(user, card.getCardId(), "카드 이름 변경");
        payOwnerReader.findPaymentCardByOwnerAndCard(user, card.getCardId())
                .ifPresent(payCard -> assertThat(payCard.getCardName()).isNotEqualTo(card.getCardName()));
    }

    @Test
    void 사용자_소유의_결제_카드를_해지합니다() {

        PaymentCard card = CardFixture.naming("결제 카드", false);
        payCardService.register(user, card);

        assertThat(cardFacade.deleteRegisteredCard(user, card.getCardId()).getCode())
                .isEqualTo(ResponseCodeV2.SUCCESS);
    }
}