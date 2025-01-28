package org.bank.store.mysql.core.pay.family.infrastructure;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.bank.pay.core.familly.event.FamilyInvitation;
import org.bank.pay.core.familly.event.FamilyPaymentRequest;
import org.bank.pay.core.familly.repository.FamilyEventStore;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class FamilyEventRepository implements FamilyEventStore {

    @PersistenceContext
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
    public void store(FamilyPaymentRequest paymentRequest) {
        if (paymentRequest.getId() == null) {
            entityManager.persist(paymentRequest);
        } else {
            entityManager.merge(paymentRequest);
        }
    }
}
