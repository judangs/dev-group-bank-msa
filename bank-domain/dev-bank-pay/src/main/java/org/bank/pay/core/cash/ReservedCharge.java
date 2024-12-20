package org.bank.pay.core.cash;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.pay.core.onwer.PaymentCard;
import org.bank.pay.global.domain.DomainEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ReservedCharge extends DomainEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne
    private Cash cash;

    @OneToOne
    private PaymentCard card;

    private BigDecimal amount;
    private BigDecimal triggerBalance;
    private LocalDate scheduledDate;

    public void setTriggerBalance(BigDecimal balance) {
        this.triggerBalance = balance;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }
}