package org.bank.pay.dto.service.request;

import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CashLimitRequest{

	@DecimalMin(value = "0.00", inclusive = false, message = "결제 한도는 음수일 수 없습니다.")
	private BigDecimal daily;

	@DecimalMin(value = "0.00", inclusive = false, message = "결제 한도는 음수일 수 없습니다.")
	private BigDecimal each;
}
