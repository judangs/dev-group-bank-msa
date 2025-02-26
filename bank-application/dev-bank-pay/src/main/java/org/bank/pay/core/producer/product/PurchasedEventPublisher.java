package org.bank.pay.core.producer.product;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.kafka.KafkaEvent;
import org.bank.pay.core.event.product.PurchasedEvent;
import org.bank.pay.core.payment.product.Category.CategoryType;
import org.bank.pay.core.payment.PaymentDetail;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PurchasedEventPublisher {

    private final String CASH_CHARGED_TOPIC = "pay.cachable.charged";
    private final String PRODUCT_PURCHASED_TOPIC = "pay.product.purchased";

    private final KafkaTemplate<String, KafkaEvent> kafkaTemplate;
    private final ProductEventMapper productEventMapper;

    @Async
    public void charged(AuthClaims user, PaymentDetail detail, CategoryType type) {

        PurchasedEvent event = productEventMapper.mapTo(user, detail);

        if(type.equals(CategoryType.CASHABLE)) {
            kafkaTemplate.send(CASH_CHARGED_TOPIC, event);
        }
    }

    @Async
    public void purchased(AuthClaims user, PaymentDetail detail) {
        PurchasedEvent event = productEventMapper.mapTo(user, detail);
        kafkaTemplate.send(PRODUCT_PURCHASED_TOPIC, event);
    }



}
