package org.bank.pay.core.domain.history;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@DiscriminatorValue("order")
@Entity
public class OrderPayHistory extends PayHistory {

    private String orderId;
    private String storeName;
    private String username;
}
