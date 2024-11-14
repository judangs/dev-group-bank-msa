package org.bank.user.domain.credential.repository;

import org.bank.user.domain.credential.UserCredential;
import org.bank.user.exception.credential.InvalidArgumentException;

public interface MailVerificationPendingQueue {

    void save(String id, UserCredential credential) throws InvalidArgumentException;
    void deleteById(String id);

    boolean existsById(String id);
    UserCredential findById(String id);


}
