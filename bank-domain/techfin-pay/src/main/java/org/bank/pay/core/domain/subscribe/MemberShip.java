package org.bank.pay.core.domain.subscribe;

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
@Table(name = "membership")
public class MemberShip extends DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID membershipId;

    @Embedded
    private Billing payment;

    @Enumerated(EnumType.STRING)
    private MemberShipState state;

    private Instant enrollmentDate;
    private Instant terminateDate;

}
