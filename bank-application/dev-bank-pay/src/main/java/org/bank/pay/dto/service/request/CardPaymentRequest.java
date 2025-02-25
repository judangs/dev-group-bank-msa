package org.bank.pay.dto.service.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CardPaymentRequest {
	private String cardNumber;
	private String cVC;
	private String passstartWith;
	private LocalDate dateOfExpiry;
	private String cardName;
}
