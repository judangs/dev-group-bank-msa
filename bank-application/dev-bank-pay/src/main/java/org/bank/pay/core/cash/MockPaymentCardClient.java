package org.bank.pay.core.cash;

import org.bank.pay.client.PaymentCardClient;
import org.bank.pay.core.domain.onwer.PaymentCard;
import org.springframework.stereotype.Component;

@Component
public class MockPaymentCardClient implements PaymentCardClient {
    @Override
    public boolean validateCard(PaymentCard paymentCard) {
        return true;
    }
}
