package org.bank.user.core.mail.infrastructure.implement;

import lombok.RequiredArgsConstructor;
import org.bank.user.core.mail.domain.MailCache;
import org.bank.user.core.mail.domain.repository.MailVerificationPendingCommandRepository;
import org.bank.user.core.mail.infrastructure.MailCacheStore;
import org.bank.user.global.mail.CacheType;
import org.springframework.stereotype.Repository;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;

@Repository
@RequiredArgsConstructor
public class MailVerificationPendingCommandRepositoryImpl implements MailVerificationPendingCommandRepository {

    private final MailCacheStore mailCacheStore;

    private boolean existByid(String id) {

        if(!mailCacheStore.getMailCache().containsKey(id)) {
            return false;
        }

        mailCacheStore.getMailCache().get(id).readCache();
        return true;
    }

    @Override
    public boolean isEmpty() {
        return mailCacheStore.getMailCache().isEmpty();
    }

    @Override
    public CacheType findForCacheTypeById(String id) throws NoSuchElementException, TimeoutException {
        if(!existByid(id)) {
            throw new NoSuchElementException();
        }

        MailCache mailCache = mailCacheStore.getMailCache().get(id);
        if(mailCache.isExpired()) {
            throw new TimeoutException();
        }


        return mailCache.getType();
    }

    @Override
    public MailCache findById(String id) throws NoSuchElementException {
        if(!existByid(id)) {
            throw new NoSuchElementException();
        }

        return mailCacheStore.getMailCache().get(id);
    }

}
