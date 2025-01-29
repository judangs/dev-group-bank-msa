package org.bank.pay.core.domain.familly.repository;

import org.bank.pay.core.domain.familly.Family;
import org.bank.pay.core.domain.familly.MemberClaims;

import java.util.Optional;
import java.util.UUID;

public interface FamilyReader {

    Optional<Family> findById(UUID familyId);
    Optional<Family> findByUserIsLeader(MemberClaims memberClaims);
}
