package org.bank.pay.core.history;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.bank.core.cash.Money;
import org.bank.core.cash.PayMethod;
import org.bank.pay.global.domain.DomainEntity;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "history_type")
@Entity
public abstract class PayHistory extends DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;


    protected String userId;
    protected String payName;

    @Embedded
    protected Money payMoney;

    @Enumerated(EnumType.STRING)
    protected PayMethod method;

    protected LocalDateTime transactionDate;
    protected LocalDateTime rollbackDate;
}
