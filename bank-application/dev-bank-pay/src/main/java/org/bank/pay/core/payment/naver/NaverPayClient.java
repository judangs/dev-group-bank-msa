package org.bank.pay.core.payment.naver;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.PaymentProcessingException;
import org.bank.core.dto.response.ResponseCodeV2;
import org.bank.core.payment.Product;
import org.bank.pay.core.payment.product.Category.CategoryType;
import org.bank.pay.core.payment.PaymentCallbackProperties;
import org.bank.pay.core.payment.PaymentDetail;
import org.bank.pay.core.payment.client.PaymentClient;
import org.bank.pay.global.exception.PaymentException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class NaverPayClient implements PaymentClient {

    private final RestClient naverPayRestClient;
    private final NaverPayClientProperties naverPayClientProperties;

    @Override
    public String payment(AuthClaims user, Product product, CategoryType categoryType, PaymentCallbackProperties payment) {

        NaverPaymentRequest request = NaverPaymentRequest.of(user, product, categoryType, payment.callback());

        ResponseEntity<NaverPaymentReserveResponse> response = naverPayRestClient.post()
                .accept(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toEntity(NaverPaymentReserveResponse.class);

        if(!response.getBody().getCode().equals("Success")) {
            throw new PaymentProcessingException(response.getBody().getMessage());
        }

        return naverPayClientProperties.window(response.getBody().reserve());
    }

    @Override
    public ResponseCodeV2 apply(String paymentId) {

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("paymentId", paymentId);

        try {

            ResponseEntity<NaverPaymentApplyResponse> response = naverPayRestClient.post()
                    .uri(naverPayClientProperties.apply())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(data)
                    .retrieve()
                    .toEntity(NaverPaymentApplyResponse.class);


            if (response.getBody().getCode().equals("Success")) {
                return ResponseCodeV2.SUCCESS;
            }

            throw new PaymentException(response.getBody().getMessage());

        } catch (PaymentException e) {
            throw new PaymentProcessingException(e.getMessage());
        }
    }

    @Override
    public PaymentDetail history(String paymentId) {

        NaverPaymentHistoryResponse response = naverPayRestClient.post()
                .uri(naverPayClientProperties.history(paymentId))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .toEntity(NaverPaymentHistoryResponse.class).getBody();

        if (Objects.requireNonNull(response).getCode().equals("Success")) {
            return response.detail().orElseThrow(IllegalAccessError::new);
        }

        throw new IllegalArgumentException("결과를 조회하는데 문제가 발생했습니다.");
    }
}
