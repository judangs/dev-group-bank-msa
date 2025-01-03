package org.bank.pay.client;

import org.bank.pay.core.onwer.PaymentCard;

public interface PaymentCardClient {

    boolean validateCard(PaymentCard paymentCard);
}

