package org.bank.pay.core.event.family;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.bank.pay.core.domain.familly.MemberClaims;
import org.bank.pay.core.event.family.kafka.InviteEvent;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@SuperBuilder
@Table(name = "pay_family_invitation_tb")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class FamilyInvitation extends FamilyEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Embedded
    private MemberClaims to;

    @Enumerated(EnumType.STRING)
    private FamilyEventStatus status;
    private LocalDateTime expiryDate;

    public boolean isExpired() {
        if(LocalDateTime.now().isAfter(this.expiryDate)) {
            status = FamilyEventStatus.EXPIRED;
            return true;
        }
        return false;
    }

    public static FamilyInvitation of(InviteEvent event) {
        return FamilyInvitation.builder()
                .familyId(event.getFamilyId())
                .to(event.getTo())
                .status(FamilyEventStatus.PENDING)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .build();
    }
}

