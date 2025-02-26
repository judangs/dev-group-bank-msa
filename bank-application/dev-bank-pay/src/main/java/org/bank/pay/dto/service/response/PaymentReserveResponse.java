package org.bank.pay.dto.service.response;

import lombok.Data;
import org.bank.core.dto.response.ResponseDtoV2;

@Data
public class PaymentReserveResponse extends ResponseDtoV2 {

    private String reserve;

    public PaymentReserveResponse(String reserve) {
        super();
        this.reserve = reserve;
    }
}
