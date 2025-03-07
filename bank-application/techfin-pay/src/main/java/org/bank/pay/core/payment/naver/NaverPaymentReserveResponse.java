package org.bank.pay.core.payment.naver;

import lombok.Data;
import lombok.Getter;


@Data
class NaverPaymentReserveResponse {

	private String code;
	private String message;
	private Body body;

	@Getter
	public class Body{
		private String reserveId;
	}

	public String reserve() {
		return getBody().getReserveId();
	}


}