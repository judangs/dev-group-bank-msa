package org.bank.consumer.fixture;

import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.pay.core.event.product.Category;
import org.bank.pay.core.event.product.PurchasedEvent;
import org.bank.pay.core.event.product.VirtualCash;
import org.bank.user.core.domain.account.Credential;
import org.bank.user.core.domain.fixture.AccountFixture;
import org.bank.user.core.domain.mail.AccountVerificationMail;
import org.bank.user.core.domain.mail.VerificationReason;
import org.bank.user.core.event.registration.AccountCreatedEvent;

import java.util.UUID;

public class EventFixture {

    private static final Credential credential = AccountFixture.authenticated("fixture");
    private static final AccountVerificationMail mail = new AccountVerificationMail(VerificationReason.CREATE_ACCOUNT, credential);

    public static AccountCreatedEvent signupEvent() {
        return AccountCreatedEvent.of(mail.getVerifierInfos());
    }

    public static AccountCreatedEvent signupEvent(String userid) {
        AccountVerificationMail mail = new AccountVerificationMail(VerificationReason.CREATE_ACCOUNT, AccountFixture.authenticated(userid));
        return AccountCreatedEvent.of(mail.getVerifierInfos());
    }

    public static PurchasedEvent charge(AuthClaims user, UUID cardId, Money amount) {
        VirtualCash cash = new VirtualCash(cardId, Category.CategoryType.CHARGE.getDescription(), amount, 1);
        return new PurchasedEvent("paymentId", "payHistId", user, "addmissionState", "primaryPayMeans", cash, amount.toInteger());
    }
}
