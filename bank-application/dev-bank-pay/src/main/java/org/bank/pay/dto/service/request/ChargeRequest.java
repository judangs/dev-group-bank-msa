package org.bank.pay.dto.service.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ChargeRequest {

    private UUID paymentCardId;
    private BigDecimal chargeAmount;
}
