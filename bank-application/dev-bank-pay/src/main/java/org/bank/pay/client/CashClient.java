package org.bank.pay.client;


import org.bank.core.cash.Money;
import org.bank.core.dto.pay.ChargeResponse;
import org.bank.pay.core.onwer.PaymentCard;

public interface CashClient {
    ChargeResponse processPayment(String paymentId);
    ChargeResponse processPayment(PaymentCard card, Money amount);
}
