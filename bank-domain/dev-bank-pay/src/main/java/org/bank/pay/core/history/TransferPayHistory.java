package org.bank.pay.core.history;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@DiscriminatorValue("transfer")
@Entity
public class TransferPayHistory extends PayHistory {

    private String transferTo;

}
