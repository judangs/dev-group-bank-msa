package org.bank.pay.core.domain.familly;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bank.core.auth.AuthClaims;
import org.bank.pay.global.domain.DomainEntity;
import org.bank.pay.global.domain.card.CardType;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Table(name = "pay_family_tb")
@Entity
@NoArgsConstructor
public class Family extends DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "family_id", columnDefinition = "BINARY(16)")
    private UUID familyId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "pay_family_participant_tb", joinColumns = @JoinColumn(name = "family_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"family_id", "userid"})
    )
    @Cascade(CascadeType.ALL)
    private Set<MemberClaims> participants = new HashSet<>();

    @Setter
    @Embedded
    private MemberClaims leader;

    @OneToOne(cascade = jakarta.persistence.CascadeType.PERSIST)
    private FamilyCard familyCard;

    private void create(AuthClaims leader) {

        this.leader = MemberClaims.of(leader);
        participants.add(this.leader);
        familyCard.create(CardType.FAMILY);
    }

    public Family(AuthClaims leader) {
        familyCard = new FamilyCard();
        create(leader);
    }

    public MemberClaims find(String userId) {
        return participants.stream().filter(participant -> participant.getUserid().equals(userId))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("해당 사용자는 그룹원이 아닙니다."));
    }

    public int count() {
        return participants.size();
    }
}
