package org.bank.store.mysql.core.user.account.jpa;

import org.bank.store.mysql.global.jpa.JpaBaseRepository;
import org.bank.user.core.domain.account.Profile;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfileJpaRepository extends JpaBaseRepository<Profile, UUID> {

    Optional<Profile> findByNameAndEmail(String name, String email);
    Optional<Profile> findByName(String username);
    Optional<Profile> findByNameAndResidentNumber(String username, String residentNumber);
    Optional<Profile> findProfileByResidentNumberAndEmail(String residentNumber, String email);

}
