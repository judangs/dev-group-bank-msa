package org.bank.pay.core.history;

import jakarta.persistence.Entity;
import lombok.Getter;

@Getter
@Entity
public class TransferPayHistory extends PayHistory {

    String transferTo;
}
