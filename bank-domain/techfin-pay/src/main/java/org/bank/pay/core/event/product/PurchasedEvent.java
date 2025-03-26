package org.bank.pay.core.event.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.kafka.KafkaEvent;
import org.bank.core.payment.Product;

@Getter
@NoArgsConstructor
public class PurchasedEvent extends KafkaEvent {

    private String paymentId;
    private String payHistId;
    private AuthClaims user;

    @JsonDeserialize(as = VirtualCash.class)
    private Product product;
    private String admissionState; // 결제/취소 시도에 대한 최종 결과
    private String primaryPayMeans; // 주 결제 수단
    private int totalPayAmount; // 총 결제/금액


    public PurchasedEvent(String paymentId, String payHistId, AuthClaims user, String admissionState, String primaryPayMeans, Product product, int totalPayAmount) {
        super(PurchasedEvent.class);
        this.paymentId = paymentId;
        this.payHistId = payHistId;
        this.user = user;
        this.product = product;
        this.admissionState = admissionState;
        this.primaryPayMeans = primaryPayMeans;
        this.totalPayAmount = totalPayAmount;
    }
}
