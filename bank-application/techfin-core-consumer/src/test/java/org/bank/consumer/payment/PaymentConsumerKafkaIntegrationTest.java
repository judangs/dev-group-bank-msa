package org.bank.consumer.payment;

import org.bank.consumer.fixture.EventFixture;
import org.bank.consumer.integration.init.TestInitializer;
import org.bank.consumer.integration.kafka.ConsumerIntegrationTest;
import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.pay.core.domain.owner.PaymentCard;
import org.bank.pay.core.domain.owner.repository.PayOwnerReader;
import org.bank.pay.core.event.product.PurchasedEvent;
import org.bank.pay.fixture.UserFixture;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertAll;

@Tag("카프카를 활용한 결제 그룹 이벤트의 통합 테스트")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PaymentConsumerKafkaIntegrationTest extends ConsumerIntegrationTest {

    @Autowired
    private TestInitializer testInitializer;
    @Autowired
    private PayOwnerReader payOwnerReader;


    private final AuthClaims user = UserFixture.authenticated();
    private PaymentCard card;

    @BeforeAll
    void setup() {
        testInitializer.init(user);
        card = testInitializer.getCard(user);
    }

    @Test
    public void 카프카로부터_사용자_캐시를_충전하는_이벤트를_받아_처리합니다()  {
        Money amount = new Money(10000);
        PurchasedEvent chargeEvent = EventFixture.charge(user, card.getCardId(), amount);
        kafkaTemplate.send("pay.cachable.charged", chargeEvent);

        await().atMost(20, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        kafkaTopicMonitor.acks("pay.cachable.charged", "purchased-event-group")
                );

        assertThat(payOwnerReader.findPaymentCardByOwnerAndCard(user, card.getCardId())).isPresent();
        payOwnerReader.findPaymentCardByOwnerAndCard(user, card.getCardId()).ifPresent(card -> {
            assertAll(
                    () -> assertThat(card.getCash().getCredit())
                            .isEqualTo(new Money(TestInitializer.INITIALBALANCE.getBalance().add(amount.getBalance())))
            );
        });
    }
}
