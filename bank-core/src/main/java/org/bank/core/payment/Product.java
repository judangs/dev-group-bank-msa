package org.bank.core.payment;

import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.core.cash.Money;

import java.io.Serializable;

@Getter
@MappedSuperclass
@NoArgsConstructor
@EqualsAndHashCode
public class Product implements Serializable {

    @EqualsAndHashCode.Include
    protected String name;
    @EqualsAndHashCode.Include
    protected Money price;
    @EqualsAndHashCode.Include
    protected Integer quantity;


    public Product(String name, Integer price, Integer quantity) {
        this.name = name;
        this.price = new Money(price);
        this.quantity = quantity;
    }

    public Product(String name, Money price, Integer quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Integer price() {
        return price.getBalance().intValue();
    }

    public Integer totalPrice() {
        return Integer.valueOf(price.toInteger() * quantity);
    }

}
