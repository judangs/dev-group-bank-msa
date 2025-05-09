package org.bank.store.mysql.core.pay.family;

import org.bank.core.auth.AuthClaims;
import org.bank.pay.core.domain.familly.Family;
import org.bank.store.mysql.global.jpa.JpaBaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaFamilyRepository extends JpaBaseRepository<Family, UUID> {

    @Query("SELECT f FROM Family f WHERE f.leader.userid = :leaderId")
    Optional<Family> findByLeader(@Param("leaderId") String leaderId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM participant p WHERE p.family_id = :familyId", nativeQuery = true)
    void deleteParticipantsByFamilyId(@Param("familyId") UUID familyId);

    @Query("SELECT f FROM Family f WHERE element(f.participants).userid = :#{#user.userid}")
    Optional<Family> findByContainUser(@Param("user") AuthClaims user);
}