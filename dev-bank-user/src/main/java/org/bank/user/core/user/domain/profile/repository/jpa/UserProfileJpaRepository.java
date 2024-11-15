package org.bank.user.core.user.domain.profile.repository.jpa;

import org.bank.user.core.user.domain.profile.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileJpaRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByName(String username);
    Optional<UserProfile> findByNameAndResidentNumber(String name, String residentNumber);
}
