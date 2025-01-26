package org.bank.pay.core.familly.kafka.event;

import org.bank.core.payment.Product;
import org.bank.pay.core.familly.MemberClaims;

import java.util.UUID;

public class RequestPaymentEvent extends FamilyEvent {

    private UUID familyId;
    private MemberClaims from;
    private Product product;


    public RequestPaymentEvent(UUID familyId, MemberClaims from, Product product) {
        super(familyId);
        this.from = from;
        this.product = product;
    }
}
