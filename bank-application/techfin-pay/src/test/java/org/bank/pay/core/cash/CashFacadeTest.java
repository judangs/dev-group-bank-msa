package org.bank.pay.core.cash;

import org.bank.core.auth.AuthClaims;
import org.bank.core.dto.response.ResponseCodeV2;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.pay.core.domain.owner.PayOwner;
import org.bank.pay.core.domain.owner.PaymentCard;
import org.bank.pay.core.domain.owner.repository.PayOwnerStore;
import org.bank.pay.core.domain.owner.service.PayCardService;
import org.bank.pay.dto.service.request.ChargeRequest;
import org.bank.pay.dto.service.request.ReservedChargeRequest;
import org.bank.pay.dto.service.response.PaymentReserveResponse;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CashFacadeTest.IntegrationTest.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CashFacadeTest {

    @Configuration
    @ComponentScan(basePackages = {
            "org.bank.pay.core",
            "org.bank.store.domain.pay",
            "org.bank.store.mysql.core.pay"
    })
    static class IntegrationTest {

    }

    @Autowired
    private CashFacade cashFacade;
    @Autowired
    private PayOwnerStore payOwnerStore;
    @Autowired
    private PayCardService payCardService;

    private final AuthClaims user = UserFixture.authenticated();
    private final PaymentCard card = CardFixture.cashable(false);

    @BeforeAll
    void setup() {
        payOwnerStore.save(new PayOwner(user));
        payCardService.register(user, card);
    }

    @Test
    void 온라인_재화_상품을_구매하고_결제를_예약합니다() {

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
    }

    @Test
    void 온라인_재화_상품에_대해_기한과_잔액에_대한_조건_결제를_설정합니다() {

        ReservedChargeRequest reservedChargeRequest = new ReservedChargeRequest(card.getCardId(), new BigDecimal(10000), LocalDateTime.now().plusWeeks(1), new BigDecimal(5000));
        assertThat(cashFacade.reserveReCharge(user, reservedChargeRequest).getCode())
                .isEqualTo(ResponseCodeV2.SUCCESS);
    }





}