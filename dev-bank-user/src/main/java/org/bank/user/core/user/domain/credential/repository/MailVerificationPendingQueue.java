package org.bank.user.core.user.domain.credential.repository;

import org.bank.user.core.user.domain.credential.UserCredential;
import org.bank.user.global.exception.InvalidArgumentException;

public interface MailVerificationPendingQueue {

    void save(String id, UserCredential credential) throws InvalidArgumentException;
    void deleteById(String id);

    boolean existsById(String id);
    UserCredential findById(String id);


}
