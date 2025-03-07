package org.bank.pay.core.integration;

import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.pay.core.domain.card.PayCard;
import org.bank.pay.core.domain.card.PaymentCard;
import org.bank.pay.core.domain.owner.PayOwner;
import org.bank.pay.core.domain.owner.repository.PayOwnerReader;
import org.bank.pay.core.domain.owner.repository.PayOwnerStore;
import org.bank.pay.core.unit.CardUnitTest;
import org.bank.pay.core.unit.CashUnitTest;
import org.bank.pay.fixture.CardFixture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Optional;

@Configuration
@Import(value = {
        CardUnitTest.class, CashUnitTest.class
})
public class IntegrationTestInitializer {

    @Autowired
    private PayOwnerReader payOwnerReader;
    @Autowired
    private PayOwnerStore payOwnerStore;

    public void init(AuthClaims user) {
        if(find(user).isEmpty()) {
            PayOwner owner = new PayOwner(user);
            PaymentCard card = CardFixture.cashable(owner, false);
            card.getCash().charge(new Money(10000));
            owner.addPaymentCard(card);

            payOwnerStore.save(owner);
        }
    }

    public Optional<PayOwner> find(AuthClaims user) {
        return payOwnerReader.findByUserClaims(user);
    }

    public Optional<PayCard> card(AuthClaims user) {
        return find(user).map(owner -> owner.getPaymentCards().get(0));
    }

}
