package org.bank.pay.fixture;

import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.core.payment.Product;
import org.bank.pay.core.domain.familly.Family;
import org.bank.pay.core.domain.familly.MemberClaims;
import org.bank.pay.core.event.family.FamilyInvitation;
import org.bank.pay.core.event.family.FamilyPayment;
import org.bank.pay.core.event.family.kafka.CashConversionEvent;
import org.bank.pay.core.event.family.kafka.InviteEvent;
import org.bank.pay.core.event.family.kafka.PaymentEvent;
import org.bank.pay.global.domain.card.PayCard;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class FamilyEventFixture {

    private static Family family = new Family(FamilyFixture.leader());
    private static PayCard card = CardFixture.cashable();

    private static InviteEvent inviteEvent = new InviteEvent(family.getFamilyId(), FamilyFixture.follower());;
    private static PaymentEvent paymentEvent = new PaymentEvent(family.getFamilyId(), FamilyFixture.follower(), Arrays.asList(new Product("테스트 결제", 10000, 1)));;
    private static CashConversionEvent cashConversionEvent = new CashConversionEvent(family.getFamilyId(), card.getCardId(), FamilyFixture.leader(), new Money(1000));;


    public static InviteEvent invite() {
        return inviteEvent;
    }


    public static InviteEvent invite(UUID familyId, AuthClaims follower) {
        return new InviteEvent(familyId, follower);
    }

    public static PaymentEvent pay() {
        return paymentEvent;
    }

    public static PaymentEvent pay(UUID familyId, AuthClaims follower, List<Product> products) {
        return new PaymentEvent(familyId, MemberClaims.of(follower), products);
    }

    public static CashConversionEvent cashConversion() {
        return cashConversionEvent;
    }

    public static CashConversionEvent cashConversion(UUID familyId, UUID cardId, AuthClaims from, Money amount) {
        return new CashConversionEvent(familyId, cardId, MemberClaims.of(from), amount);
    }

    public static FamilyInvitation invitation() {
        return FamilyInvitation.of(invite());
    }

    public static FamilyInvitation invitation(Family family, AuthClaims follower) {
        return FamilyInvitation.of(invite(family.getFamilyId(), follower));
    }

    public static FamilyPayment payment() {
        return FamilyPayment.of(pay());
    }


}
