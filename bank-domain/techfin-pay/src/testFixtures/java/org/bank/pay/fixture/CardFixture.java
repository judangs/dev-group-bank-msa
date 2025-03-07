package org.bank.pay.fixture;

import org.bank.core.auth.AuthClaims;
import org.bank.pay.core.domain.cash.Cash;
import org.bank.pay.core.domain.owner.PayOwner;
import org.bank.pay.core.domain.card.PaymentCard;

import java.time.LocalDate;
import java.util.UUID;

public class CardFixture {

    public static final AuthClaims user = new AuthClaims.ConcreteAuthClaims("user", "fixture", "user@email.com");
    private static final PayOwner owner = new PayOwner(user);



    public static PaymentCard card() {
        return PaymentCard.builder()
                .cardId(UUID.randomUUID())
                .build();
    }

    public static PaymentCard cashable() {
        return PaymentCard.builder()
                .cardId(UUID.randomUUID())
                .cardName("카드 별칭")
                .cardNumber("1111-2222-3333-4444")
                .cvc("111")
                .passwordStartwith("12")
                .expireDate(LocalDate.now().plusYears(5).toString())
                .cash(new Cash(owner))
                .payOwner(owner)
                .build();
    }

    public static PaymentCard cashable(PayOwner owner, boolean id) {
        return PaymentCard.builder()
                .cardId(id ? UUID.randomUUID() : null)
                .cardName("카드 별칭")
                .cardNumber("1111-2222-3333-4444")
                .cvc("111")
                .passwordStartwith("12")
                .expireDate(LocalDate.now().plusYears(5).toString())
                .cash(new Cash(owner))
                .payOwner(owner)
                .build();
    }

    public static PaymentCard cashable(boolean id) {
        return PaymentCard.builder()
                .cardName("카드 별칭")
                .cardNumber("1111-2222-3333-4444")
                .cvc("111")
                .passwordStartwith("12")
                .expireDate(LocalDate.now().plusYears(5).toString())
                .cash(new Cash(owner))
                .payOwner(owner)
                .build();
    }


    public static PaymentCard naming(String name) {
        return PaymentCard.builder()
                .cardId(UUID.randomUUID())
                .cardName(name)
                .cardNumber("1111-2222-3333-4444")
                .cvc("111")
                .passwordStartwith("12")
                .expireDate(LocalDate.now().plusYears(5).toString())
                .cash(new Cash(owner))
                .payOwner(owner)
                .build();
    }

    public static PaymentCard naming(String name, boolean id) {
        return PaymentCard.builder()
                .cardName(name)
                .cardNumber("1111-2222-3333-4444")
                .cvc("111")
                .passwordStartwith("12")
                .expireDate(LocalDate.now().plusYears(5).toString())
                .cash(new Cash(owner))
                .payOwner(owner)
                .build();
    }
}
