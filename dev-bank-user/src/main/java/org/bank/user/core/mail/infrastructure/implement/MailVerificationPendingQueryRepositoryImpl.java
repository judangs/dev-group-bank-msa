package org.bank.user.core.mail.infrastructure.implement;

import lombok.RequiredArgsConstructor;
import org.bank.user.core.user.domain.credential.UserCredential;
import org.bank.user.core.mail.domain.repository.MailVerificationPendingQueryRepository;
import org.bank.user.core.mail.domain.MailCache;
import org.bank.user.core.mail.infrastructure.MailCacheStore;
import org.bank.user.global.mail.CacheType;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MailVerificationPendingQueryRepositoryImpl implements MailVerificationPendingQueryRepository {

    private final MailCacheStore mailCacheStore;

    @Override
    public void save(String id, CacheType type, List<UserCredential> credentials) throws DataIntegrityViolationException {
        if(mailCacheStore.getMailCache().containsKey(id)) {
            throw new DataIntegrityViolationException("메일 캐시가 이미 존재합니다.");
        }

        mailCacheStore.getMailCache().put(id, new MailCache(type, credentials));
    }

    @Override
    public void deleteById(String id) {
        mailCacheStore.getMailCache().remove(id);
    }


}
