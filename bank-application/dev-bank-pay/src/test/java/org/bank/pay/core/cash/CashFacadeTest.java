package org.bank.pay.core.cash;

import org.bank.core.auth.AuthClaims;
import org.bank.core.dto.response.ResponseCode;
import org.bank.core.dto.response.ResponseDto;
import org.bank.pay.core.onwer.OwnerClaims;
import org.bank.pay.core.onwer.PaymentCard;
import org.bank.pay.core.onwer.PaymentCardService;
import org.bank.pay.dto.request.ChargeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CashFacadeTest.TestConfig.class)
class CashFacadeTest {

    @Configuration
    @ComponentScan(basePackages ={
            "org.bank.pay.core",
            "org.bank.store.mysql.core.pay"

    })
    static class TestConfig {

    }

    AuthClaims claims = new OwnerClaims("fixture-id", "fixture", "fixture@email.com");

    @Autowired
    @Qualifier("userRegisterTask")
    private UserRegisterTask userRegisterTask;

    @Autowired
    private CashFacade cashFacade;

    @Autowired
    private PaymentCardService paymentCardService;

    private PaymentCard paymentCard;


    @BeforeEach
    public void setUp() {
        userRegisterTask.initialize(claims);
        paymentCard = paymentCardService.registerCard(claims, PaymentCard.of("테스트 카드", "1234 1234 1234 1234", "000", "12", LocalDate.of(2025, 3, 30)));

    }

    @Test
    void chargeCash() {
        ChargeRequest chargeRequest = new ChargeRequest(paymentCard.getCardId(), new BigDecimal(10000));
        ResponseDto chargeResponse = cashFacade.chargeCash(claims, chargeRequest);
        assertEquals(ResponseCode.SUCCESS, chargeResponse.getCode());
    }
}