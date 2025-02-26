package org.bank.pay.core.payment.product;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bank.core.cash.Money;
import org.bank.core.payment.Product;

import java.util.UUID;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class VirtualCash extends Product {

    private UUID cardId;

    public VirtualCash(UUID cardId, String name, Money price, Integer quantity) {
        super(name, price, quantity);
        this.cardId = cardId;
    }
}
