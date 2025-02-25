package org.bank.pay.dto.service.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CashLimitRequest{
	private BigDecimal daily;
	private BigDecimal each;
}
