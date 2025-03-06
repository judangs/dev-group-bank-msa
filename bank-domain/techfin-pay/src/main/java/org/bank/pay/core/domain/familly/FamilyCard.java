package org.bank.pay.core.domain.familly;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.bank.pay.global.domain.card.CardType;
import org.bank.pay.global.domain.card.PayCard;

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
