package org.bank.pay.dto.service.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ReservedChargeRequest {

	private UUID paymentCardId;
	private BigDecimal chargeAmount;

	private LocalDateTime date;
	private BigDecimal balance;
}
