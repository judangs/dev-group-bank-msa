package org.bank.pay.core.producer.product;

import org.bank.core.auth.AuthClaims;
import org.bank.pay.core.event.product.PurchasedEvent;
import org.bank.pay.core.payment.PaymentDetail;
import org.springframework.stereotype.Component;

@Component
public class ProductEventMapper {


    public PurchasedEvent mapTo(AuthClaims user, PaymentDetail paymentDetail) {
        return new PurchasedEvent(
                paymentDetail.getPaymentId(),
                paymentDetail.getPayHistId(),
                user,
                paymentDetail.getAdmissionState(),
                paymentDetail.getPrimaryPayMeans(),
                paymentDetail.getProductName(),
                paymentDetail.getTotalPayAmount()
        );
    }

}
