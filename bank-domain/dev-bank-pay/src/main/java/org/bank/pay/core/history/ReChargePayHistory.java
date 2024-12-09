package org.bank.pay.core.history;

import jakarta.persistence.Entity;
import lombok.Getter;

@Getter
@Entity
public class ReChargePayHistory extends PayHistory {

    String userid;
    String cardNumber;
}
