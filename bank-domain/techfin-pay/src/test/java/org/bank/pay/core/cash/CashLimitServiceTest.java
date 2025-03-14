package org.bank.pay.core.cash;


import org.bank.pay.core.domain.cash.Cash;
import org.bank.pay.core.domain.cash.service.CashLimitService;
import org.bank.pay.core.unit.CashUnitTest;
import org.bank.pay.fixture.CardFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;


public class CashLimitServiceTest extends CashUnitTest {


    @Autowired
    private CashLimitService cashLimitService;

    private static final BigDecimal MAX_PAYMENT_LIMIT = new BigDecimal("99999999999999999999.99");

    private final Cash cash = CardFixture.cashable().getCash();

    @BeforeEach
    void setup() {
        doNothing().when(cashStore).save(any(Cash.class));
    }

    @Test
    void 온라인_재화의_일일_사용량과_일회_사용량을_설정합니다() {
        cashLimitService.limit(cash, new BigDecimal(10000), new BigDecimal(10000));
        assertThat(cash.getLimits().getPerDaily()).isEqualTo(new BigDecimal(10000));
        assertThat(cash.getLimits().getPerOnce()).isEqualTo(new BigDecimal(10000));
    }

    @Test
    void 온라인_재화의_일일사용량과_일회_사용량을_초기화합니다() {
        cashLimitService.clear(cash);
        assertThat(cash.getLimits().getPerDaily()).isEqualTo(MAX_PAYMENT_LIMIT);
        assertThat(cash.getLimits().getPerOnce()).isEqualTo(MAX_PAYMENT_LIMIT);
    }

    @Test
    void 일일한도는_일회한도보다_크거나_같아야_합니다() {
        assertThatThrownBy(() -> cashLimitService.limit(cash, new BigDecimal(20000), new BigDecimal(10000)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("1일 한도는 1회 한도보다 크거나 같아야 합니다.");

    }

}
