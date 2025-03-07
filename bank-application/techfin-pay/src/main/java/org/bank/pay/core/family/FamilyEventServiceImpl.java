package org.bank.pay.core.family;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.core.dto.response.ResponseCodeV2;
import org.bank.core.payment.Product;
import org.bank.pay.core.domain.familly.Family;
import org.bank.pay.core.domain.familly.FamilyConstraints;
import org.bank.pay.core.domain.familly.service.FamilyPaymentService;
import org.bank.pay.core.domain.familly.service.FamilyService;
import org.bank.pay.core.domain.familly.MemberClaims;
import org.bank.pay.core.domain.owner.repository.PayOwnerReader;
import org.bank.pay.core.event.family.service.FamilyEventService;
import org.bank.pay.core.infrastructure.FamilyPurchaseEventClient;
import org.bank.pay.core.infrastructure.FollowerEventClient;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FamilyEventServiceImpl implements FamilyEventService {

    private final FamilyService familyService;
    private final FamilyPaymentService familyPaymentService;

    private final PayOwnerReader payOwnerReader;

    private final FollowerEventClient followerEventClient;
    private final FamilyPurchaseEventClient familyPurchaseEventClient;

    public ResponseCodeV2 invite(UUID familyId, AuthClaims leader, String userid) {

        try {
            Family family = familyService.isExist(familyId);
            FamilyConstraints.isEligibleForInvitation(family, MemberClaims.of(leader));

            payOwnerReader.findByUserId(userid)
                    .ifPresent(follower -> followerEventClient.invite(family, follower.getClaims()));

            return ResponseCodeV2.SUCCESS;

        } catch (IllegalArgumentException e) {
            return ResponseCodeV2.NOT_FOUND;
        }
    }

    @Override
    public ResponseCodeV2 order(UUID familyId, String userid, Product product) {
        try {
            Family family = familyService.isExist(familyId);
            MemberClaims from = family.find(userid);

            familyPurchaseEventClient.request(family, from, product);

            return ResponseCodeV2.SUCCESS;

        } catch (IllegalArgumentException e) {
            return ResponseCodeV2.NOT_FOUND;
        }
    }

    @Override
    public ResponseCodeV2 charge(AuthClaims user, UUID familyId, UUID cardId, Money amount) {
        try {
            Family family = familyService.isExist(familyId);
            MemberClaims from = family.find(user.getUserid());

            if(familyPaymentService.check(user, cardId).getCredit().isGreaterThan(amount)) {
                payOwnerReader.findPaymentCardByOwnerAndCard(user, cardId)
                        .ifPresent(card -> familyPurchaseEventClient.conversion(family, from, card, amount));

                return ResponseCodeV2.SUCCESS;
            }

            return ResponseCodeV2.FAIL;

        } catch (IllegalArgumentException e) {
            return ResponseCodeV2.NOT_FOUND;
        }
    }
}
