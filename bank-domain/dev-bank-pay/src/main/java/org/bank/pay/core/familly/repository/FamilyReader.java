package org.bank.pay.core.familly.repository;

import org.bank.pay.core.familly.Family;
import org.bank.pay.core.familly.MemberClaims;

import java.util.Optional;
import java.util.UUID;

public interface FamilyReader {

    Optional<Family> findById(UUID familyId);
    Optional<Family> findByUserIsLeader(MemberClaims memberClaims);
}
