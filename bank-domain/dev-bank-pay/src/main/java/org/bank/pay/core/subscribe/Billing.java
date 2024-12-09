package org.bank.pay.core.subscribe;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.core.cash.Money;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Billing {

    private String name;

    @Enumerated(EnumType.STRING)
    private BillingCycle cycle;

    @Embedded
    private Money price;
}
