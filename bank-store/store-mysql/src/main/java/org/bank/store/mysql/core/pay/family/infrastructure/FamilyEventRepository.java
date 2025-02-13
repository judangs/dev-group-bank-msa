package org.bank.store.mysql.core.pay.family.infrastructure;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.domain.DomainNames;
import org.bank.pay.core.domain.familly.repository.FamilyEventReader;
import org.bank.pay.core.domain.familly.repository.FamilyEventStore;
import org.bank.pay.core.event.family.FamilyInvitation;
import org.bank.pay.core.event.family.FamilyPayment;
import org.bank.store.source.DataSourceType;
import org.bank.store.source.NamedRepositorySource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@NamedRepositorySource(domain = DomainNames.PAY, type = DataSourceType.READWRITE)
@RequiredArgsConstructor
public class FamilyEventRepository implements FamilyEventStore, FamilyEventReader {

    @PersistenceContext(unitName = "payEntityManagerFactory")
    private EntityManager entityManager;

    @Override
    @Transactional
    public void store(FamilyInvitation invitation) {
        if (invitation.getId() == null) {
            entityManager.persist(invitation);
        } else {
            entityManager.merge(invitation);
        }
    }

    @Override
    @Transactional
    public void store(FamilyPayment paymentRequest) {
        if (paymentRequest.getId() == null) {
            entityManager.persist(paymentRequest);
        } else {
            entityManager.merge(paymentRequest);
        }
    }

    @Override
    public Optional<FamilyInvitation> findInvitationEventByUser(AuthClaims user) {

        String jpql = "SELECT fi FROM FamilyInvitation fi WHERE fi.to = :user";

        TypedQuery<FamilyInvitation> query = entityManager.createQuery(jpql, FamilyInvitation.class);
        query.setParameter("user", user);

        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public Optional<FamilyInvitation> findInvitationEventById(UUID id) {
        FamilyInvitation familyInvitation = entityManager.find(FamilyInvitation.class, id);
        return Optional.ofNullable(familyInvitation);
    }

    @Override
    public Optional<FamilyPayment> findPaymentRequestEventByFamilyAndId(UUID familyId, UUID id) {
        String jpql = "SELECT fp FROM FamilyPayment fp WHERE fp.id = :id AND fp.familyId = :familyId" ;

        TypedQuery<FamilyPayment> query = entityManager.createQuery(jpql, FamilyPayment.class);
        query.setParameter("familyId", familyId);
        query.setParameter("id", id);

        return Optional.ofNullable(query.getSingleResult());
    }
}
