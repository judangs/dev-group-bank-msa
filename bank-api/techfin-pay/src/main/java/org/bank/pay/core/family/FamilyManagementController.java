package org.bank.pay.core.family;


import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.auth.AuthenticatedUser;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.pay.dto.service.request.FamilyEventRequest;
import org.bank.pay.global.http.HttpResponseEntityStatusConverter;
import org.bank.pay.global.swagger.spec.FamilyManagerSwaggerSpec;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/pay/family")
@RequiredArgsConstructor
public class FamilyManagementController implements FamilyManagerSwaggerSpec {

    private final HttpResponseEntityStatusConverter httpResponseEntityStatusConverter;

    private final RoleBasedFamilyEventFacade roleBasedFamilyEventFacade;
    private final FamilyEventFacade familyEventFacade;
    private final FamilyFacade familyFacade;


    @GetMapping
    public ResponseEntity<? extends ResponseDtoV2> view(@AuthenticatedUser AuthClaims user) {
        ResponseDtoV2 response = familyFacade.viewMember(user);
        return httpResponseEntityStatusConverter.convert(response);
    }



    @PostMapping("/invitation")
    public ResponseEntity<? extends ResponseDtoV2> participate(@AuthenticatedUser AuthClaims follower,
                                                            @RequestBody FamilyEventRequest request) {
        ResponseDtoV2 response = roleBasedFamilyEventFacade.handleInvitation(follower, request);
        return httpResponseEntityStatusConverter.convert(response);
    }

    @PutMapping("/{familyId}/leader/{memberId}")
    public ResponseEntity<? extends ResponseDtoV2> delegate(@AuthenticatedUser AuthClaims leader,
                                                          @PathVariable UUID familyId,
                                                          @PathVariable String memberId) {
        ResponseDtoV2 response = familyFacade.changeLeader(leader, familyId, memberId);
        return httpResponseEntityStatusConverter.convert(response);
    }

    @PostMapping("/{familyId}/invitation/{userid}")
    public ResponseEntity<? extends ResponseDtoV2> invite(@AuthenticatedUser AuthClaims leader,
                                                          @PathVariable UUID familyId,
                                                          @PathVariable String userid) {
        ResponseDtoV2 response = familyEventFacade.invite(familyId, leader, userid);
        return httpResponseEntityStatusConverter.convert(response);
    }

    @DeleteMapping("/{familyId}/member/{memberId}")
    public ResponseEntity<? extends ResponseDtoV2> expel(@AuthenticatedUser AuthClaims leader,
                                                         @PathVariable UUID familyId,
                                                         @PathVariable String memberId) {
        ResponseDtoV2 response = familyFacade.ejectMember(leader, familyId, memberId);
        return httpResponseEntityStatusConverter.convert(response);
    }

}
