package org.bank.pay.core.payment.naver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.bank.pay.core.payment.PaymentDetail;

import java.util.List;
import java.util.Optional;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class NaverPaymentHistoryResponse {

	private String code;
	private String message;
	private Body body;

	@Data
	public class Body{
		private List<PaymentDetail> list;
	}

	public Optional<PaymentDetail> detail() {
		if(!getBody().getList().isEmpty()) {
			return Optional.ofNullable(getBody().getList().get(0));
		}

		return Optional.empty();
	}
}