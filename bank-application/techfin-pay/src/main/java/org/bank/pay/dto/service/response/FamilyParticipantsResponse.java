package org.bank.pay.dto.service.response;

import lombok.Data;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.pay.core.domain.familly.MemberClaims;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
public class FamilyParticipantsResponse extends ResponseDtoV2 {

    private UUID familyId;
    private Set<MemberClaims> users = new HashSet<>();

    public FamilyParticipantsResponse(UUID familyId, Set<MemberClaims> users) {
        super();
        this.familyId = familyId;
        this.users.addAll(users);
    }
}
