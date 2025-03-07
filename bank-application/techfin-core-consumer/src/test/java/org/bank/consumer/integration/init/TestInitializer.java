package org.bank.consumer.integration.init;

import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.pay.core.domain.cash.service.CashChargeSerivce;
import org.bank.pay.core.domain.familly.Family;
import org.bank.pay.core.domain.familly.repository.FamilyReader;
import org.bank.pay.core.domain.familly.repository.FamilyStore;
import org.bank.pay.core.domain.owner.PayOwner;
import org.bank.pay.core.domain.card.PaymentCard;
import org.bank.pay.core.domain.owner.repository.PayOwnerReader;
import org.bank.pay.core.domain.owner.repository.PayOwnerStore;
import org.bank.pay.fixture.CardFixture;
import org.bank.pay.fixture.FamilyFixture;
import org.bank.pay.fixture.UserFixture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestInitializer {

    @Autowired
    private PayOwnerStore payOwnerStore;
    @Autowired
    private PayOwnerReader payOwnerReader;
    @Autowired
    private FamilyStore familyStore;
    @Autowired
    private FamilyReader familyReader;
    @Autowired
    private CashChargeSerivce cashChargeSerivce;

    public static final Money INITIALBALANCE = new Money(10000);


    public void init(AuthClaims user) {
        owner(user);
        card(getPayOwner(user));
        family(user);
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

        cashChargeSerivce.charge(card, INITIALBALANCE);
        payOwnerStore.save(owner);
    }

    public void family(AuthClaims user) {
        familyStore.saveFamily(FamilyFixture.family(user));
    }

    public PayOwner getPayOwner(AuthClaims user) {
        return payOwnerReader.findByUserClaims(user).get();
    }

    public PaymentCard getCard(AuthClaims user) {
        return getPayOwner(user).getPaymentCards().get(0);
    }

    public Family getFamily(AuthClaims user) {
        return familyReader.findByUserIsLeader(user).orElseThrow(IllegalAccessError::new);
    }
}
