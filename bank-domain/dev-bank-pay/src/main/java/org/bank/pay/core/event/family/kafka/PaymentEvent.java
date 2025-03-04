package org.bank.pay.core.event.family.kafka;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.payment.Product;
import org.bank.pay.core.domain.familly.MemberClaims;

import java.util.List;
import java.util.UUID;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@JsonTypeName(value = "PAYMENT_REQUEST")
public class PaymentEvent extends FamilyEvent {

    private AuthClaims from;
    private List<Product> products;


    public PaymentEvent(UUID familyId, MemberClaims from, List<Product> products) {
        super(PaymentEvent.class, familyId);
        this.from = from;
        this.products = products;
    }
}
