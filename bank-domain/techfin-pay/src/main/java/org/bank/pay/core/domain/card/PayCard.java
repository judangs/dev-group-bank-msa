package org.bank.pay.core.domain.card;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.bank.pay.core.domain.cash.Cash;
import org.bank.pay.global.domain.DomainEntity;

import java.util.UUID;

@Getter
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "pay_card_tb")
@AllArgsConstructor
@NoArgsConstructor
@Entity
public abstract class PayCard extends DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)", unique = true)
    @EqualsAndHashCode.Include
    protected UUID cardId;

    @EqualsAndHashCode.Include
    protected String cardNumber;
    protected String cvc;
    protected String passwordStartwith;
    protected String expireDate;
    protected String cardName;

    @Enumerated(EnumType.STRING)
    protected CardType type;

    @OneToOne(cascade = CascadeType.ALL)
    protected Cash cash;

    protected void create() {
        cash = new Cash();
    }

    public boolean match(UUID cardId) {
        return getCardId().equals(cardId);
    }

    public void alias(String newCardName) {
        this.cardName = newCardName;
    }

}
