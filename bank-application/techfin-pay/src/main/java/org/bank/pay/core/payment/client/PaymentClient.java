package org.bank.pay.core.payment.client;

import org.bank.core.auth.AuthClaims;
import org.bank.core.dto.response.ResponseCodeV2;
import org.bank.core.payment.Product;
import org.bank.pay.core.event.product.Category.CategoryType;
import org.bank.pay.core.payment.PaymentCallbackProperties;
import org.bank.pay.core.payment.PaymentDetail;

public interface PaymentClient {

    String payment(AuthClaims user, Product product, CategoryType categoryType, PaymentCallbackProperties payment);
    ResponseCodeV2 apply(String paymentId);
    PaymentDetail history(String paymentId);
}
