package org.bank.pay.core.card;

import org.bank.core.auth.AuthClaims;
import org.bank.core.dto.response.ResponseCodeV2;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.pay.core.domain.card.PaymentCard;
import org.bank.pay.core.domain.owner.service.PayCardService;
import org.bank.pay.core.integration.IntegrationTest;
import org.bank.pay.dto.service.request.CardPaymentRequest;
import org.bank.pay.dto.service.response.PaymentCardsListResponse;
import org.bank.pay.fixture.CardFixture;
import org.bank.pay.fixture.UserFixture;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CardFacadeTest extends IntegrationTest {

    @Autowired
    private CardFacade cardFacade;
    @Autowired
    private PayCardService payCardService;

    private final AuthClaims user = UserFixture.authenticated();


    @BeforeAll
    void setup() {
        integrationTestInitializer.init(user);
    }


    @Test
    void 사용자_소유의_카드를_등록합니다() {
        CardPaymentRequest request = new CardPaymentRequest("1234-1111-1432-1234", "123", "1122", LocalDate.now().plusYears(5), "카드 별칭");
        assertThat(cardFacade.registerCard(user, request).getCode())
                .isEqualTo(ResponseCodeV2.SUCCESS);

        assertThat(integrationTestInitializer.find(user)).isPresent();
        integrationTestInitializer.find(user).ifPresent(owner ->
                assertThat(owner.match(request.getCardNumber()).isPresent()));
    }

    @Test
    void 사용자_소유의_카드_정보를_불러옵니다() {
        payCardService.register(user, CardFixture.cashable(false));

        ResponseDtoV2 response = cardFacade.getRegisteredCards(user);

        assertAll(
                () -> assertThat(response.getCode()).isEqualTo(ResponseCodeV2.SUCCESS),
                () -> assertThat(response).isExactlyInstanceOf(PaymentCardsListResponse.class),
                () -> assertThat(response).isInstanceOf(PaymentCardsListResponse.class)
                        .extracting("cards").isInstanceOf(List.class)
                        .satisfies(cards -> {
                            assertThat((List<?>) cards).isNotEmpty();
                        }),
                () -> assertThat(integrationTestInitializer.find(user)).isPresent()
        );
    }

    @Test
    void 사용자_소유의_카드_별칭을_변경합니다() {
        PaymentCard card = CardFixture.naming("결제 카드", false);
        payCardService.register(user, card);
        cardFacade.updateCardAlias(user, card.getCardId(), "카드 이름 변경");

        assertThat(payCardService.get(user, card.getCardId()).getCardNumber()).isNotEqualTo(card.getCardName());
    }

    @Test
    void 사용자_소유의_결제_카드를_해지합니다() {
        PaymentCard card = CardFixture.naming("결제 카드", false);
        payCardService.register(user, card);

        assertThat(cardFacade.deleteRegisteredCard(user, card.getCardId()).getCode())
                .isEqualTo(ResponseCodeV2.SUCCESS);
    }
}