package org.bank.user.core.mail.domain.repository;

import org.bank.user.core.mail.domain.MailCache;
import org.bank.user.global.mail.CacheType;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;

public interface MailVerificationPendingCommandRepository {

    boolean isEmpty();
    CacheType findForCacheTypeById(String id) throws NoSuchElementException, TimeoutException;
    MailCache findById(String id) throws NoSuchElementException;

}
