package org.bank.user.core.mail.domain.repository;

import org.bank.user.core.user.domain.credential.UserCredential;
import org.bank.user.global.mail.CacheType;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

public interface MailVerificationPendingQueryRepository {

    void save(String id, CacheType type, List<UserCredential> credentials) throws DataIntegrityViolationException;
    void deleteById(String id);

}
