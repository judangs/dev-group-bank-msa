package org.bank.consumer.account;

import org.bank.consumer.fixture.EventFixture;
import org.bank.consumer.integration.kafka.ConsumerIntegrationTest;
import org.bank.core.auth.AuthClaims;
import org.bank.pay.core.domain.owner.OwnerClaims;
import org.bank.pay.core.domain.owner.repository.PayOwnerReader;
import org.bank.pay.fixture.UserFixture;
import org.bank.user.core.event.registration.AccountCreatedEvent;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertAll;

@Tag("카프카를 활용한 사용자 계정 이벤트의  통합 테스트")
public class AccountConsumerKafkaIntegrationTest extends ConsumerIntegrationTest {

    @Autowired
    private PayOwnerReader payOwnerReader;

    private final AuthClaims user = UserFixture.authenticated();


    @Test
    public void 카프카에서_새로운_유저가_생성되었다는_이벤트를_처리한_뒤_pay_도메인의_유저_데이터_초기화가_이루어져야_합니다() {

        AccountCreatedEvent event = EventFixture.signupEvent(user.getUserid());
        kafkaTemplate.send("user.account.created", event);

        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        kafkaTopicMonitor.acks("user.account.created", "user-account-group")
                );


        assertThat(payOwnerReader.findByUserId(user.getUserid())).isPresent();

        payOwnerReader.findByUserId(user.getUserid()).ifPresent(owner -> {
            assertAll(
                    () -> assertThat(owner).isNotNull(),
                    () -> assertThat(owner.getClaims()).isEqualTo(OwnerClaims.of(user)),
                    () -> assertThat(owner.getPaymentCards().isEmpty()).isTrue());
        });
    }
}