package org.bank.pay.core.familly;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.pay.core.familly.repository.FamilyReader;
import org.bank.pay.core.familly.repository.FamilyStore;
import org.bank.pay.core.onwer.OwnerClaims;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FamilyService {

    private final FamilyReader familyReader;
    private final FamilyStore familyStore;

    public void createFamily(OwnerClaims leaderClaims) {
        MemberClaims memberClaims = MemberClaims.of(leaderClaims);
        familyStore.saveFamily(memberClaims);
    }

    @Transactional
    public void addMemberToFamily(UUID familyId, AuthClaims newMember) {
        Family family = findById(familyId);
        family.addMember(MemberClaims.of(newMember));
        familyStore.saveFamily(family);
    }

    @Transactional
    public void ejectMemberFromFamily(UUID familyId, UUID memberId) {
        Family family = findById(familyId);
        family.ejectMember(memberId);
    }

    @Transactional
    public void changeFamilyLeader(UUID familyId, MemberClaims newLeader) {
        Family family = findById(familyId);
        family.changeLeader(newLeader);
    }

    @Transactional
    public void depositCashToFamily(UUID familyId, Money cashToTransfer) {
        Family family = findById(familyId);
        family.transferCashToFamily(cashToTransfer);
    }

    @Transactional
    public void withdrawCashFromFamily(UUID familyId, Money cashToTransfer) {
        Family family = findById(familyId);
        family.transferCashFromFamily(cashToTransfer);
    }

    private Family findById(UUID familyId) {
        Optional<Family> ownerFamily = familyReader.findById(familyId);
        if(ownerFamily.isEmpty()) {
            throw new IllegalArgumentException("생성된 패밀리가 없습니다.");
        }

        return ownerFamily.get();
    }

    // 4. 패밀리 결제 대행 요청
    // 5. 패밀리 대표의 패밀리 멤버 초대
}