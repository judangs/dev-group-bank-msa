package org.bank.user.core.event.registration;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.kafka.KafkaEvent;
import org.bank.user.core.domain.mail.AccountVerificationMail;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class AccountCreatedEvent extends KafkaEvent {

    private AuthClaims credential;
    private EventStatus status;

    enum EventStatus {
        PENDING, ACCEPTED, REJECTED, EXPIRED
    }

    private AccountCreatedEvent(AuthClaims credential) {
        super();
        this.credential = credential;
        this.status = EventStatus.PENDING;
    }

    public static AccountCreatedEvent of(AccountVerificationMail.VerifierInfo info) {
        AuthClaims.ConcreteAuthClaims concreteAuthClaims = new AuthClaims.ConcreteAuthClaims(info.getUserid(), info.getUsername(), info.getEmail());
        return new AccountCreatedEvent(concreteAuthClaims);
    }

    public static AccountCreatedEvent of(List<AccountVerificationMail.VerifierInfo> infos) {
        return AccountCreatedEvent.of(infos.get(0));
    }
}
