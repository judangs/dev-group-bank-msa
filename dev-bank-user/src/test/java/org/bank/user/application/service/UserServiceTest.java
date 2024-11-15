package org.bank.user.application.service;


import org.bank.user.application.service.fixture.TestFixtureProvider;
import org.bank.user.core.user.application.service.UserService;
import org.bank.user.core.user.domain.credential.RoleClassification;
import org.bank.user.core.user.domain.credential.UserCredential;
import org.bank.user.core.user.domain.profile.UserProfile;
import org.bank.user.dto.AccountRequest;
import org.bank.user.dto.ResponseDto;
import org.bank.user.dto.profile.ProfileSaveRequest;
import org.bank.user.global.exception.PermissionException;
import org.bank.user.global.response.ResponseMessage;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;


@Nested
@DisplayName("사용자 요구사항 테스트")
@TestPropertySource(
        locations = "/application-test.properties",
        properties = "spring.sql.init.mode=never")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class UserServiceTest {

    @Autowired
    private TestFixtureProvider testFixtureProvider;
    @Autowired
    private UserService userService;

    private UserProfile profileFixture;
    private UserCredential credentialFixture;

    private AccountRequest accountRequest;

    @BeforeAll
    void setUp() {

        profileFixture = testFixtureProvider.createProfileFixture("fixture", "fixture-email@bank.com");
        credentialFixture =  testFixtureProvider.createCredentialFixture("fixture", "fixture-id", "fixture-password");
        accountRequest = testFixtureProvider.createAccountRequestFixture("fixture", "fixture-id", "fixture-password", RoleClassification.UserRole.INDIVIDUAL);
    }


    @Test
    @DisplayName("사용자는 사용할 수 있는 계정 아이디를 조회할 수 있어야 한다.")
    public void existsAccount() {

        assertAll(
                "사용자 계정 아이디 조회에 대한 결과",
                () -> assertTrue(userService.existsAccount(credentialFixture.getUserid()), "잘못된 응답입니다."),
                () -> assertFalse(userService.existsAccount("incorrect-userid"), "잘못된 응답입니다.")
        );
    }

    @Deprecated
    @Test
    @DisplayName("사용자는 계정을 추가할 수 있어야 한다.")
    public void createAccount() {

        AccountRequest createAccountRequest =  testFixtureProvider.createAccountRequestFixture("new-fixture", "new-fixture-id", "new-fix-password", RoleClassification.UserRole.INDIVIDUAL);
        userService.createAccount(createAccountRequest);
        assertTrue(userService.existsAccount(createAccountRequest.getCredential().getUserid()));
    }

    @Test
    @DisplayName("일반 사용자는 권한 그룹('CUSTOMER') 타입의 계정만 생성할 수 있어야 한다. ")
    public void createInvalidRole() {

        AccountRequest invalidRequest = testFixtureProvider.createAccountRequestFixture("error-fixture", "error-fixture-id", "error-fixture-password", RoleClassification.UserRole.ADMIN);
        assertThrows(PermissionException.class, () -> userService.createAccount(invalidRequest));
    }

    @Test
    @DisplayName("로그인 중인 사용자는 사용자 정보를 변경할 수 있어야 한다.")
    public void editProfile() {

        ProfileSaveRequest modifyProfileRequest = ProfileSaveRequest.builder()
                .email("modify-fixture@bank.com")
                .build();

        ResponseDto response =  userService.editProfile(
                new AccountRequest(modifyProfileRequest, null),
                credentialFixture.getUserid()
        );
        assertAll(
                "사용자 이메일 정보 수정 결과",
                () -> assertEquals(ResponseMessage.SUCCESS, response.getCode()),
                () -> assertEquals("사용자 정보 변경이 완료되었습니다.", response.getMessage())
        );
    }

    @Test
    @DisplayName("로그인 중인 사용자는 사용자 계정을 탈퇴할 수 있어야 한다.")
    public void withdrawAccount() {


        ResponseDto response =  userService.withdrawAccount(credentialFixture.getUserid());
        assertAll(
                "사용자 계정 탈퇴 결과",
                () -> assertEquals(ResponseMessage.SUCCESS, response.getCode()),
                () -> assertEquals("사용자 계정 탈퇴가 완료되었습니다.", response.getMessage())
        );


    }

}