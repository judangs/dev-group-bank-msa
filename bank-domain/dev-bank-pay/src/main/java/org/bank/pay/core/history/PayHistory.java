package org.bank.pay.core.history;

import jakarta.persistence.*;
import lombok.Getter;
import org.bank.core.cash.Money;
import org.bank.pay.global.domain.DomainEntity;

import java.time.Instant;

@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "pay_type")
@Entity
public abstract class PayHistory extends DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false)
    protected String payName;

    @Embedded
    protected Money payMoney;

    @Enumerated(EnumType.STRING)
    protected payMethod method;

    protected Instant transactionDate;
    protected Instant rollbackDate;
}
