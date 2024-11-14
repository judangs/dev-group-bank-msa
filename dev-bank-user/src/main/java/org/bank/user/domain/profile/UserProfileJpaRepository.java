package org.bank.user.domain.profile;

import org.reactivestreams.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileJpaRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByName(String username);
    Optional<UserProfile> findByNameAndResidentNumber(String name, String residentNumber);
}
