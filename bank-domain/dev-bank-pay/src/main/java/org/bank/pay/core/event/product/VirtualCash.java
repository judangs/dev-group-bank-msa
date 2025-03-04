package org.bank.pay.core.event.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.core.cash.Money;
import org.bank.core.payment.Product;

import java.util.UUID;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@JsonDeserialize(converter = ProductToVirtualCashConverter.class)
@NoArgsConstructor
public class VirtualCash extends Product {

    private UUID cardId;

    public VirtualCash(UUID cardId, String name, Money price, Integer quantity) {
        super(name, price, quantity);
        this.cardId = cardId;
    }


    public static VirtualCash of(Product product) {
        return new VirtualCash(product.getCardId(), product.getName(), product.getPrice(),  product.getQuantity());
    }
}
