package org.bank.user.core.user.domain.credential.repository.jpa;

import org.bank.user.core.user.domain.credential.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCredentialJpaRepository extends JpaRepository<UserCredential, Long> {

    Optional<UserCredential> findByUserid(String userid);
    Optional<UserCredential> findByUsername(String username);

}
