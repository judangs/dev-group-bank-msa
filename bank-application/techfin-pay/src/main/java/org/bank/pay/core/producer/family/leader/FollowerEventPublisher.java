package org.bank.pay.core.producer.family.leader;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.kafka.KafkaEvent;
import org.bank.pay.core.domain.familly.Family;
import org.bank.pay.core.domain.familly.MemberClaims;
import org.bank.pay.core.infrastructure.FollowerEventClient;
import org.bank.pay.core.event.family.kafka.InviteEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FollowerEventPublisher implements FollowerEventClient {

    private final String INVITATION_TOPIC = "family.invitation";

    private final KafkaTemplate<String, KafkaEvent> kafkaTemplate;

    @Async
    public void invite(Family family, AuthClaims follower) {
        kafkaTemplate.send(INVITATION_TOPIC, new InviteEvent(family.getFamilyId(), MemberClaims.of(follower)));
    }
}
