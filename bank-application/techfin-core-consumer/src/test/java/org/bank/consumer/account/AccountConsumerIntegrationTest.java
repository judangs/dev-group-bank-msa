package org.bank.consumer.account;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.bank.consumer.core.user.AccountEventConsumer;
import org.bank.consumer.fixture.EventFixture;
import org.bank.core.auth.AuthClaims;
import org.bank.core.kafka.KafkaEvent;
import org.bank.pay.core.domain.owner.OwnerClaims;
import org.bank.pay.core.domain.owner.repository.PayOwnerReader;
import org.bank.pay.fixture.UserFixture;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@ActiveProfiles({"test"})
@Tag("카프카를 활용하지 않은 사용자 계정 이벤트의 통합 테스트")
public class AccountConsumerIntegrationTest {

    @Autowired
    private AccountEventConsumer accountCreationConsumer;
    @Autowired
    private PayOwnerReader payOwnerReader;

    private final AuthClaims user = UserFixture.authenticated();

    @Test
    void 카프카에서_새로운_유저가_생성되었다는_이벤트를_처리해_pay_도메인_사용자_데이터를_초기화합니다() {
        ConsumerRecord<String, KafkaEvent> record = new ConsumerRecord<>("user.account.created", 0, 0, UUID.randomUUID().toString(), EventFixture.signupEvent(user.getUserid()));
        accountCreationConsumer.registration(record);

        assertThat(payOwnerReader.findByUserId(user.getUserid())).isPresent();

        payOwnerReader.findByUserId(user.getUserid()).ifPresent(owner -> {
            assertAll(
                    () -> assertThat(owner).isNotNull(),
                    () -> assertThat(owner.getClaims()).isEqualTo(OwnerClaims.of(user)),
                    () -> assertThat(owner.getPaymentCards().isEmpty()).isTrue()
            );
        });
    }
}
