package org.bank.pay.core.family;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.pay.core.domain.familly.Family;
import org.bank.pay.core.domain.familly.service.FamilyService;
import org.bank.pay.dto.service.response.FamilyParticipantsResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FamilyFacade {
    private final FamilyService familyService;

    public ResponseDtoV2 viewMember(AuthClaims user) {
        try {
            Family family = familyService.getFamily(user);
            return new FamilyParticipantsResponse(family.getFamilyId(), family.getParticipants());
        } catch (IllegalArgumentException e) {
            return ResponseDtoV2.fail("패밀리를 찾을 수 없습니다.");
        }
    }

    public ResponseDtoV2 addMember(UUID familyId, AuthClaims newMember) {
        familyService.addMemberToFamily(familyId, newMember);
        return ResponseDtoV2.success("작업을 완료했습니다.");
    }

    public ResponseDtoV2 changeLeader(AuthClaims leader, UUID familyId, String userid) {
        familyService.changeFamilyLeader(leader, familyId, userid);
        return ResponseDtoV2.success("작업을 완료했습니다.");
    }

    public ResponseDtoV2 ejectMember(AuthClaims leader, UUID familyId, String userid) {
        familyService.ejectMemberFromFamily(leader, familyId, userid);
        return ResponseDtoV2.success("작업을 완료했습니다.");
    }


}
