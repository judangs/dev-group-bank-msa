package org.bank.pay.core.domain.owner;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.pay.global.domain.DomainEntity;
import org.bank.pay.global.domain.card.CardType;
import org.bank.pay.global.domain.card.PayCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pay_owner_tb")
@Entity
public class PayOwner extends DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "owner_id", columnDefinition = "BINARY(16)", unique = true)
    private UUID ownerId;

    @Embedded
    private OwnerClaims claims;

    @OneToMany(mappedBy = "payOwner", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentCard> paymentCards = new ArrayList<>();

    public PayOwner(AuthClaims user) {
        this.claims = OwnerClaims.of(user);
    }

    public Optional<PaymentCard> match(PayCard card) {
        return paymentCards.stream().filter(paymentCard -> paymentCard.equals(card))
                .findFirst();
    }

    public Optional<PaymentCard> match(UUID cardId) {
        return paymentCards.stream().filter(card -> card.match(cardId))
                .findFirst();
    }

    public void addPaymentCard(PaymentCard card) {
        getPaymentCards().add(card);
        card.create(this, CardType.PERSONAL);
    }

    public void close(UUID cardId) {
        match(cardId).ifPresent(card -> paymentCards.remove(card));
    }
}
