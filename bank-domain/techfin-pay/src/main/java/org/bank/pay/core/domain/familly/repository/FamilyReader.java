package org.bank.pay.core.domain.familly.repository;

import org.bank.core.auth.AuthClaims;
import org.bank.pay.core.domain.familly.Family;

import java.util.Optional;
import java.util.UUID;

public interface FamilyReader {

    Optional<Family> findById(UUID familyId);
    Optional<Family> findByUserIsLeader(AuthClaims leader);
    Optional<Family> findByContainUser(AuthClaims user);
}
