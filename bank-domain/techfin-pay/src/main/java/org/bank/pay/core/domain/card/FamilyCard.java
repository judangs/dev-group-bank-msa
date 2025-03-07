package org.bank.pay.core.domain.card;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@Table(name = "family_card_tb")
@Entity
public class FamilyCard extends PayCard {

    public void create(CardType type) {
        super.create();
        this.type = type;
    }
}
