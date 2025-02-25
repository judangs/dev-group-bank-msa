package org.bank.pay.dto.service.response;

import lombok.Data;
import org.bank.core.cash.Money;
import org.bank.core.dto.response.ResponseDtoV2;

import java.math.BigDecimal;

@Data
public class CashBalanceResponse extends ResponseDtoV2 {

    BigDecimal balance;

    public CashBalanceResponse(Money credit) {
        super();
        this.balance = credit.getBalance();
    }
}
