package org.bank.pay.core.domain.familly.service;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.pay.core.domain.familly.Family;
import org.bank.pay.core.domain.familly.FamilyConstraints;
import org.bank.pay.core.domain.familly.MemberClaims;
import org.bank.pay.core.domain.familly.repository.FamilyReader;
import org.bank.pay.core.domain.familly.repository.FamilyStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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


    public boolean isLeader(AuthClaims leader, UUID familyId) {
        Family family = familyReader.findById(familyId).orElseThrow(IllegalArgumentException::new);
        return family.getLeader().equals(leader);
    }

    public Family getFamily(AuthClaims user) {
        return familyReader.findByContainUser(user).orElseThrow(IllegalArgumentException::new);
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
    public void ejectMemberFromFamily(AuthClaims leader, UUID familyId, String userid) {
        Family family = isExist(familyId);

        if(!family.getLeader().equals(leader)) {
            throw new IllegalArgumentException("패밀리 리더만이 멤버를 추방할 수 있습니다.");
        }

        Set<MemberClaims> participants = family.getParticipants();
        participants.remove(family.find(userid));

    }

    @Transactional
    public void changeFamilyLeader(AuthClaims leader, UUID familyId, String userid) {
        Family family = isExist(familyId);

        if(!family.getLeader().equals(leader)) {
            throw new IllegalArgumentException("패밀리 리더만이 멤버를 추방할 수 있습니다.");
        }

        FamilyConstraints.validateParticipantContaining(family, family.find(userid));
        family.setLeader(family.find(userid));
    }

    public Family isExist(UUID familyId) {
        Optional<Family> ownerFamily = familyReader.findById(familyId);
        if(ownerFamily.isEmpty()) {
            throw new IllegalArgumentException("생성된 패밀리가 없습니다.");
        }

        return ownerFamily.get();
    }



    // 4. 패밀리 결제 대행 요청
    // 5. 패밀리 대표의 패밀리 멤버 초대
}