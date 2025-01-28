package org.bank.consumer.pay;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.bank.core.kafka.KafkaEvent;
import org.bank.pay.core.familly.event.kafka.CashConversionEvent;
import org.bank.pay.core.familly.event.kafka.InviteEvent;
import org.bank.pay.core.familly.event.kafka.RequestPaymentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FamilyEventConsumer {

    private final FamilyEventTask familyEventTask;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Logger log = LoggerFactory.getLogger(FamilyEventConsumer.class);

    @KafkaListener(topics = "family.invitation",
            groupId = "invitation-group")
    public void invitation(ConsumerRecord<String, KafkaEvent> record) {
        try {
            InviteEvent event = objectMapper.convertValue(record.value(), InviteEvent.class);
            familyEventTask.processInvitation(event);

        } catch (Exception e) {
            log.error("Failed to process family invitation event: {}", e.getMessage(), e);
            // 필요한 경우 Dead Letter Queue로 메시지 전송 로직 추가
        }
    }

    @KafkaListener(topics = "family.cash.conversion",
            groupId = "conversion-group")
    public void conversion(ConsumerRecord<String, KafkaEvent> record) {
        try {
            CashConversionEvent event = objectMapper.convertValue(record.value(), CashConversionEvent.class);
            familyEventTask.processConversion(event);
        } catch (Exception e) {
            log.error("Failed to process family cash event: {}", e.getMessage(), e);
        }
    }


    @KafkaListener(topics = "family.payment",
            groupId = "request-payment-group")
    public void requestPayment(ConsumerRecord<String, KafkaEvent> record) {
        try {
            RequestPaymentEvent event = objectMapper.convertValue(record.value(), RequestPaymentEvent.class);
            familyEventTask.processRequestPayment(event);

        } catch (Exception e) {

        }
    }
}
