package org.bank.pay.core.onwer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.pay.core.cash.Cash;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "owner")
public class PayOwner {

    @Id
    @GeneratedValue
    private UUID ownerId;

    @Embedded
    private OwnerClaims claims;

    @OneToMany(mappedBy = "payOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentCard> paymentCards = new ArrayList<>();

    @OneToOne(mappedBy = "payOwner")
    private Cash cash;

    public PayOwner(OwnerClaims claims) {
        this.claims = claims;
        this.cash = new Cash();
    }

    public void addPaymentCard(PaymentCard paymentCard) {
        paymentCards.add(paymentCard);
    }

    public void updateCardAlias(UUID cardId, String newAlias) {
        PaymentCard paymentCard = paymentCards.stream().filter(card -> card.getCardId().equals(cardId))
                .findFirst().orElseThrow(IllegalArgumentException::new);

        paymentCard.updateCardAlias(newAlias);
    }

    public void removeRegisteredCard(UUID cardId) {
        paymentCards.removeIf(card -> card.getCardId().equals(cardId));
    }
}
