package org.bank.pay.core.family;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.bank.core.cash.Money;
import org.bank.core.kafka.KafkaEvent;
import org.bank.core.payment.Product;
import org.bank.pay.core.familly.Family;
import org.bank.pay.core.familly.MemberClaims;
import org.bank.pay.core.familly.kafka.event.CashConversionEvent;
import org.bank.pay.core.familly.kafka.event.InviteEvent;
import org.bank.pay.core.familly.kafka.event.RequestPaymentEvent;
import org.bank.store.mysql.core.pay.family.infrastructure.FamilyQueryRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FamilyEventFacade {

    private final FamilyQueryRepository familyQueryRepository;
    private final KafkaTemplate<String, KafkaEvent> kafkaTemplate;

    @Transactional
    public void inviteMember(UUID familyId, MemberClaims follower) {
        Family family = familyQueryRepository.findById(familyId)
                .orElseThrow(() -> new EntityNotFoundException("family를 찾을 수 없습니다."));

        kafkaTemplate.send("family.invitation", new InviteEvent(family.getFamilyId(), follower));
    }

    @Transactional
    public void requestPayment(UUID familyId, String userId, Product product) {
        Family family = familyQueryRepository.findById(familyId)
                .orElseThrow(() -> new EntityNotFoundException("Family not found"));

        MemberClaims from = family.getParticipants().stream().filter(participant -> participant.getUserid().equals(userId))
                        .findFirst().orElseThrow(() -> new EntityNotFoundException("해당 사용자는 그룹원이 아닙니다."));

        kafkaTemplate.send("family.payment", new RequestPaymentEvent(family.getFamilyId(), from, product));
    }

    @Transactional
    public void convertPersonalToFamilyCash(UUID familyId, String userId, Money amount) {
        Family family = familyQueryRepository.findById(familyId)
                .orElseThrow(() -> new EntityNotFoundException("Family not found"));

        MemberClaims from = family.getParticipants().stream().filter(participant -> participant.getUserid().equals(userId))
                .findFirst().orElseThrow(() -> new EntityNotFoundException("해당 사용자는 그룹원이 아닙니다."));

        kafkaTemplate.send("family.cash.conversion", new CashConversionEvent(family.getFamilyId(), from, amount));
    }
}
