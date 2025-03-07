package org.bank.pay.dto.service.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class FamilyEventRequest {

    private UUID familyId;
    private UUID paymentId;
    private Decision decision;

    public enum Decision {
        ACCEPT, REJECT
    }
}
