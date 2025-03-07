package org.bank.store.mysql.core.pay.unit.init;

import org.bank.core.auth.AuthClaims;
import org.bank.pay.core.domain.owner.PayOwner;
import org.bank.pay.core.domain.card.PaymentCard;
import org.bank.pay.core.domain.owner.repository.PayOwnerReader;
import org.bank.pay.core.domain.owner.repository.PayOwnerStore;
import org.bank.pay.fixture.CardFixture;
import org.bank.pay.fixture.UserFixture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@TestConfiguration
@ComponentScan("org.bank.store.mysql.core.pay.owner")
@EnableJpaRepositories("org.bank.store.mysql.core.pay.owner")
public class TestInitializer {

    @Autowired
    private PayOwnerStore payOwnerStore;
    @Autowired
    private PayOwnerReader payOwnerReader;


    public void init(AuthClaims user) {
        owner(user);
        card(getPayOwner(user));
    }

    public PayOwner owner() {
        PayOwner owner = new PayOwner(UserFixture.authenticated());
        payOwnerStore.save(owner);

        return owner;
    }

    public void owner(AuthClaims user) {
        PayOwner owner = new PayOwner(user);
        payOwnerStore.save(owner);
    }

    public void card(PayOwner owner) {
        PaymentCard card = CardFixture.cashable(owner, false);
        owner.addPaymentCard(card);
        payOwnerStore.save(owner);
    }

    public PayOwner getPayOwner(AuthClaims user) {
        return payOwnerReader.findByUserClaims(user).get();
    }

    public PaymentCard getCard(AuthClaims user) {
        return getPayOwner(user).getPaymentCards().get(0);
    }
}
