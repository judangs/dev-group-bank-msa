package org.bank.store.mysql.core.pay.family.infrastructure;

import lombok.RequiredArgsConstructor;
import org.bank.pay.core.domain.familly.Family;
import org.bank.pay.core.domain.familly.repository.FamilyStore;
import org.bank.store.mysql.core.pay.family.JpaFamilyRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FamilyCommandRepository implements FamilyStore {

    private final JpaFamilyRepository jpaFamilyRepository;

    @Override
    public void saveFamily(Family family) {
        jpaFamilyRepository.save(family);
    }

    @Override
    public void deleteFamily(Family family) {
        jpaFamilyRepository.delete(family);
    }
}
