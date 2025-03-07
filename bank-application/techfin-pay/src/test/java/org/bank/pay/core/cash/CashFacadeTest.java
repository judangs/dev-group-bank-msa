package org.bank.pay.core.cash;

import org.assertj.core.api.Assertions;
import org.bank.core.auth.AuthClaims;
import org.bank.core.dto.response.ResponseCodeV2;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.pay.core.domain.card.PayCard;
import org.bank.pay.core.integration.IntegrationTest;
import org.bank.pay.dto.service.request.ChargeRequest;
import org.bank.pay.dto.service.request.ReservedChargeRequest;
import org.bank.pay.dto.service.response.PaymentReserveResponse;
import org.bank.pay.fixture.UserFixture;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CashFacadeTest extends IntegrationTest {

    @Autowired
    private CashFacade cashFacade;

    private final AuthClaims user = UserFixture.authenticated();
    private Optional<PayCard> optionalPayCard;

    @BeforeAll
    void setup() {
        integrationTestInitializer.init(user);
        this.optionalPayCard = integrationTestInitializer.card(user);
    }

    @Test
    void 온라인_재화_상품을_구매하고_결제를_예약합니다() {
        Assertions.assertThat(optionalPayCard).isPresent();
        optionalPayCard.ifPresent(card -> {
            ChargeRequest chargeRequest = new ChargeRequest(card.getCardId(), new BigDecimal(10000));
            ResponseDtoV2 response = cashFacade.purchase(user, chargeRequest);

            assertAll(
                    () -> assertThat(response.getCode()).isEqualTo(ResponseCodeV2.SUCCESS),
                    () -> {
                        if(response instanceof PaymentReserveResponse paymentReserveResponse) {
                            assertThat(paymentReserveResponse.getReserve()).isNotNull();
                        }
                    }
            );
        });
    }

    @Test
    void 온라인_재화_상품에_대해_기한과_잔액에_대한_조건_결제를_설정합니다() {
        Assertions.assertThat(optionalPayCard).isPresent();
        optionalPayCard.ifPresent(card -> {
            ReservedChargeRequest reservedChargeRequest = new ReservedChargeRequest(card.getCardId(), new BigDecimal(10000), LocalDateTime.now().plusWeeks(1), new BigDecimal(5000));
            assertThat(cashFacade.reserveReCharge(user, reservedChargeRequest).getCode())
                    .isEqualTo(ResponseCodeV2.SUCCESS);
        });
    }
}