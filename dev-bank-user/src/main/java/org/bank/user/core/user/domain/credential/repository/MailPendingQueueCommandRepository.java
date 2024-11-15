package org.bank.user.core.user.domain.credential.repository;

import org.bank.user.core.user.domain.credential.UserCredential;
import org.bank.user.global.mail.CacheType;

import java.util.List;
import java.util.NoSuchElementException;

public interface MailPendingQueueCommandRepository {

    boolean enableLock(String id);
    CacheType findForCacheTypeById(String id) throws NoSuchElementException;
    UserCredential findyOneById(String id) throws NoSuchElementException;
    List<UserCredential> findById(String id) throws NoSuchElementException;

}
