package org.bank.store.mysql.core.pay.cash.infrastructure;

import org.bank.pay.core.cash.ReservedCharge;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ReservedCashRepositoryTest.TestConfig.class)
class ReservedCashRepositoryTest {


    @Configuration
    @ComponentScan(basePackages ={
            "org.bank.store.mysql.core.pay.config",
            "org.bank.store.mysql.core.pay.cash"
    })
    static class TestConfig {

    }

    @Autowired @Qualifier("reservedCashCommandRepository")
    private ReservedCashCommandRepository reservedCashCommandRepository;

    @Autowired @Qualifier("reservedCashQueryRepository")
    private ReservedCashQueryRepository reservedCashQueryRepository;

    @Test
    @Order(1)
    @DisplayName("[예약 결제] 예약 결제와 관련된 정보를 저장할 수 있다.")
    void save_success() {

        ReservedCharge reservedCharge = new ReservedCharge();
        Assertions.assertDoesNotThrow(() -> reservedCashCommandRepository.save(reservedCharge));
    }

    @Test
    @Order(Integer.MAX_VALUE)
    @DisplayName("[예약 결제] 예약 결제와 관련된 정보를 삭제할 수 있다.")
    void deleteByScheduleId_success() {


        ReservedCharge reservedCharge = new ReservedCharge();
        reservedCashCommandRepository.save(reservedCharge);
        assertDoesNotThrow(() -> reservedCashCommandRepository.deleteByScheduledId(reservedCharge.getId()));
    }



    @Test
    @DisplayName("[예약 결제] 예약 결제와 관련된 정보를 모두 조회할 수 있다.")
    void findAllReservedCharges_success() {

        for(int idx=0; idx<50; idx++) {
            ReservedCharge reservedCharge = new ReservedCharge();
            Assertions.assertDoesNotThrow(() -> reservedCashCommandRepository.save(reservedCharge));
        }

        assertAll(
                () -> assertEquals(20, reservedCashQueryRepository.findAllReservedCharges(0).getContent().size()),
                () -> assertEquals(20, reservedCashQueryRepository.findAllReservedCharges(1).getContent().size()),
                () -> {
                    Page<ReservedCharge> lastReservedCharge = reservedCashQueryRepository.findAllReservedCharges(2);
                    assertEquals(10, lastReservedCharge.getContent().size());
                    assertFalse(lastReservedCharge.hasNext());
                }
        );
    }

    @Test
    @DisplayName("[예약 결제] 예약 결제의 id를 조회할 수 있다.")
    void findByScheduledId() {

        ReservedCharge reservedCharge = new ReservedCharge();
        reservedCashCommandRepository.save(reservedCharge);
        Optional<ReservedCharge> optionalReservedCharge = reservedCashQueryRepository.findByScheduledId(reservedCharge.getId());
        assertTrue(optionalReservedCharge.isPresent());
    }
}