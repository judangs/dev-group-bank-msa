package org.bank.user.core.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.bank.core.auth.AuthClaims;
import org.bank.core.auth.AuthConstants;
import org.bank.core.auth.AuthenticationException;
import org.bank.core.dto.response.ResponseCodeV2;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.user.core.facade.account.UserAccountManagementFacade;
import org.bank.user.core.fixture.UserClaimsFixture;
import org.bank.user.dto.service.request.AccountRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserAccountManagementFacade userAccountManagementFacade;


    @Test
    @DisplayName("사용자 프로파일과 계정 생성을 요청하고 완료되었다는 응답받습니다")
    void 사용자_프로파일과_계정_생성을_요청하고_완료되었다는_응답을_받습니다() throws Exception {
        when(userAccountManagementFacade.create(any(AccountRequest.class))).thenReturn(ResponseDtoV2.success(""));

        mockMvc.perform(post("/user/account/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(new AccountRequest()))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCodeV2.SUCCESS.name()));
    }

    @Test
    @DisplayName("사용자 프로파일과 계정 생성을 요청하고 금지되었다는 응답을 받습니다")
    void 사용자_프로파일과_계정_생성을_요청하고_금지되었다는_응답을_받습니다() throws Exception {
        when(userAccountManagementFacade.create(any(AccountRequest.class))).thenReturn(ResponseDtoV2.forbidden(""));

        mockMvc.perform(post("/user/account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AccountRequest()))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCodeV2.FORBIDDEN.name()));
    }

    @Test
    @DisplayName("사용자 프로파일과 계정 생성을 요청하고 실패되었다는 응답을 받습니다")
    void 사용자_프로파일과_계정_생성을_요청하고_실패되었다는_응답을_받습니다() throws Exception {
        when(userAccountManagementFacade.create(any(AccountRequest.class))).thenReturn(ResponseDtoV2.fail(""));

        mockMvc.perform(post("/user/account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AccountRequest()))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCodeV2.FAIL.name()));
    }

    @Test
    @DisplayName("사용자 프로파일을 수정을 요청하고 완료되었다는 응답을 받습니다")
    void 사용자_프로파일의_수정을_요청하고_완료되었다는_응답을_받습니다() throws Exception {
        when(userAccountManagementFacade.edit(any(AccountRequest.class), any(AuthClaims.class))).thenReturn(ResponseDtoV2.success(""));

        mockMvc.perform(put("/user/account/edit")
                        .header(AuthConstants.HeaderField.X_AUTH_CLAIMS, UserClaimsFixture.header())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AccountRequest()))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCodeV2.SUCCESS.name()));
    }

    @Test
    @DisplayName("사용자 프로파일을 수정을 요청하고 권한이 부족하다는 응답을 받습니다")
    void 사용자_프로파일의_수정을_요청하고_권한이_부족하다는_응답을_받습니다() throws Exception {
        when(userAccountManagementFacade.edit(any(AccountRequest.class), any(AuthClaims.class))).thenReturn(ResponseDtoV2.unauthorized(""));

        mockMvc.perform(put("/user/account/edit")
                        .header(AuthConstants.HeaderField.X_AUTH_CLAIMS, UserClaimsFixture.header())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AccountRequest()))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCodeV2.UNAUTHORIZED.name()));
    }

    @Test
    @DisplayName("사용자 프로파일을 수정을 요청하고 실패되었다는 응답을 받습니다")
    void 사용자_프로파일의_수정을_요청하고_실패되었다는_응답을_받습니다() throws Exception {
        when(userAccountManagementFacade.edit(any(AccountRequest.class), any(AuthClaims.class))).thenReturn(ResponseDtoV2.fail(""));

        mockMvc.perform(put("/user/account/edit")
                        .header(AuthConstants.HeaderField.X_AUTH_CLAIMS, UserClaimsFixture.header())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AccountRequest()))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCodeV2.FAIL.name()));
    }

    @Test
    @DisplayName("사용자 프로파일을 수정을 요청하고 인증이_필요하다는 응답을 받습니다")
    void 사용자_프로파일의_수정을_요청하고_인증이_필요하다는_응답을_받습니다() throws Exception {
        when(userAccountManagementFacade.edit(any(AccountRequest.class), any(AuthClaims.class))).thenReturn(ResponseDtoV2.fail(""));

        assertThatThrownBy(() ->
            mockMvc.perform(put("/user/account/edit")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(new AccountRequest()))
            )
        )
        .isInstanceOf(ServletException.class)
        .hasCauseInstanceOf(AuthenticationException.class);
    }


    @Test
    @DisplayName("회원 탈퇴를 요청하고 성공했다는 응답을 받습니다")
    void 회원_탈퇴를_요청하고_성공했다는_응답을_받습니다() throws Exception {
        when(userAccountManagementFacade.withdraw(any(AuthClaims.class))).thenReturn(ResponseDtoV2.success(""));

        mockMvc.perform(delete("/user/account/withdraw")
                        .header(AuthConstants.HeaderField.X_AUTH_CLAIMS, UserClaimsFixture.header())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCodeV2.SUCCESS.name()));
    }

    @Test
    @DisplayName("회원 탈퇴를 요청하고 요청사항이 권한이 없다는 응답을 받습니다")
    void 회원_탈퇴를_요청하고_권한이_없다는_응답을_받습니다() throws Exception {
        when(userAccountManagementFacade.withdraw(any(AuthClaims.class))).thenReturn(ResponseDtoV2.unauthorized(""));

        mockMvc.perform(delete("/user/account/withdraw")
                        .header(AuthConstants.HeaderField.X_AUTH_CLAIMS, UserClaimsFixture.header())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCodeV2.UNAUTHORIZED.name()));
    }

    @Test
    @DisplayName("회원 탈퇴를 요청하고 실패되었다는 응답을 받습니다")
    void 회원_탈퇴를_요청하고_실패되었다는_응답을_받습니다() throws Exception {
        when(userAccountManagementFacade.withdraw(any(AuthClaims.class))).thenReturn(ResponseDtoV2.fail(""));

        mockMvc.perform(delete("/user/account/withdraw")
                        .header(AuthConstants.HeaderField.X_AUTH_CLAIMS, UserClaimsFixture.header())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCodeV2.FAIL.name()));
    }

    @Test
    @DisplayName("회원 탈퇴를 요청하고 인증이_필요하다는 응답을 받습니다")
    void 회원_탈퇴를_요청하고_인증이_필요하다는_응답을_받습니다() throws Exception {
        when(userAccountManagementFacade.withdraw(any(AuthClaims.class))).thenReturn(ResponseDtoV2.unauthorized(""));

        assertThatThrownBy(() -> mockMvc.perform(delete("/user/account/withdraw")))
                .isInstanceOf(ServletException.class)
                        .hasCauseInstanceOf(AuthenticationException.class);
    }
}