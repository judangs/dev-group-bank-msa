package org.bank.user.core.domain.account.repository;

import org.bank.user.core.domain.account.Credential;

import java.util.Optional;

public interface CredentialRepository {
    Optional<Credential> findByUserid(String userid);
    Optional<Credential> findByUsername(String username);
}
