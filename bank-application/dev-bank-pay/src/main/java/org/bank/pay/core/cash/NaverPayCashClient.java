package org.bank.pay.core.cash;

import org.bank.core.cash.Money;
import org.bank.core.cash.PayMethod;
import org.bank.core.dto.pay.ChargeResponse;
import org.bank.core.dto.response.ResponseCode;
import org.bank.core.dto.response.ResponseMessage;
import org.bank.pay.client.CashClient;
import org.bank.pay.core.onwer.PaymentCard;
import org.bank.pay.dto.response.NaverPaymentResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class NaverPayCashClient implements CashClient {

    @Override
    public ChargeResponse processPayment(String paymentId) {
        String url = "https://dev-pub.apis.naver.com/naverpay-partner/naverpay/payments/v2.2/apply/payment";

        String clientId = "HN3GGCMDdTgGUfl0kFCo";
        String clientSecret = "ftZjkkRNMR";
        String chainId = "SFlCMUNON05kbE5";

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);
        headers.set("X-NaverPay-Chain-Id", chainId);

        String body = "paymentId=" + paymentId;
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<NaverPaymentResponse> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, NaverPaymentResponse.class);

        if(!response.getStatusCode().equals("Success")) {
            return (ChargeResponse) ChargeResponse.fail("결제를 다시 요청해주세요..");
        }


        NaverPaymentResponse paymentResponse = response.getBody();
        return new ChargeResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS,
                paymentResponse.getPaymentId(), paymentResponse.getProductName(), PayMethod.CARD, paymentResponse.getTotalPayAmount());
    }

    @Override
    public ChargeResponse processPayment(PaymentCard card, Money amount) {
        return null;
    }
}
