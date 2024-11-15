package org.bank.user.core.user.infrastructure.persistence;

import lombok.Getter;
import org.bank.user.core.user.domain.credential.UserCredential;
import org.bank.user.global.mail.CacheType;

import java.util.List;

@Getter
public class MailCacheMeta {

    private CacheType type;
    private List<UserCredential> credentials;
    private boolean cacheRead = false;

    public MailCacheMeta(CacheType type, List<UserCredential> credentials) {
        this.type = type;
        this.credentials = credentials;
    }

    public void readCache() {
        this.cacheRead = true;
    }



}
