package org.bank.consumer.core.user;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.bank.core.kafka.KafkaEvent;
import org.bank.user.core.domain.account.service.AccountManagerService;
import org.bank.user.core.event.registration.AccountCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountEventConsumer {

    private final UserRegisterTask userRegisterTask;

    private final AccountManagerService accountManagerService;

    @KafkaListener(topics = "user.account.created",
            groupId = "account-event-group")
    public void registration(ConsumerRecord<String, KafkaEvent> record) {

        AccountCreatedEvent event = (AccountCreatedEvent) record.value();

        try {
            userRegisterTask.initialize(event.getCredential());
        } catch (Exception e) {
            accountManagerService.withdrawAccount(event.getCredential());
        }
    }
}
