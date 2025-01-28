package org.bank.pay.core.familly;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.pay.core.familly.repository.FamilyReader;
import org.bank.pay.core.familly.repository.FamilyStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FamilyService {

    private final FamilyReader familyReader;
    private final FamilyStore familyStore;

    @Transactional
    public Family createFamily(AuthClaims claims) {
        MemberClaims leader = MemberClaims.of(claims);
        Family family = new Family(leader);
        familyStore.saveFamily(family);

        return family;
    }

    @Transactional
    public void addMemberToFamily(UUID familyId, AuthClaims member) {
        Family family = isExist(familyId);
        Set<MemberClaims> participants = family.getParticipants();

        try {
            FamilyConstraints.validateParticipantsLimit(family);
            participants.add(MemberClaims.of(member));
        }catch (IllegalArgumentException e) {
            participants.remove(MemberClaims.of(member));
            throw e;
        }
    }

    @Transactional
    public void ejectMemberFromFamily(UUID familyId, AuthClaims member) {
        Family family = isExist(familyId);

        Set<MemberClaims> participants = family.getParticipants();
        participants.remove(member);
    }

    @Transactional
    public void changeFamilyLeader(UUID familyId, AuthClaims newLeader) {
        Family family = isExist(familyId);

        Set<MemberClaims> participants = family.getParticipants();
        MemberClaims leader = participants.stream().filter(participant -> participant.equals(newLeader))
                .findFirst().orElseThrow(NoSuchElementException::new);
        FamilyConstraints.validateParticipantContaining(family, leader);

        family.setLeader(leader);
    }

    @Transactional
    public void depositCashToFamily(UUID familyId, AuthClaims from, Money cash) {

        Money familyCredit = isExist(familyId).getFamilyCredit();
        familyCredit.deposit(cash);
    }

    @Transactional
    public void withdrawCashFromFamily(UUID familyId, Money cashToTransfer) {
        Family family = isExist(familyId);
        Money familyCredit = family.getFamilyCredit();

        familyCredit.withdraw(cashToTransfer);
    }

    public Family isExist(UUID familyId) {
        Optional<Family> ownerFamily = familyReader.findById(familyId);
        if(ownerFamily.isEmpty()) {
            throw new EntityNotFoundException("생성된 패밀리가 없습니다.");
        }

        return ownerFamily.get();
    }

    // 4. 패밀리 결제 대행 요청
    // 5. 패밀리 대표의 패밀리 멤버 초대
}