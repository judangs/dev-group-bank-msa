package org.bank.pay.core.payment;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.core.cash.PaymentProcessingException;
import org.bank.core.dto.response.ResponseCodeV2;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.core.payment.Product;
import org.bank.pay.core.payment.product.VirtualCash;
import org.bank.pay.core.payment.product.Category.CategoryType;
import org.bank.pay.core.payment.client.PaymentClient;
import org.bank.pay.core.producer.product.PurchasedEventPublisher;
import org.bank.pay.global.exception.PaymentException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentClient paymentClient;
    private final PaymentCallbackProperties paymentCallback;

    private final PurchasedEventPublisher purchasedEventPublisher;

    public String request(AuthClaims user, Product product, CategoryType categoryType) {
        return switch (categoryType) {
            case CASHABLE, CHARGE -> paymentClient.payment(user, product, categoryType, paymentCallback);
            case GENERAL, DIGITAL_CONTENT, SUPPORT, DELIVERY, ETC -> throw new PaymentException("현재 지원하지 않는 상품 타입입니다.");
        };
    }

    public String cache(AuthClaims user, UUID cardId, Money amount) {
        return request(user, new VirtualCash(cardId, CategoryType.CHARGE.getDescription(), amount, 1), CategoryType.CASHABLE);
    }

    public ResponseDtoV2 process(AuthClaims user, String paymentId) {
        try {
            ResponseCodeV2 code = paymentClient.apply(paymentId);

            if(code.equals(ResponseCodeV2.SUCCESS)) {

                PaymentDetail detail = paymentClient.history(paymentId);
                if(detail.type(CategoryType.CHARGE)) {
                    purchasedEventPublisher.charged(user, detail, CategoryType.CHARGE);
                    return ResponseDtoV2.success("결제를 진행하고 온라인 캐시를 충전합니다.");
                }

                purchasedEventPublisher.purchased(user, detail);
                return ResponseDtoV2.success("결제를 진행하고 상품 구입을 완료합니다.");

            }

            return ResponseDtoV2.fail("결제에 실패했습니다. 다시 시도해주세요.");

        } catch (PaymentProcessingException | PaymentException e) {
            return ResponseDtoV2.fail(e.getMessage());
        }
    }
}
