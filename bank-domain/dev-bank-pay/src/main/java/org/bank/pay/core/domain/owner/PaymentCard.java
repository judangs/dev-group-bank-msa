package org.bank.pay.core.domain.owner;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.bank.pay.core.domain.cash.Cash;
import org.bank.pay.global.domain.DomainEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pay_payment_card_tb")
@Entity
public class PaymentCard extends DomainEntity {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private UUID cardId;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private PayOwner payOwner;

    @EqualsAndHashCode.Include
    private String cardNumber;
    private String cvc;
    private String passwordStartwith;
    private String expireDate;
    private String cardName;

    @OneToOne(cascade = CascadeType.ALL)
    private Cash cash;


    public static PaymentCard of(String cardName, String cardNumber, String cVC, String passwordStartwith, LocalDate dateOfExpiry) {
        return PaymentCard.builder()
                .cardName(cardName)
                .cardNumber(cardNumber)
                .cvc(cVC)
                .passwordStartwith(passwordStartwith)
                .expireDate(dateOfExpiry.format(DateTimeFormatter.ofPattern("yyyy-MM")))
                .build();

    }

    public void create(PayOwner payOwner) {
        this.cash = new Cash(payOwner);
        this.payOwner = payOwner;
    }

    public boolean match(UUID cardId) {
        return getCardId().equals(cardId);
    }


    public void alias(String newCardName) {
        this.cardName = newCardName;
    }

}
