package org.bank.pay.core.event.product;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.core.cash.Money;
import org.bank.core.payment.Product;

import java.util.UUID;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
public class VirtualCash extends Product {

    private UUID cardId;

    public VirtualCash(UUID cardId, String name, Money price, Integer quantity) {
        super(name, price, quantity);
        this.cardId = cardId;
    }

    public static VirtualCash of(UUID cardId, Product product) {
        return new VirtualCash(cardId, product.getName(), product.getPrice(),  product.getQuantity());
    }
}
