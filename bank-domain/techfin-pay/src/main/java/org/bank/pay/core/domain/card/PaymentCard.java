package org.bank.pay.core.domain.card;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.bank.pay.core.domain.cash.Cash;
import org.bank.pay.core.domain.owner.PayOwner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "owner_pay_card_tb")
@Entity
public class PaymentCard extends PayCard {


    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private PayOwner payOwner;



    public static PaymentCard of(String cardName, String cardNumber, String cVC, String passwordStartwith, LocalDate dateOfExpiry) {
        return PaymentCard.builder()
                .cardName(cardName)
                .cardNumber(cardNumber)
                .cvc(cVC)
                .passwordStartwith(passwordStartwith)
                .expireDate(dateOfExpiry.format(DateTimeFormatter.ofPattern("yyyy-MM")))
                .build();

    }

    public void create(PayOwner payOwner, CardType type) {
        this.cash = new Cash(payOwner);
        this.type = type;
        this.payOwner = payOwner;
    }

    public boolean match(UUID cardId) {
        return getCardId().equals(cardId);
    }


    public void alias(String newCardName) {
        super.alias(newCardName);
        this.cardName = newCardName;
    }

}
