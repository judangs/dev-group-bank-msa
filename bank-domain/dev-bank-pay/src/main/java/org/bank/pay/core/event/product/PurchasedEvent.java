package org.bank.pay.core.event.product;

import lombok.Getter;
import org.bank.core.auth.AuthClaims;
import org.bank.core.kafka.KafkaEvent;
import org.bank.core.payment.Product;

@Getter
public class PurchasedEvent extends KafkaEvent {

    private final String paymentId;
    private final String payHistId;
    private final AuthClaims user;
    private final Product product;
    private final String admissionState; // 결제/취소 시도에 대한 최종 결과
    private final String primaryPayMeans; // 주 결제 수단
    private final int totalPayAmount; // 총 결제/금액


    public PurchasedEvent(String paymentId, String payHistId, AuthClaims user, String admissionState, String primaryPayMeans, String productName, int totalPayAmount) {
        super();
        this.paymentId = paymentId;
        this.payHistId = payHistId;
        this.user = user;
        this.product = new Product(productName, totalPayAmount, 1);
        this.admissionState = admissionState;
        this.primaryPayMeans = primaryPayMeans;
        this.totalPayAmount = totalPayAmount;
    }
}
