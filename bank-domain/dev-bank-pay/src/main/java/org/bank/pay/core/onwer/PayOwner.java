package org.bank.pay.core.onwer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.pay.core.cash.Cash;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
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
    private List<Card> cards;

    @OneToOne(mappedBy = "payOwner")
    private Cash cash;
}
