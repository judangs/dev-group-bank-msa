package org.bank.user.core.user.domain.credential.repository;

import org.bank.user.core.user.domain.credential.UserCredential;
import org.bank.user.global.exception.InvalidArgumentException;
import org.bank.user.global.mail.CacheType;

import java.util.List;

public interface MailVerificationPendingQueue {

    void save(String id, CacheType type, List<UserCredential> credentials) throws InvalidArgumentException;
    void deleteById(String id);

}
