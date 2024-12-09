package org.bank.pay.core.cash;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.core.cash.Money;
import org.bank.pay.core.onwer.PayOwner;
import org.bank.pay.global.domain.DomainEntity;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cash extends DomainEntity {

    @Id
    @GeneratedValue
    private UUID cashId;

    @Embedded
    Money cash;

    @Embedded
    PayLimit limits;

    @OneToOne
    @JoinColumn(name = "owner_id")
    private PayOwner payOwner;

}
