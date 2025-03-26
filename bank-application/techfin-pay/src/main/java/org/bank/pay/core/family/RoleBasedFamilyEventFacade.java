package org.bank.pay.core.family;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.pay.core.domain.familly.repository.FamilyEventReader;
import org.bank.pay.core.domain.familly.service.FamilyService;
import org.bank.pay.core.event.family.FamilyInvitation;
import org.bank.pay.core.event.family.FamilyPayment;
import org.bank.pay.core.infrastructure.FamilyPurchaseEventClient;
import org.bank.pay.dto.service.request.FamilyEventRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class  RoleBasedFamilyEventFacade {

    private final FamilyService familyService;
    private final FamilyPurchaseEventClient familyPurchaseEventClient;

    private final FamilyEventReader familyEventReader;

    @Transactional
    public ResponseDtoV2 handleInvitation(AuthClaims follower, FamilyEventRequest request) {

        FamilyInvitation invitation = familyEventReader.findInvitationEventByUser(follower)
                .orElseThrow(IllegalArgumentException::new);

        if(invitation.isExpired()) {
            return ResponseDtoV2.fail("요청 시간이 만료되었습니다.");
        }

        ResponseDtoV2 response = switch (request.getDecision()) {

            case ACCEPT -> {
                invitation.accept();
                familyService.addMemberToFamily(request.getFamilyId(), follower);
                yield ResponseDtoV2.success("패밀리에 초대되었습니다.");
            }
            case REJECT -> {
                invitation.reject();
                yield ResponseDtoV2.success("패밀리 초대를 거절했습니다.");
            }
        };

        return response;
    }

    @Transactional
    public ResponseDtoV2 handlePayment(AuthClaims leader, FamilyEventRequest request) {

        FamilyPayment payment = familyEventReader.findPaymentRequestEventByFamilyAndId(request.getFamilyId(), request.getPaymentId())
                .orElseThrow(IllegalAccessError::new);

        if(familyService.isLeader(leader, request.getFamilyId()) && payment.isPending()) {

            ResponseDtoV2 response = switch (request.getDecision()) {
                case ACCEPT -> {
                    payment.accept();
                    familyPurchaseEventClient.approval(payment);
                    yield ResponseDtoV2.success("결제를 승인했습니다.");
                }
                case REJECT -> {
                    payment.accept();
                    yield ResponseDtoV2.success("결제 요청을 거절했습니다.");
                }
            };

            return response;
        }

        return ResponseDtoV2.fail("유효하지 않은 결제 요청입니다.");
    }
}
