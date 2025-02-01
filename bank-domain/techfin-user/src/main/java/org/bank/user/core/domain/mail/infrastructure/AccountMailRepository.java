package org.bank.user.core.domain.mail.infrastructure;

import org.bank.user.core.domain.mail.AccountVerificationMail;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AccountMailRepository {

    // 여기서도 문제가 발생할 수 있는 것 해결하기.
    private final Map<String, AccountVerificationMail> storage = new ConcurrentHashMap<>();

    public String save(AccountVerificationMail data) {
        String id = UUID.randomUUID().toString();
        storage.putIfAbsent(id, data);
        return id;
    }

    public Optional<AccountVerificationMail> findById(String id) {
        AccountVerificationMail verificationMail =  storage.get(id);
        if(verificationMail == null) {
            throw new NoSuchElementException("일치하는 키를 가져올 수 없습니다.");
        }

        return Optional.of(verificationMail);
    }

    public void deleteById(String id) {
        storage.remove(id);
    }
}
