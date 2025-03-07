package org.bank.pay.core.payment;

import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.core.payment.Product;
import org.bank.pay.core.event.product.Category.CategoryType;
import org.bank.pay.core.producer.product.PurchasedEventPublisher;
import org.bank.pay.fixture.CardFixture;
import org.bank.pay.global.exception.PaymentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PaymentServiceTest.UnitTest.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PaymentServiceTest {

    @Configuration
    @ComponentScan(basePackages = {
            "org.bank.pay.core.payment",
    })
    static class UnitTest { }

    @Autowired
    private PaymentService paymentService;
    @MockBean
    private PurchasedEventPublisher purchasedEventPublisher;

    private final AuthClaims user = new AuthClaims.ConcreteAuthClaims("user", "fixture", "user@email.com");
    private final Product product = new Product("새로운 상품 구매", 10000, 1);

    @Test
    void 캐시_충전_요청을_통해_네이버_페이_단건_결제예약_api를_호출하고_결제창_URL을_받아옵니다() {
        String window = paymentService.cache(user, CardFixture.card().getCardId(), new Money(10000));
        assertThat(window).isNotNull();
    }

    @Test
    void 상품_구매_요청을_통해_네이버_페이_단건_결제예약_api를_호출하고_결제창_url을_받아옵니다() {
        String window = paymentService.request(user, product, CategoryType.CASHABLE);
        assertThat(window).isNotNull();
    }

    @Test
    void 현재_지원하지_않는_상품타입에_대한_구매_요청에_대해_결제_예외를_반환합니다() {
        assertThrows(PaymentException.class, () -> paymentService.request(user, product, CategoryType.ETC));
    }
}