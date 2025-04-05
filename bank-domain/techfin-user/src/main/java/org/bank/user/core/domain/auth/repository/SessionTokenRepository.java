package org.bank.user.core.domain.auth.repository;

import java.util.Optional;

public interface SessionTokenRepository {
    void save(String access, String refresh);
    Optional<String> findByToken(String access);
    void deleteByTokenAndUser(String access, String userid);
}
