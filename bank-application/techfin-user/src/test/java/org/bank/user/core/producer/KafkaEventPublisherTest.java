package org.bank.user.core.producer;

import org.assertj.core.api.Assertions;
import org.bank.user.core.domain.account.Credential;
import org.bank.user.core.domain.fixture.AccountFixture;
import org.bank.user.core.domain.mail.AccountVerificationMail;
import org.bank.user.core.domain.mail.VerificationReason;
import org.bank.user.core.producer.registration.AccountRegistrationEventPublisher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = KafkaEventPublisherTest.IntegrationTest.class)
class KafkaEventPublisherTest {

    @Configuration
    @ComponentScan("org.bank.user.core.producer")
    static class IntegrationTest { }


    @Autowired
    private AccountRegistrationEventPublisher accountRegistrationEventPublisher;

    @Test
    @DisplayName("사용자 계정을 생성한 뒤_계정 생성 이벤트를 발송합니다")
    void 사용자_계정을_생성한_뒤_계정_생성_이벤트를_발송합니다() {

        Assertions.assertThatCode(() -> {
            Credential credential = AccountFixture.authenticated("userid-01");
            accountRegistrationEventPublisher.userCreated(new AccountVerificationMail(VerificationReason.CREATE_ACCOUNT, credential));
        }).doesNotThrowAnyException();

    }



}