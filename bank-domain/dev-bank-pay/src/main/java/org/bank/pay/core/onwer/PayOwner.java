package org.bank.pay.core.onwer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.pay.core.cash.Cash;
import org.bank.pay.global.domain.DomainEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "owner")
public class PayOwner extends DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)", unique = true)
    private UUID ownerId;

    @Embedded
    private OwnerClaims claims;

    @OneToMany(mappedBy = "payOwner", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentCard> paymentCards = new ArrayList<>();

    @OneToOne(mappedBy = "payOwner")
    private Cash cash;

    public PayOwner(OwnerClaims claims) {
        this.claims = claims;
        this.cash = new Cash(this);
    }

    public void setCash(Cash cash) {
        this.cash = cash;
    }

    public void addPaymentCard(PaymentCard paymentCard) {
        paymentCards.add(paymentCard);
        paymentCard.setPayOwner(this);
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
