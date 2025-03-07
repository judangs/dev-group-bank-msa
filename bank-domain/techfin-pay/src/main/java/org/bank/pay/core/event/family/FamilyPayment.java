package org.bank.pay.core.event.family;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.bank.core.payment.Product;
import org.bank.pay.core.domain.familly.MemberClaims;
import org.bank.pay.core.event.family.kafka.PaymentEvent;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Getter
@SuperBuilder
@Table(name = "pay_family_payment_tb")
@DiscriminatorValue("payment")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class FamilyPayment extends FamilyEventEntity {

    @Embedded
    private MemberClaims from;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "pay_family_payment_products_tb", joinColumns = @JoinColumn(name = "request-payment-id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"request-payment-id", "name"})
    )
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "name", columnDefinition = "TEXT")),
            @AttributeOverride(name = "price.balance", column = @Column(name = "price", precision = 30, scale = 2)),
            @AttributeOverride(name = "quantity", column = @Column(name = "quantity"))
    })
    @Cascade(CascadeType.ALL)
    private List<PaymentProduct> products;

    private LocalDateTime requestDate;

    public boolean isPending() {
        return status.equals(FamilyEventStatus.PENDING);
    }

    public static FamilyPayment of(PaymentEvent event) {

        LocalDateTime requestDate = Instant.ofEpochMilli(event.getTimestamp())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        List<Product> products = event.getProducts();
        List<PaymentProduct> paymentProducts = products.stream().map(PaymentProduct::of).toList();

        return FamilyPayment.builder()
                .familyId(event.getFamilyId())
                .from(MemberClaims.of(event.getFrom()))
                .products(paymentProducts)
                .status(FamilyEventStatus.PENDING)
                .requestDate(requestDate)
                .build();
    }

    public static PaymentEvent to(FamilyPayment familyPayment) {
        List<Product> products = familyPayment.getProducts().stream()
                .map(PaymentProduct::to).toList();

        return new PaymentEvent(familyPayment.getFamilyId(), familyPayment.getFrom(), products);
    }



}
