package org.bank.pay.core.familly;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.pay.global.domain.DomainEntity;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
public class Family extends DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "family_id", columnDefinition = "BINARY(16)")
    private UUID familyId;

    @ElementCollection
    @CollectionTable(
            name = "participant", joinColumns = @JoinColumn(name = "family_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"userid"})
    )
    @Cascade(CascadeType.ALL)
    private Set<MemberClaims> participants = new HashSet<>();

    @Setter
    @Embedded
    private MemberClaims leader;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "balance", column = @Column(name = "credit", precision = 30, scale = 10))
    })
    private Money familyCredit;

    private void create(AuthClaims leader) {

        this.leader = MemberClaims.of(leader);
        participants.add(this.leader);
        familyCredit = new Money();
    }

    public Family(AuthClaims leader) {
        create(leader);
    }
}
