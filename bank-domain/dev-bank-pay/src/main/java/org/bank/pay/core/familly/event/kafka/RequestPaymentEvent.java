package org.bank.pay.core.familly.event.kafka;

import lombok.Getter;
import org.bank.core.payment.Product;
import org.bank.pay.core.familly.MemberClaims;

import java.util.List;
import java.util.UUID;

@Getter
public class RequestPaymentEvent extends FamilyEvent {

    private MemberClaims from;
    private List<Product> products;


    public RequestPaymentEvent(UUID familyId, MemberClaims from, List<Product> products) {
        super(familyId);
        this.from = from;
        this.products = products;
    }
}
