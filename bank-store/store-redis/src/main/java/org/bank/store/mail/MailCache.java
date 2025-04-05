package org.bank.store.mail;

import lombok.Getter;
import org.bank.user.core.domain.mail.AccountVerificationMail;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.UUID;

@Getter
@RedisHash(value = "mail", timeToLive = 60 * 3)
public class MailCache implements Serializable {

    @Id
    private String id;
    @Indexed
    private String userid;
    private String email;

    private AccountVerificationMail content;

    private MailCache(AccountVerificationMail content) {
        this.id = UUID.randomUUID().toString();
        this.userid = content.info().getUserid();
        this.email = content.info().getProfile().getEmail();
        this.content = content;
    }

    public static MailCache of(AccountVerificationMail verificationMail) {
        return new MailCache(verificationMail);
    }

}
