package org.bank.store.mail;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthenticationException;
import org.bank.user.core.domain.account.Credential;
import org.bank.user.core.domain.account.Profile;
import org.bank.user.core.domain.mail.AccountVerificationMail;
import org.bank.user.core.domain.mail.VerificationReason;
import org.bank.user.core.domain.mail.repository.AccountMailReader;
import org.bank.user.core.domain.mail.repository.AccountMailStore;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;

@org.springframework.context.annotation.Profile(value = {"redis", "production"})
@Repository
@RequiredArgsConstructor
public class RedisAccountAuthMailRepository implements AccountMailReader, AccountMailStore {

    private final RedisAccountMailCrudRepository redisAccountMailCrudRepository;

    @Override
    public VerificationReason findVerificationReasonById(String id) throws TimeoutException {
        return redisAccountMailCrudRepository.findById(id)
                .map(cache -> cache.getContent().getReason())
                .orElseThrow(AuthenticationException::new);
    }

    @Override
    public AccountVerificationMail findById(String id) throws NoSuchElementException {
        return redisAccountMailCrudRepository.findById(id)
                .map(MailCache::getContent)
                .orElseThrow(AuthenticationException::new);
    }

    @Override
    public List<String> findVerifierUserIdById(String id) {
        return redisAccountMailCrudRepository.findById(id)
                .map(cache -> cache.getContent().getVerifierInfos().stream()
                        .map(AccountVerificationMail.VerifierInfo::getUserid))
                .orElseThrow(AuthenticationException::new).toList();
    }

    @Override
    public String save(VerificationReason reason, Credential credential) throws DataIntegrityViolationException {
        MailCache cache = MailCache.of(new AccountVerificationMail(reason, credential));
        redisAccountMailCrudRepository.save(cache);
        return cache.getId();
    }

    @Override
    public String save(VerificationReason reason, List<Credential> credentials) throws DataIntegrityViolationException {
        MailCache cache = MailCache.of(new AccountVerificationMail(reason, credentials));
        redisAccountMailCrudRepository.save(cache);
        return cache.getId();
    }

    @Override
    public String save(VerificationReason reason, Credential credential, Profile profile) throws DataIntegrityViolationException {
        MailCache cache = MailCache.of(new AccountVerificationMail(reason, credential, profile));
        redisAccountMailCrudRepository.save(cache);
        return cache.getId();
    }

    @Override
    public void deleteById(String id) {
        redisAccountMailCrudRepository.deleteById(id);
    }
}