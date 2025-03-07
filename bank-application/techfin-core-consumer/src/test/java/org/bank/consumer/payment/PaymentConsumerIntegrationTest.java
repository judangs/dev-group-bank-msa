package org.bank.consumer.payment;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.bank.consumer.core.product.PurchasedEventConsumer;
import org.bank.consumer.fixture.EventFixture;
import org.bank.consumer.integration.init.TestInitializer;
import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.core.kafka.KafkaEvent;
import org.bank.pay.core.domain.card.PaymentCard;
import org.bank.pay.core.domain.owner.repository.PayOwnerReader;
import org.bank.pay.core.event.product.PurchasedEvent;
import org.bank.pay.fixture.UserFixture;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Tag("카프카를 활용하지 않은 결제 그룹 이벤트의 통합 테스트")
@Import(TestInitializer.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles({"test"})
@SpringBootTest
public class PaymentConsumerIntegrationTest {

    @Autowired
    private TestInitializer testInitializer;
    @Autowired
    private PurchasedEventConsumer purchasedEventConsumer;
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
    void 사용자_캐시_충전_이벤트를_처리합니다() {

        Money amount = new Money(10000);
        PurchasedEvent charge = EventFixture.charge(user, card.getCardId(), amount);
        ConsumerRecord<String, KafkaEvent> record = new ConsumerRecord<>("user.account.created", 0, 0, UUID.randomUUID().toString(), charge);

        purchasedEventConsumer.charged(record);

        assertThat(payOwnerReader.findPaymentCardByOwnerAndCard(user, card.getCardId())).isPresent();
        payOwnerReader.findPaymentCardByOwnerAndCard(user, card.getCardId()).ifPresent(card -> {
            assertAll(
                    () -> assertThat(card.getCash().getCredit())
                            .isEqualTo(new Money(TestInitializer.INITIALBALANCE.getBalance().add(amount.getBalance())))
            );
        });
    }
}
