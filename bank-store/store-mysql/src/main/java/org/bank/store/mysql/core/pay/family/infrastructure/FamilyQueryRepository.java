package org.bank.store.mysql.core.pay.family.infrastructure;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.domain.DomainNames;
import org.bank.pay.core.domain.familly.Family;
import org.bank.pay.core.domain.familly.repository.FamilyReader;
import org.bank.store.mysql.core.pay.family.JpaFamilyRepository;
import org.bank.store.source.DataSourceType;
import org.bank.store.source.NamedRepositorySource;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@NamedRepositorySource(domain = DomainNames.PAY, type = DataSourceType.READWRITE)
@RequiredArgsConstructor
public class FamilyQueryRepository implements FamilyReader {

    private final JpaFamilyRepository jpaFamilyRepository;

    @Override
    public Optional<Family> findById(UUID familyId) {
        return jpaFamilyRepository.findById(familyId);
    }

    @Override
    public Optional<Family> findByUserIsLeader(AuthClaims leader) {
        return jpaFamilyRepository.findByLeader(leader.getUserid());
    }

    @Override
    public Optional<Family> findByContainUser(AuthClaims user) {
        return jpaFamilyRepository.findByContainUser(user);
    }
}
