package org.bank.consumer.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.bank.core.kafka.KafkaEvent;
import org.bank.user.core.domain.account.service.AccountManagerService;
import org.bank.user.core.event.registration.AccountCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountCreationConsumer {

    private final UserRegisterTask userRegisterTask;
    private final ObjectMapper objectMapper;

    private final AccountManagerService accountManagerService;


    @KafkaListener(topics = "user.account.created",
            groupId = "account-event-group")
    public void registration(ConsumerRecord<String, KafkaEvent> record) {

        AccountCreatedEvent event = objectMapper.convertValue(record.value(), AccountCreatedEvent.class);

        try {
            userRegisterTask.initialize(event.getCredential());
        } catch (Exception e) {
            accountManagerService.withdrawAccount(event.getCredential());
        }
    }
}
