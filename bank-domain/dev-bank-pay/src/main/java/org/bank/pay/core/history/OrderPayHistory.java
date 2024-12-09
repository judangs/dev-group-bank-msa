package org.bank.pay.core.history;

import jakarta.persistence.Entity;
import lombok.Getter;

@Getter
@Entity
public class OrderPayHistory extends PayHistory {

    String orderId;
    String storeName;
    String username;

}
