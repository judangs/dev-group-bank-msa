package org.bank.pay.dto.service.response;

import lombok.Data;
import org.bank.core.cash.Money;
import org.bank.core.dto.response.ResponseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CashBalanceResponse extends ResponseDto {

    BigDecimal balance;

    public CashBalanceResponse(String code, String message, Money credit) {
        super(code, message, LocalDateTime.now());
        this.balance = credit.getBalance();
    }
}
