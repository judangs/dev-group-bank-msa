package org.bank.pay.core.familly;

import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.pay.core.domain.familly.Family;
import org.bank.pay.core.domain.familly.FamilyService;
import org.bank.pay.core.domain.familly.MemberClaims;
import org.bank.pay.core.domain.familly.repository.FamilyReader;
import org.bank.pay.core.domain.familly.repository.FamilyStore;
import org.bank.pay.core.domain.onwer.OwnerClaims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FamilyCashServiceTest {

    @Mock
    private FamilyReader familyReader;
    @Mock
    private FamilyStore familyStore;
    @Mock
    private FamilyService familyService;

    private final AuthClaims leader = new OwnerClaims("fixture-id", "fixture", "fixture@email.com");
    private final AuthClaims follower = new MemberClaims("follower", "follower", "follower@email.com");

    private Family family;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        familyService = new FamilyService(familyReader, familyStore);

        family = new Family(leader);

        lenient().doNothing().when(familyStore).saveFamily(any(Family.class));
        when(familyReader.findById(family.getFamilyId())).thenReturn(Optional.of(family));
    }

    @Test
    @DisplayName("패밀리 캐시를 충전합니다.")
    void 패밀리_캐시를_충전합니다() {

        Money amount = new Money(1000);

        familyService.depositCashToFamily(family.getFamilyId(), follower, amount);
        assertThat(family.getFamilyCredit()).isEqualTo(amount);
    }

    @Test
    @DisplayName("패밀리 캐시를 사용합니다.")
    void 패밀리_캐시를_사용합니다() {

        family.getFamilyCredit().deposit(new Money(20000));
        Money amount = new Money(10000);
        familyService.withdrawCashFromFamily(family.getFamilyId(), amount);
        assertThat(family.getFamilyCredit()).isEqualTo(new Money(10000));
    }
}
