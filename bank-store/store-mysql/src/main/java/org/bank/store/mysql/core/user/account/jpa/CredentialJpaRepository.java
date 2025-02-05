package org.bank.store.mysql.core.user.account.jpa;

import org.bank.store.mysql.global.infrastructure.JpaBaseRepository;
import org.bank.user.core.domain.account.Credential;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CredentialJpaRepository extends JpaBaseRepository<Credential, UUID> {

    Optional<Credential> findByUserid(String userId);
    Optional<Credential> findByUsername(String username);
}
