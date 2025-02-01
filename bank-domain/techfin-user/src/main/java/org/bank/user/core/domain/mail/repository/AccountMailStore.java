package org.bank.user.core.domain.mail.repository;

import org.bank.user.core.domain.account.Credential;
import org.bank.user.core.domain.account.Profile;
import org.bank.user.core.domain.mail.VerificationReason;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

public interface AccountMailStore {
    String save(VerificationReason reason, Credential credential) throws DataIntegrityViolationException;
    String save(VerificationReason reason, List<Credential> credentials) throws DataIntegrityViolationException;
    String save(VerificationReason type, Credential credential, Profile profile) throws DataIntegrityViolationException;
    void deleteById(String id);
}
