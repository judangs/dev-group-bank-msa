package org.bank.store.mysql.core.pay.owner.infrastructure;

import org.bank.pay.core.onwer.OwnerClaims;
import org.bank.pay.core.onwer.PayOwner;
import org.bank.pay.core.onwer.PaymentCard;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = OwnerRepositoryTest.TestConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OwnerRepositoryTest {

    @Configuration
    @ComponentScan(basePackages ={
            "org.bank.store.mysql.core.pay.config",
            "org.bank.store.mysql.core.pay.owner",
            "org.bank.store.mysql.core.pay.cash"
    })
    static class TestConfig {

    }

    private static OwnerClaims claims = new OwnerClaims("fixture", "name", "fixture@bank.com");
    private static PayOwner owner = new PayOwner(claims);

    @Autowired
    private OwnerRepository ownerRepository;

    @BeforeAll
    public void setUp() {
        owner.addPaymentCard(PaymentCard.of("first card", "1414 2000 1010 1000", "000", "12", LocalDate.of(2025, 03, 31)));
        owner.addPaymentCard(PaymentCard.of("second card", "1414 2000 1010 1001", "000", "12", LocalDate.of(2025, 03, 31)));
        owner.addPaymentCard(PaymentCard.of("third card", "1414 2000 1010 1002", "000", "12", LocalDate.of(2025, 03, 31)));
        ownerRepository.save(owner);
    }

    @Test
    @DisplayName("[PayOwner 조회] PayOwner 엔티티의 Claims와 동일한 정보를 데이터베이스에서 조회할 수 있습니다.")
    public void findByClaims_success() {
        Optional<PayOwner> owner = ownerRepository.findByUserClaims(claims);
        assertTrue(owner.isPresent());
    }

    @Test
    @DisplayName("[PayOwner 조회] PayOwner 엔티티에 등록된 결제 카드 정보를 데이터베이스에서 조회할 수 있습니다.")
    public void findPaymentCardsByClaims_success() {
        List<PaymentCard> cards = ownerRepository.findAllPaymentCardsByOwner(owner);
        assertEquals(3, cards.size());
    }

    @Test
    @DisplayName("[PayOwner 조회] PayOwner 엔티티에 등록된 결제 카드 정보 중 id가 일치하는 카드를 데이터베이스에서 조회할 수 있습니다.")
    public void findPaymentCardByClaimsAndId_success() {
        UUID paymentCardId = ownerRepository.findAllPaymentCardsByOwner(owner).get(0).getCardId();
        Optional<PaymentCard> card = ownerRepository.findPaymentCardByOwnerAndCard(owner, paymentCardId);
        assertTrue(card.isPresent());
    }
}