package org.bank.pay.core.cash;

import org.bank.core.cash.Money;
import org.bank.core.cash.PayMethod;
import org.bank.core.dto.pay.ChargeResponse;
import org.bank.core.dto.response.ResponseCode;
import org.bank.core.dto.response.ResponseMessage;
import org.bank.pay.client.CashClient;
import org.bank.pay.core.domain.onwer.PaymentCard;
import org.springframework.stereotype.Component;

@Component(value = "mockCashClient")
public class MockCashClient implements CashClient {

    @Override
    public ChargeResponse processPayment(String paymentId) {
        return new ChargeResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, "mock-paymeny-id", "Mock 상품 결제", PayMethod.CARD, 10000);
    }

    @Override
    public ChargeResponse processPayment(PaymentCard card, Money amount) {
        return new ChargeResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, "mock-paymeny-id", "Mock 상품 결제", PayMethod.CARD, 10000);
    }
}
