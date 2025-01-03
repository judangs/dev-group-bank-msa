package org.bank.pay.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CashLimitRequest{
	private BigDecimal daily;
	private BigDecimal each;
}
