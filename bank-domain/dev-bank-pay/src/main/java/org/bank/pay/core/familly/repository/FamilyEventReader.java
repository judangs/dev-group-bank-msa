package org.bank.pay.core.familly.repository;

import org.bank.pay.core.familly.event.kafka.InviteEvent;

import java.util.Optional;
import java.util.UUID;

public interface FamilyEventReader {

    Optional<InviteEvent> findInvitationEventById(UUID id);
}
