package org.bank.pay.core.family;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.dto.response.ResponseDto;
import org.bank.pay.core.familly.FamilyService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FamilyFacade {

    private final FamilyService familyService;

    public ResponseDto addMember(UUID familyId, AuthClaims newMember) {
        familyService.addMemberToFamily(familyId, newMember);
        return ResponseDto.success("작업을 완료했습니다.");
    }

    public ResponseDto changeLeader(UUID familyId, UUID newLeaderId) {
        familyService.changeFamilyLeader(familyId, newLeaderId);
        return ResponseDto.success("작업을 완료했습니다.");
    }

    public ResponseDto ejectMember(UUID familyId, UUID memberId) {
        familyService.ejectMemberFromFamily(familyId, memberId);
        return ResponseDto.success("작업을 완료했습니다.");
    }


}
