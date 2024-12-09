package org.bank.pay.core.familly;

import jakarta.persistence.*;
import lombok.Getter;
import org.bank.core.cash.Money;
import org.bank.pay.global.domain.DomainEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Entity
public class Familly extends DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "familly_id", columnDefinition = "BINARY(16)")
    private UUID famillyId;

    @ElementCollection
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
            FamillyConstraints.validateParticipantsLimit(this);
        }catch (IllegalArgumentException e) {
            participants.remove(newMember);
            throw e;
        }
    }

    public void changeLeader(MemberClaims newLeader) {
        FamillyConstraints.validateParticipantContaining(this, newLeader);
        this.leader = newLeader;
    }

    public void transferCashToFamilyCash(Money cashToTransfer) {
        FamillyConstraints.validateCashRemaining(this, cashToTransfer);
        this.famillyCredit.getCredit().add(cashToTransfer.getCredit());
    }

    public void requestPaymentToLeader(Money amount) {

        FamillyConstraints.validateCashRemaining(this, amount);
        // 결제 요청 로직 (예시: 리더에게 알림을 보내거나, 결제 요청을 기록)
        System.out.println("패밀리 리더 " + leader.getUserid() + "에게 " + amount + " 결제를 요청했습니다.");
    }

    public void inviteMemberToFamily(MemberClaims leader, MemberClaims newMember) {

        isFamillyLeader(leader);
        FamillyConstraints.validateParticipantsLimit(this);
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
