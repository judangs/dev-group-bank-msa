package org.bank.user.core.user.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.bank.user.core.user.domain.credential.UserCredential;
import org.bank.user.core.user.domain.credential.repository.MailVerificationPendingQueue;
import org.bank.user.global.exception.InvalidArgumentException;
import org.bank.user.global.mail.CacheType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MailVerificationPendingQueueImpl implements MailVerificationPendingQueue {

    private final MailCacheStore mailCacheStore;

    @Override
    public void save(String id, CacheType type, List<UserCredential> credentials) throws InvalidArgumentException {
        if(mailCacheStore.getMailCache().containsKey(id)) {
            throw new InvalidArgumentException("메일 캐시가 이미 존재합니다.");
        }

        mailCacheStore.getMailCache().put(id, new MailCacheMeta(type, credentials));
    }

    @Override
    public void deleteById(String id) {
        mailCacheStore.getMailCache().remove(id);
    }


}
