package org.bank.pay.core.familly.event;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.pay.core.familly.MemberClaims;
import org.bank.pay.core.familly.event.kafka.InviteEvent;
import org.bank.pay.global.domain.DomainEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@Table(name = "pay_family_invitation_tb")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class FamilyInvitation extends DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private UUID familyId;

    @Embedded
    private MemberClaims to;

    @Enumerated(EnumType.STRING)
    private FamilyEventStatus status;
    private LocalDateTime expiryDate;

    public static FamilyInvitation of(InviteEvent event) {
        return FamilyInvitation.builder()
                .familyId(event.getFamilyId())
                .to(event.getTo())
                .status(FamilyEventStatus.PENDING)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .build();
    }
}

