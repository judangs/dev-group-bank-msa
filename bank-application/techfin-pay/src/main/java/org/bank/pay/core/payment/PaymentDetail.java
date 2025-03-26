package org.bank.pay.core.payment;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bank.pay.core.event.product.Category.CategoryType;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class PaymentDetail {
    protected String paymentId; // 네이버 페이 결제 번호
    protected String payHistId; // 네이버페이 결제 이력 번호
    protected String admissionState; // 결제/취소 시도에 대한 최종 결과
    protected String primaryPayMeans; // 주 결제 수단
    protected String admissionTypeCode; // 결제 승인 유형
    protected String cardAuthNo; // 카드 승인 번호
    protected String bankAccountNo; // 일부 마스킹 된 계좌 번호
    protected String cardNo; // 일부 마스킹된 신용카드 번호
    protected String productName; // 상품명
    protected int totalPayAmount; // 총 결제/금액
    protected String admissionYmdt; // 결제 / 취소 일시
    protected String tradeConfirmYmdt; // 거래 완료 일시

    protected PaymentDetail(PaymentDetail detail) {
        this.paymentId = detail.paymentId;
        this.payHistId = detail.payHistId;
        this.admissionState = detail.admissionState;
        this.primaryPayMeans = detail.primaryPayMeans;
        this.admissionTypeCode = detail.admissionTypeCode;
        this.cardAuthNo = detail.cardAuthNo;
        this.bankAccountNo = detail.bankAccountNo;
        this.cardNo = detail.cardNo;
        this.productName = detail.productName;
        this.totalPayAmount = detail.totalPayAmount;
        this.admissionYmdt = detail.admissionYmdt;
        this.tradeConfirmYmdt = detail.tradeConfirmYmdt;
    }


    public boolean type(CategoryType categoryType) {
        return getProductName().equals(categoryType.getDescription());
    }
}
