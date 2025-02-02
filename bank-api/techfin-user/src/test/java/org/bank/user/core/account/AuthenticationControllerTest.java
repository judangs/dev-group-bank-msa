package org.bank.user.core.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bank.core.auth.AuthClaims;
import org.bank.core.auth.AuthConstants;
import org.bank.core.dto.response.ResponseCodeV2;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.user.core.facade.account.UserAccountAuthenticationFacade;
import org.bank.user.core.fixture.UserClaimsFixture;
import org.bank.user.dto.service.request.LoginRequest;
import org.bank.user.dto.service.response.LoginResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(AuthenticationController.class)
@ComponentScan(basePackages = "org.bank.user.global.resolver")
class AuthenticationControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserAccountAuthenticationFacade userAccountAuthenticationFacade;

    @Test
    @DisplayName("사용자 로그인을 요청하고 토큰과 함께 완료되었다는 응답받습니다")
    void 사용자_로그인을_요청하고_토큰과_함께_완료되었다는_응답받습니다() throws Exception {
        when(userAccountAuthenticationFacade.login(any(String.class), any(String.class))).thenReturn(LoginResponse.success("access-token"));
        mockMvc.perform(post("/user/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest(UserClaimsFixture.userid(), UserClaimsFixture.password())))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCodeV2.SUCCESS.name()))
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION));
    }

    @Test
    @DisplayName("사용자 로그인을 요청하고 인증에 실패했다는 응답받습니다")
    void 사용자_로그인을_요청하고_인증에_실패했다는_응답받습니다() throws Exception {
        when(userAccountAuthenticationFacade.login(any(String.class), any(String.class))).thenReturn(ResponseDtoV2.unauthorized(""));
        mockMvc.perform(post("/user/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest(UserClaimsFixture.userid(), UserClaimsFixture.password())))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCodeV2.UNAUTHORIZED.name()));
    }

    @Test
    @DisplayName("사용자 로그아웃을 요청하고 완료되었다는 응답을 받습니다")
    void 사용자_로그아웃을_요청하고_완료되었다는_응답을_받습니다() throws Exception {
        doNothing().when(userAccountAuthenticationFacade).logout(any(AuthClaims.class), any(String.class));
        mockMvc.perform(post("/user/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "access-token")
                        .header(AuthConstants.HeaderField.X_AUTH_CLAIMS, UserClaimsFixture.header())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCodeV2.SUCCESS.name()));
    }
}