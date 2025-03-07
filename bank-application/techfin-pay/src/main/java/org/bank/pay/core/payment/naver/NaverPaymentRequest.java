package org.bank.pay.core.payment.naver;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.bank.core.auth.AuthClaims;
import org.bank.core.payment.Product;
import org.bank.pay.core.event.product.Category.CategoryType;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Data
@SuperBuilder
class NaverPaymentRequest {
	private String merchantPayKey; // 가맹점 주문 내역 확인 가능한 가맹점 결제 번호 또는 주문번호
	private int totalPayAmount; // 총 결제 금액. 최소 결제금액은 10원
	private int productCount; // 상품 수량 예: A 상품 2개 + B 상품 1개의 경우 productCount 3으로 전달
	private String productName; //대표 상품명. 예: 장미의 이름 외 1건(X), 장미의 이름(O)
	private String purchaserName; // 구매자 성명. 결제 상품이 보험 및 위험 업종 등인 경우에만 필수 값입니다. 그 외에는 전달할 필요가 없습니다
	private List<ProductItems> productItems;
	private int taxExScopeAmount; //면세 대상 금액. 과세 대상 금액 + 면세 대상 금액 + 컵 보증금 금액 (옵션) = 총 결제 금액
	private int taxScopeAmount; // 과세 대상 금액. 과세 대상 금액 + 면세 대상 금액 + 컵 보증금 금액 (옵션) = 총 결제 금액

	/*
		결제 완료 후 이동할 URL(returnUrl + 가맹점 파라미터 전달이 가능합니다)
		네이버페이는 결제 작업 완료 후, 가맹점이 등록한 returnUrl로 리디렉션을 수행합니다
		가맹점은 이를 활용하여 내부 처리를 수행하거나 구매자에게 결제 결과 화면을 노출할 수 있습니다
	 */
	private String returnUrl;


	public static NaverPaymentRequest of(AuthClaims user, Product product, CategoryType categoryType, String url) {

		String purchaseId = UUID.randomUUID().toString();

		return NaverPaymentRequest.builder()
				.merchantPayKey(purchaseId)
				.productName(product.getName())
				.purchaserName(user.getUsername())
				.productItems(Collections.singletonList(ProductItems.of(product, categoryType)))
				.totalPayAmount(product.totalPrice())
				.productCount(product.getQuantity())
				.taxExScopeAmount(product.totalPrice())
				.taxScopeAmount(0)
				.returnUrl(url + purchaseId)
				.build();
	}

}