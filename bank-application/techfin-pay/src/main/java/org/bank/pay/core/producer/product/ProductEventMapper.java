package org.bank.pay.core.producer.product;

import org.bank.core.auth.AuthClaims;
import org.bank.core.payment.Product;
import org.bank.pay.core.event.family.PaymentProduct;
import org.bank.pay.core.event.product.PurchasedEvent;
import org.bank.pay.core.event.product.VirtualCash;
import org.bank.pay.core.payment.CashChargeDetail;
import org.bank.pay.core.payment.PaymentDetail;
import org.springframework.stereotype.Component;

@Component
public class ProductEventMapper {

    public PurchasedEvent mapTo(AuthClaims user, PaymentDetail paymentDetail, Product product) {
        return new PurchasedEvent(
                paymentDetail.getPaymentId(),
                paymentDetail.getPayHistId(),
                user,
                paymentDetail.getAdmissionState(),
                paymentDetail.getPrimaryPayMeans(),
                product,
                paymentDetail.getTotalPayAmount()
        );
    }

    public PurchasedEvent mapTo(AuthClaims user, PaymentDetail paymentDetail) {

        Product product = new PaymentProduct(paymentDetail.getProductName(), paymentDetail.getTotalPayAmount(), 1);

        if(paymentDetail instanceof CashChargeDetail cashChargeDetail) {
            return new PurchasedEvent(
                    paymentDetail.getPaymentId(),
                    paymentDetail.getPayHistId(),
                    user,
                    paymentDetail.getAdmissionState(),
                    paymentDetail.getPrimaryPayMeans(),
                    VirtualCash.of(cashChargeDetail.getCardId(), product),
                    paymentDetail.getTotalPayAmount()
            );
        }

        return new PurchasedEvent(
                paymentDetail.getPaymentId(),
                paymentDetail.getPayHistId(),
                user,
                paymentDetail.getAdmissionState(),
                paymentDetail.getPrimaryPayMeans(),
                product,
                paymentDetail.getTotalPayAmount()
        );
    }

}
