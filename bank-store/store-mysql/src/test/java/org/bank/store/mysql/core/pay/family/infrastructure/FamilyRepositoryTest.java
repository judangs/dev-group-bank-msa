package org.bank.store.mysql.core.pay.family.infrastructure;

import org.bank.pay.core.domain.familly.Family;
import org.bank.pay.core.domain.familly.MemberClaims;
import org.bank.pay.core.domain.onwer.OwnerClaims;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = FamilyRepositoryTest.TestConfig.class)
class FamilyRepositoryTest {

    @Configuration
    @ComponentScan(basePackages ={
            "org.bank.store.mysql.core.pay.config",
            "org.bank.store.mysql.core.pay.family"
    })
    static class TestConfig {

    }

    @Autowired @Qualifier("familyCommandRepository")
    private FamilyCommandRepository familyCommandRepository;

    @Autowired @Qualifier("familyQueryRepository")
    private FamilyQueryRepository familyQueryRepository;

    private static OwnerClaims owner;
    private static MemberClaims leader;
    private static Family family;

    @BeforeEach
    public void setUp() {

        owner = new OwnerClaims("fixture", "fixture", "fixture@bank.com");
        leader = MemberClaims.of(owner);

        family = new Family();
        family.createFamilly(leader);

        familyCommandRepository.saveFamily(family);
    }

    @AfterEach
    public void tearDown() {
        familyCommandRepository.deleteFamily(family);
    }

    @Test
    @DisplayName("[family 캐시] 캐시를 공유할 그룹을 저장합니다.")
    void save_success() {


        Family family = new Family();
        family.createFamilly(MemberClaims.of(new OwnerClaims("fixture2", "fixture", "fixture@bank.com")));

        assertDoesNotThrow(() -> familyCommandRepository.saveFamily(family));
    }

    @Test
    @DisplayName("[family 캐시] 캐시를 공유할 그룹을 찾을 수 있습니다.")
    void findById() {

        Optional<Family> optionalFamily = familyQueryRepository.findById(family.getFamilyId());
        assertTrue(optionalFamily.isPresent());
    }

    @Test
    @DisplayName("[family 캐시] 리더의 역할을 맡고 있는 그룹을 찾습니다.")
    void findByUserIsLeader() {
        Optional<Family> optionalFamily = familyQueryRepository.findByUserIsLeader(leader);
        assertTrue(optionalFamily.isPresent());
    }
}