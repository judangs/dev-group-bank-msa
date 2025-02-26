package org.bank.pay.core.payment.naver;

import lombok.Builder;
import lombok.Data;
import org.bank.core.payment.Product;
import org.bank.pay.core.payment.product.Category;
import org.bank.pay.core.payment.product.Category.CategoryType;

@Data
@Builder
public class ProductItems {
	private String categoryType; // 결제 상품 유형. 정의는 별도로 제공되는
	private String categoryId;
	private String uid;
	private String payReferrer; // 결제 상품 유형
	private String name; // 상품 명
	private int count; // 결제 상품 개수

	public static ProductItems of(Product product, CategoryType categoryType) {
		return ProductItems.builder()
				.categoryType(Category.category(categoryType))
				.categoryId(categoryType.getCategoryId())
				.uid(categoryType.getUid())
				.name(product.getName())
				.count(product.getQuantity())
				.build();
	}
}