package org.bank.pay.core.subscribe;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.pay.global.domain.DomainEntity;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "membership")
public class MemberShip extends DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "membership_id", columnDefinition = "BINARY(16)")
    private UUID membershipId;

    @Embedded
    private Billing payment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberShipState state;

    private Instant enrollmentDate;
    private Instant terminateDate;

}
