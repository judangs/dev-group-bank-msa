package org.bank.pay.core.family;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.core.kafka.KafkaEvent;
import org.bank.core.payment.Product;
import org.bank.pay.core.domain.familly.Family;
import org.bank.pay.core.domain.familly.FamilyConstraints;
import org.bank.pay.core.domain.familly.FamilyService;
import org.bank.pay.core.domain.familly.MemberClaims;
import org.bank.pay.core.event.family.kafka.CashConversionEvent;
import org.bank.pay.core.event.family.kafka.InviteEvent;
import org.bank.pay.core.event.family.kafka.PaymentEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FamilyEventFacade {

    private final KafkaTemplate<String, KafkaEvent> kafkaTemplate;
    private final FamilyService familyService;

    @Transactional
    public void inviteMember(UUID familyId, AuthClaims leader, AuthClaims follower) {
        Family family = familyService.isExist(familyId);

        FamilyConstraints.isEligibleForInvitation(family, MemberClaims.of(leader));
        kafkaTemplate.send("family.invitation", new InviteEvent(family.getFamilyId(), MemberClaims.of(follower)));
    }

    @Transactional
    public void requestPayment(UUID familyId, String userId, Product product) {
        Family family = familyService.isExist(familyId);
        MemberClaims from = family.find(userId);

        kafkaTemplate.send("family.payment", new PaymentEvent(family.getFamilyId(), from, Arrays.asList(product)));
    }

    @Transactional
    public void convertPersonalToFamilyCash(UUID familyId, String userId, Money amount) {
        Family family = familyService.isExist(familyId);
        MemberClaims from = family.find(userId);

        kafkaTemplate.send("family.cash.conversion", new CashConversionEvent(family.getFamilyId(), from, amount));
    }
}
