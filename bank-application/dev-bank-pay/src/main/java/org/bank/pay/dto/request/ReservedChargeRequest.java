package org.bank.pay.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ReservedChargeRequest {

	private UUID paymentCardId;
	private BigDecimal chargeAmount;

	private LocalDateTime date;
	private BigDecimal balance;
}
