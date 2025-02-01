package org.bank.user.core.domain.account.service;

import org.bank.user.core.domain.account.Credential;
import org.bank.user.core.domain.account.Profile;

public interface AccountDomainMapper<T> {
    Credential toCredential(T request);
    Profile toProfile(T request);
}
