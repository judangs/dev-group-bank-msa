package org.bank.pay.core.domain.cash;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.pay.core.domain.onwer.PaymentCard;
import org.bank.pay.global.domain.DomainEntity;

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

    @OneToOne
    private Cash cash;

    @OneToOne
    private PaymentCard card;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private ReservationType type;

    private LocalDate scheduledDate;
    private BigDecimal triggerBalance;

    public static ReservedCharge of(Cash cash, PaymentCard card, BigDecimal amount, ReservationType type, LocalDate scheduledDate) {
        return ReservedCharge.builder()
                .cash(cash)
                .card(card)
                .amount(amount)
                .type(type)
                .scheduledDate(scheduledDate)
                .build();
    }

    public static ReservedCharge of(Cash cash, PaymentCard card, BigDecimal amount, ReservationType type, BigDecimal triggerBalance) {
        return ReservedCharge.builder()
                .cash(cash)
                .card(card)
                .amount(amount)
                .type(type)
                .triggerBalance(triggerBalance)
                .build();
    }
}