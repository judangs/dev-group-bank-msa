package org.bank.pay.dto.service.request;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateCardAliasRequest {

    private UUID cardId;
    private String cardName;
}
