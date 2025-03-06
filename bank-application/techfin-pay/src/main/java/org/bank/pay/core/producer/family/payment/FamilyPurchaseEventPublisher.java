package org.bank.pay.core.producer.family.payment;

import lombok.RequiredArgsConstructor;
import org.bank.core.cash.Money;
import org.bank.core.kafka.KafkaEvent;
import org.bank.core.payment.Product;
import org.bank.pay.core.domain.familly.Family;
import org.bank.pay.core.domain.familly.MemberClaims;
import org.bank.pay.core.event.family.FamilyPayment;
import org.bank.pay.core.event.family.kafka.CashConversionEvent;
import org.bank.pay.core.event.family.kafka.PaymentEvent;
import org.bank.pay.global.domain.card.PayCard;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class FamilyPurchaseEventPublisher {


    private final String PAYMENT_REQUEST_TOPIC = "family.payment.request";
    private final String PAYMENT_APPROVAL_TOPIC = "family.payment.approval";

    private final String CASH_CONVERSION_TOPIC = "family.cash.conversion";

    private final KafkaTemplate<String, KafkaEvent> kafkaTemplate;

    @Async
    public void request(Family family, MemberClaims from, Product product) {
        kafkaTemplate.send(PAYMENT_REQUEST_TOPIC, new PaymentEvent(family.getFamilyId(), from, Arrays.asList(product)));
    }

    @Async
    public void approval(FamilyPayment payment) {
        kafkaTemplate.send(PAYMENT_APPROVAL_TOPIC, FamilyPayment.to(payment));
    }


    @Async
    public void conversion(Family family, MemberClaims from, PayCard card, Money amount) {
        kafkaTemplate.send(CASH_CONVERSION_TOPIC, new CashConversionEvent(family.getFamilyId(), card.getCardId(), from, amount));
    }


}
