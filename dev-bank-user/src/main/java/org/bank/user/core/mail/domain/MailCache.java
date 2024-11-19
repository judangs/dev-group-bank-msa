package org.bank.user.core.mail.domain;

import lombok.Getter;
import org.bank.user.core.user.domain.credential.UserCredential;
import org.bank.user.global.mail.CacheType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Getter
public class MailCache {

    private CacheType type;
    private List<UserCredential> credentials;
    private LocalDateTime expire;
    private boolean cacheRead = false;

    public MailCache(CacheType type, List<UserCredential> credentials) {
        this.type = type;
        this.credentials = credentials;
        this.expire = LocalDateTime.now().plusMinutes(3);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expire);
    }

    public void readCache() {
        this.cacheRead = true;
    }


    public UserCredential getFrontCredential() {
        if(credentials.isEmpty())
            throw new NoSuchElementException();

        return credentials.get(0);
    }



}
