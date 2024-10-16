package org.bank.user.domain.credential;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCredentialJpaRepository extends JpaRepository<UserCredential, Long> {
}
