package org.bank.user.core.user.infrastructure.persistence;

import org.bank.user.core.user.domain.credential.UserCredential;
import org.bank.user.core.user.domain.credential.repository.MailVerificationPendingQueue;
import org.bank.user.global.exception.InvalidArgumentException;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MailVerificationPendingQueueImpl implements MailVerificationPendingQueue {

    // 사용자 이메일 인증을 기다리는 대기 큐
    private final Map<String, UserCredential> mailCache = new ConcurrentHashMap<>();

    @Override
    public void save(String id, UserCredential credential) throws InvalidArgumentException {
        if(this.existsById(id)) {
            throw new InvalidArgumentException();
        }

        mailCache.put(id, credential);
    }

    @Override
    public void deleteById(String id) {
        mailCache.remove(id);
    }

    @Override
    public boolean existsById(String id) {
        return mailCache.containsKey(id);
    }

    @Override
    public UserCredential findById(String id) {
        return mailCache.get(id);
    }
}
