package org.bank.consumer.core.family;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.bank.consumer.global.kafka.EventTypeCaster;
import org.bank.core.cash.PaymentProcessingException;
import org.bank.core.kafka.KafkaEvent;
import org.bank.pay.core.event.family.kafka.CashConversionEvent;
import org.bank.pay.core.event.family.kafka.InviteEvent;
import org.bank.pay.core.event.family.kafka.PaymentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FamilyEventConsumer {

    private final FamilyEventTask familyEventTask;
    private final EventTypeCaster eventTypeCaster;

    private final Logger log = LoggerFactory.getLogger(FamilyEventConsumer.class);

    @KafkaListener(topics = {"family.invitation"} ,
            groupId = "family-event-group")
    public void consume(ConsumerRecord<String, KafkaEvent> record) {
        try {

            InviteEvent event = eventTypeCaster.cast(record.value(), InviteEvent.class);
            familyEventTask.processInvitation(event);

        } catch (Exception e) {
            log.error("Failed to process family invitation event: {}", e.getMessage(), e);
            // 필요한 경우 Dead Letter Queue로 메시지 전송 로직 추가
        }
    }

    @KafkaListener(topics = "family.cash.conversion",
            groupId = "family-event-group")
    public void conversion(ConsumerRecord<String, KafkaEvent> record) {
        try {

            CashConversionEvent event = eventTypeCaster.cast(record.value(), CashConversionEvent.class);
            familyEventTask.processConversion(event);
        } catch (PaymentProcessingException e) {
        }
    }


    @KafkaListener(topics = "family.payment.request",
            groupId = "family-event-group")
    public void request(ConsumerRecord<String, KafkaEvent> record) {
        try {
            PaymentEvent event = eventTypeCaster.cast(record.value(), PaymentEvent.class);
            familyEventTask.processRequestPayment(event);
        } catch (PaymentProcessingException e) {

        }
    }
}
