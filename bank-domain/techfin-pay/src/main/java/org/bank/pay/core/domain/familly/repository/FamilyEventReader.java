package org.bank.pay.core.domain.familly.repository;

import org.bank.core.auth.AuthClaims;
import org.bank.pay.core.event.family.FamilyInvitation;
import org.bank.pay.core.event.family.FamilyPayment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FamilyEventReader {

    Optional<FamilyInvitation> findInvitationEventByUser(AuthClaims user);
    Optional<FamilyInvitation> findInvitationEventById(UUID id);
    Optional<FamilyPayment> findPaymentRequestEventByFamilyAndId(UUID familyId, UUID id);
    List<FamilyPayment> findPaymentRequestEventsByFamily(UUID familyId);
}
