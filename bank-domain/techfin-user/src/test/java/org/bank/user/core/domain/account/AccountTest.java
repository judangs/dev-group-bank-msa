package org.bank.user.core.domain.account;

import org.bank.core.common.Address;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountTest {

    @Test
    @DisplayName("사용자 프로파일과 계정을 생성합니다.")
    void 사용자_프로파일과_계정을_생성합니다() {
        Profile profile = new Profile();
        Credential credential = new Credential();

        profile.create(credential);
        assertThat(profile.getCredentials().contains(credential)).isTrue();
    }

    @Test
    @DisplayName("계정정보와 주민등록번호를 이용해 사용자 프로파일과 계정정보를 연동합니다")
    void 계정정보와_주민등록번호를_이용해_사용자_프로파일과_계정정보를_연동합니다() {
        Profile profile = new Profile();
        Credential credential = new Credential();
        String residentNumber = "000000-0000000";

        profile.initializeProfileWithResidentNumber(credential, residentNumber);
        assertThat(profile.getResidentNumber()).isEqualTo(residentNumber);
        assertThat(profile.getCredentials().contains(credential)).isTrue();
    }

    @Test
    @DisplayName("사용자 프로파일 정보를 교체합니다")
    void 사용자_프로파일_정보를_교체합니다() {
        Profile profile = new Profile();
        Profile otherProfile = Profile.builder()
                .address(new Address())
                .email("profile@email.com")
                .phone("010-0000-0000")
                .build();

        profile.replace(otherProfile);
        assertThat(profile.getAddress()).isNotNull();
        assertThat(profile.getPhone()).isNotNull();
        assertThat(profile.getPhone()).isNotNull();
    }

    @Test
    @DisplayName("계정을 탈퇴 처리합니다")
    void 계정을_탈퇴_처리합니다() {
        Credential credential = new Credential();
        credential.withdraw();
        assertThat(credential.isDeleted()).isEqualTo(true);
    }
}
