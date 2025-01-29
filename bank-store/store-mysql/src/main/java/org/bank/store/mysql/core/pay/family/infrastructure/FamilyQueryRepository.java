package org.bank.store.mysql.core.pay.family.infrastructure;

import lombok.RequiredArgsConstructor;
import org.bank.pay.core.domain.familly.Family;
import org.bank.pay.core.domain.familly.MemberClaims;
import org.bank.pay.core.domain.familly.repository.FamilyReader;
import org.bank.store.mysql.core.pay.family.JpaFamilyRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FamilyQueryRepository implements FamilyReader {

    private final JpaFamilyRepository jpaFamilyRepository;

    @Override
    public Optional<Family> findById(UUID familyId) {
        return jpaFamilyRepository.findById(familyId);
    }

    @Override
    public Optional<Family> findByUserIsLeader(MemberClaims leader) {
        return jpaFamilyRepository.findByLeader(leader.getUserid());
    }
}
