package org.bank.store.mysql.core.pay.family;

import org.bank.core.auth.AuthClaims;
import org.bank.pay.core.event.family.FamilyEventEntity;
import org.bank.pay.core.event.family.FamilyInvitation;
import org.bank.pay.core.event.family.FamilyPayment;
import org.bank.store.mysql.global.jpa.JpaBaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaFamilyEventRepository extends JpaBaseRepository<FamilyEventEntity, UUID> {

    @Query("SELECT fi FROM FamilyInvitation fi WHERE fi.to.userid = :#{#user.userid}")
    Optional<FamilyInvitation> findInvitationEventByUser(@Param("user") AuthClaims user);

    @Query("SELECT fi FROM FamilyInvitation fi WHERE fi.id = :eventId")
    Optional<FamilyInvitation> findInvitationEventById(@Param("eventId") UUID eventId);

    @Query("SELECT fp FROM FamilyPayment fp WHERE fp.id = :eventId AND fp.familyId = :familyId")
    Optional<FamilyPayment> findPaymentRequestEventByFamilyIdAndEventId(@Param("familyId") UUID familyId, @Param("eventId") UUID eventId);

    @Query("SELECT fp FROM FamilyPayment fp WHERE fp.familyId = :familyId")
    List<FamilyPayment> findPaymentRequestEventsByFamilyId(@Param("familyId") UUID familyId);

}