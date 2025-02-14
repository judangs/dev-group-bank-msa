package org.bank.consumer.account;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.assertj.core.api.Assertions;
import org.bank.consumer.integration.IntegrationTest;
import org.bank.core.cash.Money;
import org.bank.core.kafka.KafkaEvent;
import org.bank.pay.core.domain.cash.Cash;
import org.bank.pay.core.domain.cash.repository.CashReader;
import org.bank.pay.core.domain.onwer.OwnerClaims;
import org.bank.user.core.domain.account.Credential;
import org.bank.user.core.domain.fixture.AccountFixture;
import org.bank.user.core.domain.mail.AccountVerificationMail;
import org.bank.user.core.domain.mail.VerificationReason;
import org.bank.user.core.event.registration.AccountCreatedEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;


@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = IntegrationTest.class)
public class AccountCreationConsumerIntegrationTest {

    @Autowired
    private AccountCreationConsumer accountCreationConsumer;

    private AccountCreatedEvent event;

    @BeforeAll
    void setUp() {
        Credential credential = AccountFixture.authenticated("userid-01");

        AccountVerificationMail mail = new AccountVerificationMail(VerificationReason.CREATE_ACCOUNT, credential);
        event = AccountCreatedEvent.of(mail.getVerifierInfos());
    }

    @Autowired
    private CashReader cashReader;

    @Test
    public void 카프카에서_새로운_유저가_생성되었다는_이벤트를_처리한_뒤_pay_사용자를_위한_데이터_초기화가_이루어져야_합니다() {

        ConsumerRecord<String, KafkaEvent> record = new ConsumerRecord<>("user.account.created", 0, 0, UUID.randomUUID().toString(), event);
        accountCreationConsumer.registration(record);

        Cash cash = cashReader.findByOwnerClaims(OwnerClaims.of(event.getCredential()));
        Assertions.assertThat(cash.getCredit()).isEqualTo(new Money());
        Assertions.assertThat(cash.getPayOwner().getClaims()).isEqualTo(OwnerClaims.of(event.getCredential()));

    }
}