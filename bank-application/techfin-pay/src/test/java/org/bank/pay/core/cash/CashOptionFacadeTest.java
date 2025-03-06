package org.bank.pay.core.cash;

import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.core.dto.response.ResponseCodeV2;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.pay.core.domain.cash.PayLimit;
import org.bank.pay.core.domain.cash.service.CashChargeSerivce;
import org.bank.pay.core.domain.owner.PayOwner;
import org.bank.pay.core.domain.owner.PaymentCard;
import org.bank.pay.core.domain.owner.repository.PayOwnerStore;
import org.bank.pay.core.domain.owner.service.PayCardService;
import org.bank.pay.dto.service.request.CashLimitRequest;
import org.bank.pay.dto.service.response.CashBalanceResponse;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CashOptionFacadeTest.IntegrationTest.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CashOptionFacadeTest {

    @Configuration
    @ComponentScan(basePackages = {
            "org.bank.pay.core",
            "org.bank.store.domain.pay",
            "org.bank.store.mysql.core.pay"
    })
    static class IntegrationTest {

    }

    @Autowired
    private CashOptionFacade cashOptionFacade;
    @Autowired
    private PayOwnerStore payOwnerStore;
    @Autowired
    private PayCardService payCardService;
    @Autowired
    private CashChargeSerivce cashChargeSerivce;


    private final AuthClaims user = UserFixture.authenticated();
    private final PaymentCard card = CardFixture.cashable(false);

    @BeforeAll
    void setup() {
        payOwnerStore.save(new PayOwner(user));
        payCardService.register(user, card);
    }

    @Test
    void 사용자가_사용중인_카드의_잔액을_확인합니다() {
        assertThat(cashOptionFacade.checkBalance(user, card.getCardId()).getCode())
                .isEqualTo(ResponseCodeV2.SUCCESS);
    }

    @Test
    void 카드에_캐시를_충전한_뒤_카드의_잔액이_일치하는지_확인합니다() {

        BigDecimal chargeAmount = new BigDecimal(10000);
        Money balance = new Money(chargeAmount);

        cashChargeSerivce.charge(user, card, chargeAmount);
        ResponseDtoV2 response = cashOptionFacade.checkBalance(user, card.getCardId());
        assertAll(
                () -> assertThat(response.getCode()).isEqualTo(ResponseCodeV2.SUCCESS),
                () -> {
                    if(response instanceof CashBalanceResponse cashBalanceResponse) {
                        assertThat(cashBalanceResponse.getBalance()).isEqualTo(balance.getBalance());
                    }
                }
        );
    }

    @Test
    void 사용자가_등록한_카드에_한도가_없는지_확인합니다() {

        BigDecimal maximum = new BigDecimal("99999999999999999999.99");

        PayLimit payLimit = payCardService.get(user, card.getCardId()).getCash().getLimits();
        assertAll(
                () -> assertThat(payLimit.getPerOnce()).isEqualTo(maximum),
                () -> assertThat(payLimit.getPerDaily()).isEqualTo(maximum)
        );
    }

    @Test
    void 사용자가_사용중인_카드의_일일_사용_한도와_일회_사용_한도를_설정합니다() {

        CashLimitRequest cashLimitRequest = new CashLimitRequest(new BigDecimal(100000), new BigDecimal(100000));

        assertThat(cashOptionFacade.applyLimit(user, card.getCardId(), cashLimitRequest).getCode())
                .isEqualTo(ResponseCodeV2.SUCCESS);

    }

    @Test
    void 카드의_일회_사용_한도는_카드의_일일_사용_한도를_초과할_수_없습니다() {

        CashLimitRequest invalidCashLimitRequest = new CashLimitRequest(new BigDecimal(10000), new BigDecimal(100000));
        assertThat(cashOptionFacade.applyLimit(user, card.getCardId(), invalidCashLimitRequest).getCode())
                .isEqualTo(ResponseCodeV2.FAIL);


    }

}
