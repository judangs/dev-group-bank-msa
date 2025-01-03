package org.bank.pay.dto.response;

import lombok.Data;

@Data
public class NaverPaymentResponse {
	private String paymentId;
	private String payHistId;
	private String merchantName;
	private String productName;
	private String tradeConfirmYmdt;
	private int totalPayAmount;
}

