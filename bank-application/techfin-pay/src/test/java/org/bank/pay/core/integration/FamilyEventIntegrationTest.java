package org.bank.pay.core.integration;

import org.bank.core.auth.AuthClaims;
import org.bank.pay.core.domain.familly.Family;
import org.bank.pay.core.domain.familly.MemberClaims;
import org.bank.pay.core.domain.familly.repository.FamilyEventReader;
import org.bank.pay.core.domain.familly.repository.FamilyEventStore;
import org.bank.pay.core.domain.familly.repository.FamilyReader;
import org.bank.pay.core.domain.familly.repository.FamilyStore;
import org.bank.pay.core.infrastructure.FamilyPurchaseEventClient;
import org.bank.pay.core.infrastructure.FollowerEventClient;
import org.bank.pay.core.unit.FamilyUnitTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles({"test"})
@ContextConfiguration(classes = FamilyEventIntegrationTest.Unit.class)
public class FamilyEventIntegrationTest extends FamilyUnitTest {

    @Configuration
    @Import(value = IntegrationTestInitializer.class)
    @ComponentScan(basePackages = {
            "org.bank.pay.core.event.family",
            "org.bank.pay.core.family",
            "org.bank.store.domain.pay",
            "org.bank.store.mysql.core.pay"
    })
    static class Unit { }

    @Autowired
    protected IntegrationTestInitializer integrationTestInitializer;
    @Autowired
    protected FamilyStore familyStore;
    @Autowired
    protected FamilyReader familyReader;
    @Autowired
    protected FamilyEventReader familyEventReader;
    @Autowired
    protected FamilyEventStore familyEventStore;

    @MockBean
    protected FamilyPurchaseEventClient familyPurchaseEventClient;
    @MockBean
    protected FollowerEventClient followerEventClient;

    protected Family find(AuthClaims leader) {
        return familyReader.findByUserIsLeader(leader).orElseThrow(IllegalArgumentException::new);
    }

    protected Family init(AuthClaims leader, AuthClaims follower) {
        integrationTestInitializer.init(leader);
        integrationTestInitializer.init(follower);

        Family family = new Family(leader);
        family.getParticipants().add(MemberClaims.of(follower));

        familyStore.saveFamily(family);
        return find(leader);
    }

    protected Family init(AuthClaims leader) {
        integrationTestInitializer.init(leader);
        familyStore.saveFamily(new Family(leader));
        return find(leader);
    }
}
