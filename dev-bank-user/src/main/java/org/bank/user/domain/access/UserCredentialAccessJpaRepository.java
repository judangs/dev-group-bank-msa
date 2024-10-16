package org.bank.user.domain.access;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCredentialAccessJpaRepository extends JpaRepository<UserCredentialAccess, Long> {

}
