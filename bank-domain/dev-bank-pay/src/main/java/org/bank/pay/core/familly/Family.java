package org.bank.pay.core.familly;

import jakarta.persistence.*;
import lombok.Getter;
import org.bank.core.cash.Money;
import org.bank.pay.global.domain.DomainEntity;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Entity
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

    @Embedded
    private MemberClaims leader;

    @Embedded
    private Money famillyCredit;

    public void createFamilly(MemberClaims leader) {

        this.leader = leader;
        participants.add(leader);
        famillyCredit = new Money();
    }

    public void addMember(MemberClaims newMember) {
        try {
            participants.add(newMember);
            FamilyConstraints.validateParticipantsLimit(this);
        }catch (IllegalArgumentException e) {
            participants.remove(newMember);
            throw e;
        }
    }

    public void ejectMember(UUID memberId) {
        this.participants.remove(memberId);
    }

    public void changeLeader(MemberClaims newLeader) {
        FamilyConstraints.validateParticipantContaining(this, newLeader);
        this.leader = newLeader;
    }

    public void transferCashToFamily(Money cashToTransfer) {
        famillyCredit.deposit(cashToTransfer.getBalance());
        FamilyConstraints.validateCashRemaining(this, cashToTransfer);
    }

    public void transferCashFromFamily(Money cashToTransfer) {
        famillyCredit.withdraw(cashToTransfer.getBalance());
        FamilyConstraints.validateCashRemaining(this, cashToTransfer);
    }

    public void requestPaymentToLeader(Money amount) {

        FamilyConstraints.validateCashRemaining(this, amount);
        // 결제 요청 로직 (예시: 리더에게 알림을 보내거나, 결제 요청을 기록)
        System.out.println("패밀리 리더 " + leader.getUserid() + "에게 " + amount + " 결제를 요청했습니다.");
    }

    public void inviteMemberToFamily(MemberClaims leader, MemberClaims newMember) {

        isFamillyLeader(leader);
        FamilyConstraints.validateParticipantsLimit(this);
        participants.add(newMember);
    }

    // 패밀리 해체
    public void dissolveFamily() {
        participants.clear();
        deleteEntity();
    }

    private void isFamillyLeader(MemberClaims participant) {
        if(!this.leader.equals(participant)) {
            throw new IllegalStateException("패밀리 대표만 멤버를 초대할 수 있습니다.");
        }
    }

}
