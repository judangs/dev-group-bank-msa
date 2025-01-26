package org.bank.core.payment;

import lombok.Getter;
import org.bank.core.cash.Money;

@Getter
public class Product {

    private String productName;
    private Money amount;
    private Integer quantity;

    public Product(String productName, Integer amount, Integer quantity) {
        this.productName = productName;
        this.amount = new Money(amount);
        this.quantity = quantity;
    }

    public Integer calculateTotalAmount() {
        return Integer.valueOf(amount.toInteger() * quantity);
    }

}
