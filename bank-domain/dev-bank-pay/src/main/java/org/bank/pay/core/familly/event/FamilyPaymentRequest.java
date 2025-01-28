package org.bank.pay.core.familly.event;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.core.payment.Product;
import org.bank.pay.core.familly.MemberClaims;
import org.bank.pay.core.familly.event.kafka.RequestPaymentEvent;
import org.bank.pay.global.domain.DomainEntity;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@Table(name = "pay_family_payment_tb")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class FamilyPaymentRequest extends DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "request-payment-id", columnDefinition = "BINARY(16)")
    private UUID id;

    private UUID familyId;

    @Embedded
    private MemberClaims from;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "pay_family_payment_products_tb", joinColumns = @JoinColumn(name = "request-payment-id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"request-payment-id", "name"})
    )
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "name", columnDefinition = "TEXT")),
            @AttributeOverride(name = "price.balance", column = @Column(name = "price", precision = 30, scale = 10)),
            @AttributeOverride(name = "quantity", column = @Column(name = "quantity"))
    })
    @Cascade(CascadeType.ALL)
    private List<PaymentProduct> products;

    @Enumerated(EnumType.STRING)
    private FamilyEventStatus status;
    private LocalDateTime requestDate;

    public static FamilyPaymentRequest of(RequestPaymentEvent event) {

        LocalDateTime requestDate = Instant.ofEpochMilli(event.getTimestamp())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        List<Product> products = event.getProducts();
        List<PaymentProduct> paymentProducts = products.stream().map(PaymentProduct::of).toList();

        return FamilyPaymentRequest.builder()
                .familyId(event.getFamilyId())
                .from(event.getFrom())
                .products(paymentProducts)
                .status(FamilyEventStatus.PENDING)
                .requestDate(requestDate)
                .build();
    }



}
