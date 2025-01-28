package org.bank.pay.core.onwer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.pay.global.domain.DomainEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pay_payment_card_tb")
@Entity
public class PaymentCard extends DomainEntity {

    @Id
    @GeneratedValue
    private UUID cardId;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private PayOwner payOwner;

    private String cardNumber;
    private String cvc;
    private String passwordStartwith;
    private String expireDate;
    private String cardName;


    public static PaymentCard of(String cardName, String cardNumber, String cVC, String passwordStartwith, LocalDate dateOfExpiry) {
        return PaymentCard.builder()
                .cardName(cardName)
                .cardNumber(cardNumber)
                .cvc(cVC)
                .passwordStartwith(passwordStartwith)
                .expireDate(dateOfExpiry.format(DateTimeFormatter.ofPattern("yyyy-MM")))
                .build();

    }

    public void setPayOwner(PayOwner payOwner) {
        this.payOwner = payOwner;
    }



    public void updateCardAlias(String newCardName) {
        this.cardName = newCardName;
    }

    private String maskCard() {
        return cardNumber.substring(cardNumber.length() - 4);
    }
}
