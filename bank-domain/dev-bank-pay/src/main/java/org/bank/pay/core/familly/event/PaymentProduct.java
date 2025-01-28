package org.bank.pay.core.familly.event;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bank.core.cash.Money;
import org.bank.core.payment.Product;

@Getter
@Embeddable
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class PaymentProduct extends Product  {

    public PaymentProduct(String name, Integer price, Integer quantity) {
        super(name, price, quantity);
    }

    public PaymentProduct(String name, Money price, Integer quantity) {
        super(name, price, quantity);
    }

    public PaymentProduct() {
        super();
    }

    public static PaymentProduct of(Product product) {
        return new PaymentProduct(product.getName(), product.getPrice(), product.getQuantity());
    }
}
