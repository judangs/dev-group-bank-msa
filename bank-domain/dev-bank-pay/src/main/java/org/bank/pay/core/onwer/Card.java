package org.bank.pay.core.onwer;

import jakarta.persistence.*;
import lombok.*;
import org.bank.pay.global.domain.DomainEntity;

import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Card extends DomainEntity {

    @Id
    @GeneratedValue
    private UUID cardId;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private PayOwner payOwner;

    private String cardOwner;
    private String cardNumber;
    private String expireDate;
    private String cvc;
    private String passwordStartwith;
    private String cardName;
}
