package org.bank.store.mysql.core.pay.family.infrastructure;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.domain.DomainNames;
import org.bank.pay.core.domain.familly.repository.FamilyEventReader;
import org.bank.pay.core.domain.familly.repository.FamilyEventStore;
import org.bank.pay.core.event.family.FamilyEventEntity;
import org.bank.pay.core.event.family.FamilyInvitation;
import org.bank.pay.core.event.family.FamilyPayment;
import org.bank.store.mysql.core.pay.family.JpaFamilyEventRepository;
import org.bank.store.source.DataSourceType;
import org.bank.store.source.NamedRepositorySource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@NamedRepositorySource(domain = DomainNames.PAY, type = DataSourceType.READWRITE)
@RequiredArgsConstructor
public class FamilyEventRepository implements FamilyEventStore, FamilyEventReader {


    private final JpaFamilyEventRepository jpaFamilyEventRepository;

    @Override
    public void store(FamilyEventEntity event) {
        jpaFamilyEventRepository.save(event);
    }

    @Override
    public Optional<FamilyInvitation> findInvitationEventByUser(AuthClaims user) {

        return jpaFamilyEventRepository.findInvitationEventByUser(user);
    }

    @Override
    public Optional<FamilyInvitation> findInvitationEventById(UUID id) {
        return jpaFamilyEventRepository.findInvitationEventById(id);
    }

    @Override
    public Optional<FamilyPayment> findPaymentRequestEventByFamilyAndId(UUID familyId, UUID id) {
        return jpaFamilyEventRepository.findPaymentRequestEventByFamilyIdAndEventId(familyId, id);
    }

    @Override
    public List<FamilyPayment> findPaymentRequestEventsByFamily(UUID familyId) {
        return jpaFamilyEventRepository.findPaymentRequestEventsByFamilyId(familyId);
    }
}
