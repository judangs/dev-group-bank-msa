package org.bank.user.core.domain.mail.infrastructure;

import lombok.RequiredArgsConstructor;
import org.bank.user.core.domain.account.Credential;
import org.bank.user.core.domain.account.Profile;
import org.bank.user.core.domain.mail.AccountVerificationMail;
import org.bank.user.core.domain.mail.VerificationReason;
import org.bank.user.core.domain.mail.repository.AccountMailStore;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AccountMailQueryRepository implements AccountMailStore {

    private final AccountMailRepository accountMailRepository;

    @Override
    public String save(VerificationReason reason, List<Credential> credentials) throws DataIntegrityViolationException {
        return accountMailRepository.save(new AccountVerificationMail(reason, credentials));
    }


    @Override
    public String save(VerificationReason reason, Credential credential) throws DataIntegrityViolationException {
        return accountMailRepository.save(new AccountVerificationMail(reason, credential));
    }

    @Override
    public String save(VerificationReason reason, Credential credential, Profile profile) throws DataIntegrityViolationException {
        return accountMailRepository.save(new AccountVerificationMail(reason, credential, profile));
    }

    @Override
    public void deleteById(String id) {
        accountMailRepository.deleteById(id);
    }
}
