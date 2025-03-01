package org.bank.pay.core.domain.cash;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.core.cash.Money;
import org.bank.pay.global.domain.DomainEntity;
import org.bank.pay.global.domain.card.PayCard;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "pay_cash_reserved_charge_tb")
@Entity
public class ReservedCharge extends DomainEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private UUID cardId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "balance", column = @Column(name = "credit", precision = 30, scale = 2))
    })
    private Money amount;

    @Enumerated(EnumType.STRING)
    private ReservationType type;

    private LocalDate scheduledDate;
    private Money triggerBalance;

    public static ReservedCharge of(PayCard card, BigDecimal amount, LocalDate scheduledDate) {
        return ReservedCharge.builder()
                .cardId(card.getCardId())
                .amount(new Money(amount))
                .type(ReservationType.DATE)
                .scheduledDate(scheduledDate)
                .build();
    }

    public static ReservedCharge of(PayCard card, Money amount, LocalDate scheduledDate) {
        return ReservedCharge.builder()
                .cardId(card.getCardId())
                .amount(amount)
                .type(ReservationType.DATE)
                .scheduledDate(scheduledDate)
                .build();
    }

    public static ReservedCharge of(PayCard card, BigDecimal amount, BigDecimal triggerBalance) {
        return ReservedCharge.builder()
                .cardId(card.getCardId())
                .amount(new Money(amount))
                .type(ReservationType.BALANCE)
                .triggerBalance(new Money(triggerBalance))
                .build();
    }

    public static ReservedCharge of(PayCard card, Money amount, Money triggerBalance) {
        return ReservedCharge.builder()
                .cardId(card.getCardId())
                .amount(amount)
                .type(ReservationType.BALANCE)
                .triggerBalance(triggerBalance)
                .build();
    }
}