package org.bank.pay.core.event.family.kafka;

import lombok.Getter;
import org.bank.core.payment.Product;
import org.bank.pay.core.domain.familly.MemberClaims;

import java.util.List;
import java.util.UUID;

@Getter
public class PaymentEvent extends FamilyEvent {

    private MemberClaims from;
    private List<Product> products;


    public PaymentEvent(UUID familyId, MemberClaims from, List<Product> products) {
        super(familyId);
        this.from = from;
        this.products = products;
    }
}
