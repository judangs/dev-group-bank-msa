package org.bank.pay.core.family;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.core.dto.response.ResponseCodeV2;
import org.bank.core.payment.Product;
import org.bank.pay.core.domain.familly.Family;
import org.bank.pay.core.domain.familly.FamilyConstraints;
import org.bank.pay.core.domain.familly.FamilyService;
import org.bank.pay.core.domain.familly.MemberClaims;
import org.bank.pay.core.domain.owner.repository.PayOwnerReader;
import org.bank.pay.core.producer.family.leader.FollowerEventPublisher;
import org.bank.pay.core.producer.family.payment.FamilyPurchaseEventPublisher;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FamilyEventFacade {

    private final FamilyService familyService;
    private final PayOwnerReader payOwnerReader;

    private final FollowerEventPublisher followerEventPublisher;
    private final FamilyPurchaseEventPublisher familyPurchaseEventPublisher;

    public ResponseCodeV2 invite(UUID familyId, AuthClaims leader, String userid) {

        try {
            Family family = familyService.isExist(familyId);
            FamilyConstraints.isEligibleForInvitation(family, MemberClaims.of(leader));

            payOwnerReader.findByUserId(userid)
                    .ifPresent(follower -> followerEventPublisher.invite(family, follower.getClaims()));

            return ResponseCodeV2.SUCCESS;

        } catch (IllegalArgumentException e) {
            return ResponseCodeV2.NOT_FOUND;
        }

    }

    public ResponseCodeV2 purchaseProductToFamilyLeader(UUID familyId, String userId, Product product) {

        try {
            Family family = familyService.isExist(familyId);
            MemberClaims from = family.find(userId);

            familyPurchaseEventPublisher.request(family, from, product);

            return ResponseCodeV2.SUCCESS;

        } catch (IllegalArgumentException e) {
            return ResponseCodeV2.NOT_FOUND;
        }
    }

    public ResponseCodeV2 convertPersonalCashToFamilyCash(AuthClaims user, UUID familyId, UUID cardId, Money amount) {

        try {
            Family family = familyService.isExist(familyId);
            MemberClaims from = family.find(user.getUserid());

            payOwnerReader.findPaymentCardByOwnerAndCard(user, cardId)
                    .ifPresent(card -> familyPurchaseEventPublisher.conversion(family, from, card, amount));

            return ResponseCodeV2.SUCCESS;

        } catch (EntityNotFoundException e) {
            return ResponseCodeV2.NOT_FOUND;
        }
    }
}
