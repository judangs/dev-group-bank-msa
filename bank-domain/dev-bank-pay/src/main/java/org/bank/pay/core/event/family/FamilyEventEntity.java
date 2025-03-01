package org.bank.pay.core.event.family;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.bank.pay.global.domain.DomainEntity;

import java.util.UUID;

@Getter
@SuperBuilder
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "family-event-type")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public abstract class FamilyEventEntity extends DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    protected UUID id;

    protected UUID familyId;

    @Enumerated(EnumType.STRING)
    protected FamilyEventStatus status;

    public void accept() {
        status = FamilyEventStatus.ACCEPTED;
    }

    public void reject() {
        status = FamilyEventStatus.REJECTED;
    }

    public boolean isAccepted() {
        return status.equals(FamilyEventStatus.ACCEPTED);
    }

}
