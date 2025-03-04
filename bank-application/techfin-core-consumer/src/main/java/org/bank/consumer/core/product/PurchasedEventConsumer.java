package org.bank.consumer.core.product;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.bank.consumer.global.kafka.EventTypeCaster;
import org.bank.core.cash.PaymentProcessingException;
import org.bank.core.kafka.KafkaEvent;
import org.bank.pay.core.event.product.PurchasedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PurchasedEventConsumer {

    private final PurchasedEventTask purchasedEventTask;

    private final EventTypeCaster eventTypeCaster;

    @KafkaListener(topics = "pay.cachable.charged",
            groupId = "purchased-event-group")
    public void charged(ConsumerRecord<String, KafkaEvent> record) {
        try {

            PurchasedEvent event = eventTypeCaster.cast(record.value(), PurchasedEvent.class);
            purchasedEventTask.processCharge(event);

        } catch (PaymentProcessingException e) {

        }
    }
}
