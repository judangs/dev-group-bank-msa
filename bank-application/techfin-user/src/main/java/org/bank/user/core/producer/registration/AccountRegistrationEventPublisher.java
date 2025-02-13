package org.bank.user.core.producer.registration;


import lombok.RequiredArgsConstructor;
import org.bank.core.kafka.KafkaEvent;
import org.bank.user.core.domain.mail.AccountVerificationMail;
import org.bank.user.core.domain.mail.VerificationReason;
import org.bank.user.core.event.registration.AccountCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountRegistrationEventPublisher {

    private final String USER_CREATED_TOPIC = "user.account.created";

    private final KafkaTemplate<String, KafkaEvent> kafkaTemplate;

    @Async
    public void userCreated(AccountVerificationMail info) {
        if(info.getReason().equals(VerificationReason.CREATE_ACCOUNT)) {
            kafkaTemplate.send(USER_CREATED_TOPIC, AccountCreatedEvent.of(info.getVerifierInfos()));
        }
    }


}
