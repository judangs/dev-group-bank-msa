package org.bank.pay.core.payment;

import lombok.Data;
import org.bank.pay.core.payment.product.Category.CategoryType;

@Data
public class PaymentDetail {
    private String paymentId; // 네이버 페이 결제 번호
    private String payHistId; // 네이버페이 결제 이력 번호
    private String admissionState; // 결제/취소 시도에 대한 최종 결과
    private String primaryPayMeans; // 주 결제 수단
    private String admissionTypeCode; // 결제 승인 유형
    private String cardAuthNo; // 카드 승인 번호
    private String bankAccountNo; // 일부 마스킹 된 계좌 번호
    private String cardNo; // 일부 마스킹된 신용카드 번호
    private String productName; // 상품명
    private int totalPayAmount; // 총 결제/금액
    private String admissionYmdt; // 결제 / 취소 일시
    private String tradeConfirmYmdt; // 거래 완료 일시

    public boolean type(CategoryType categoryType) {
        return getProductName().equals(categoryType.getDescription());
    }
}
