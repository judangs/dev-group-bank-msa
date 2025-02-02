package org.bank.user.core.account;

import org.bank.core.dto.response.ResponseCodeV2;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.user.core.facade.account.UserAccountRecoveryFacade;
import org.bank.user.core.fixture.UserClaimsFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(AccountRecoveryController.class)
class AccountRecoveryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserAccountRecoveryFacade userAccountRecoveryFacade;

    @Test
    @DisplayName("사용자 아이디 조회를 요청하고 인증 메일이 전송되었다는 응답을 받습니다")
    void 사용자_아이디_조회를_요청하고_인증_메일이_전송되었다는_응답을_받습니다() throws Exception {
        when(userAccountRecoveryFacade.findAccountID(any(String.class), any(String.class))).thenReturn(ResponseDtoV2.success(""));
        mockMvc.perform(post("/user/account/find-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", UserClaimsFixture.username())
                        .param("email", UserClaimsFixture.email())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCodeV2.SUCCESS.name()));
    }

    @Test
    @DisplayName("사용자 아이디 조회를 요청하고 등록된 사용자를 찾을 수 없다는 응답을 받습니다")
    void 사용자_아이디_조회를_요청하고_등록된_사용자를_찾을_수_없다는_응답을_받습니다() throws Exception {
        when(userAccountRecoveryFacade.findAccountID(any(String.class), any(String.class))).thenReturn(ResponseDtoV2.of(ResponseCodeV2.NOT_FOUND, ""));
        mockMvc.perform(post("/user/account/find-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", UserClaimsFixture.username())
                        .param("email", UserClaimsFixture.email())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCodeV2.NOT_FOUND.name()));
    }

    @Test
    @DisplayName("사용자 아이디 조회를 요청하고 부적잘한 요청이라는 응답을 받습니다")
    void 사용자_아이디_조회를_요청하고_부적절한_요청이라는_응답을_받습니다() throws Exception {
        when(userAccountRecoveryFacade.findAccountID(any(String.class), any(String.class))).thenReturn(ResponseDtoV2.fail(""));
        mockMvc.perform(post("/user/account/find-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", UserClaimsFixture.username())
                        .param("email", UserClaimsFixture.email())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCodeV2.FAIL.name()));
    }

    @Test
    @DisplayName("사용자 비밀번호 조회를 요청하고 인증 메일이 전송되었다는 응답을 받습니다")
    void 사용자_비밀번호_조회를_요청하고_인증_메일이_전송되었다는_응답을_받습니다() throws Exception {
        when(userAccountRecoveryFacade.findAccountPassword(any(String.class), any(String.class))).thenReturn(ResponseDtoV2.success(""));
        mockMvc.perform(post("/user/account/find-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userid", UserClaimsFixture.userid())
                        .param("email", UserClaimsFixture.email())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCodeV2.SUCCESS.name()));
    }

    @Test
    @DisplayName("사용자 비밀번호 조회를 요청하고 등록된 사용자를 찾을 수 없다는 응답을 받습니다")
    void 사용자_비밀번호_조회를_요청하고_등록된_사용자를_찾을_수_없다는_응답을_받습니다() throws Exception {
        when(userAccountRecoveryFacade.findAccountPassword(any(String.class), any(String.class))).thenReturn(ResponseDtoV2.of(ResponseCodeV2.NOT_FOUND, ""));
        mockMvc.perform(post("/user/account/find-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userid", UserClaimsFixture.userid())
                        .param("email", UserClaimsFixture.email())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCodeV2.NOT_FOUND.name()));
    }


    @Test
    @DisplayName("사용자 비밀번호 조회를 요청하고 부적잘한 요청이라는 응답을 받습니다")
    void 사용자_비밀번호_조회를_요청하고_부적절한_요청이라는_응답을_받습니다() throws Exception {
        when(userAccountRecoveryFacade.findAccountPassword(any(String.class), any(String.class))).thenReturn(ResponseDtoV2.fail(""));
        mockMvc.perform(post("/user/account/find-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userid", UserClaimsFixture.userid())
                        .param("email", UserClaimsFixture.email())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCodeV2.FAIL.name()));
    }

}