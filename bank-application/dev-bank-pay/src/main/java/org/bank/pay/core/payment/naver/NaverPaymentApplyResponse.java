package org.bank.pay.core.payment.naver;

import lombok.Data;
import org.bank.pay.core.payment.PaymentDetail;

@Data
class NaverPaymentApplyResponse {
	private String code;
	private String message;
	private Body body;

	@Data
	public class Body{
		private String paymentId;
		private PaymentDetail detail;
	}
}