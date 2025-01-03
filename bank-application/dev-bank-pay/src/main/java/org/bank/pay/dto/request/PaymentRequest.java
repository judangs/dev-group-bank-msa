package org.bank.pay.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PaymentRequest{
	private String cardNumber;
	private String cVC;
	private String passstartWith;
	private LocalDate dateOfExpiry;
	private String cardName;
}
