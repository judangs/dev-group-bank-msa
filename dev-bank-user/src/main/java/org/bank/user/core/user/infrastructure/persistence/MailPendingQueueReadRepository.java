package org.bank.user.core.user.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.bank.user.core.user.domain.credential.repository.MailPendingQueueCommandRepository;
import org.bank.user.core.user.domain.credential.UserCredential;
import org.bank.user.global.mail.CacheType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
public class MailPendingQueueReadRepository implements MailPendingQueueCommandRepository {

    private final MailCacheStore mailCacheStore;

    @Override
    public boolean enableLock(String id) {

        if(!mailCacheStore.getMailCache().containsKey(id)) {
            return false;
        }

        mailCacheStore.getMailCache().get(id).readCache();
        return true;

    }

    @Override
    public CacheType findForCacheTypeById(String id) throws NoSuchElementException {
        if(!enableLock(id)) {
            throw new NoSuchElementException();
        }
        return mailCacheStore.getMailCache().get(id).getType();
    }

    @Override
    public UserCredential findyOneById(String id) throws NoSuchElementException {
        if(!enableLock(id)) {
            throw new NoSuchElementException();
        }

        return mailCacheStore.getMailCache().get(id).getCredentials().get(0);
    }

    @Override
    public List<UserCredential> findById(String id) throws NoSuchElementException {
        return mailCacheStore.getMailCache().get(id).getCredentials();
    }

}
