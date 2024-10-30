package org.bank.user.domain.policy.infra;

import org.bank.user.domain.policy.UserCredentialPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCredentialPolicyJpaRepository extends JpaRepository<UserCredentialPolicy, Long> {

}
