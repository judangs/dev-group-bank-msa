package org.bank.user.core.user.domain.credential.repository.jpa;

import org.bank.user.core.user.domain.credential.UserCredentialPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCredentialPolicyJpaRepository extends JpaRepository<UserCredentialPolicy, Long> {

}
