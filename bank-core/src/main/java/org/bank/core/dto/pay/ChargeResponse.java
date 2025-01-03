package org.bank.core.dto.pay;

import lombok.Data;
import org.bank.core.cash.PayMethod;
import org.bank.core.dto.response.ResponseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class ChargeResponse extends ResponseDto {

    private String paymentId;
    private String productName;
    private LocalDateTime completedAt;
    private PayMethod method;
    private BigDecimal totalpayAmount;

    public ChargeResponse(String code, String message, String paymentId, String productName, PayMethod method, int totalpayAmount) {
        super(code, message, LocalDateTime.now());
        this.paymentId = paymentId;
        this.productName = productName;
        this.method = method;
        this.totalpayAmount = new BigDecimal(totalpayAmount);
    }
}
