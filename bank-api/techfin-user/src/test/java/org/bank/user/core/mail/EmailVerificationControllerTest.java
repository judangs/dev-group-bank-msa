package org.bank.user.core.mail;

import org.bank.core.dto.response.ResponseCodeV2;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.user.core.facade.mail.AccountMailFacade;
import org.bank.user.dto.service.response.AccountIdListResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@WebMvcTest(EmailVerificationController.class)
public class EmailVerificationControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountMailFacade accountMailFacade;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("인증 메일을 인증하고 사용자 계정을 생성했다는 응답을 받습니다")
    public void 인증_메일을_인증하고_사용자_계정을_생성했다는_응답을_받습니다() throws Exception {
        ResponseDtoV2 responseDto = ResponseDtoV2.success("사용자 계정 생성에 성공했습니다.");
        when(accountMailFacade.confirmAccountEmail(anyString())).thenReturn(responseDto);

        mockMvc.perform(get("/user/mail/confirm-mail")
                        .param("confirm", "token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCodeV2.SUCCESS.name()));
    }

    @Test
    @DisplayName("사용자 계정의 아이디 조회에 성공했다는 응답을 받습니다")
    public void testFindAccountInformation_Success_FindAccountId() throws Exception {
        List<String> userIdList = List.of("user1", "user2");
        AccountIdListResponse responseDto = AccountIdListResponse.builder()
                .code(ResponseCodeV2.SUCCESS)
                .message("사용자 계정의 아이디 조회에 성공했습니다.")
                .userid(userIdList)
                .completedAt(LocalDateTime.now())
                .build();
        when(accountMailFacade.confirmAccountEmail(anyString())).thenReturn(responseDto);

        mockMvc.perform(get("/user/mail/confirm-mail")
                        .param("confirm", "token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCodeV2.SUCCESS.name()))
                .andExpect(jsonPath("$.message").value("사용자 계정의 아이디 조회에 성공했습니다."))
                .andExpect(jsonPath("$.userid").isArray());
    }

    @Test
    @DisplayName("임시 패스워드 메일을 발송했다는 응답을 받습니다")
    public void testFindAccountInformation_Success_FindPassword() throws Exception {
        ResponseDtoV2 responseDto = ResponseDtoV2.success("임시 패스워드 메일을 발송했습니다.");
        when(accountMailFacade.confirmAccountEmail(anyString())).thenReturn(responseDto);

        mockMvc.perform(get("/user/mail/confirm-mail")
                        .param("confirm", "token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCodeV2.SUCCESS.name()))
                .andExpect(jsonPath("$.message").value("임시 패스워드 메일을 발송했습니다."));
    }

    @Test
    @DisplayName("잘못된 요청이라는 응답을 받습니다")
    public void testFindAccountInformation_Fail_InvalidRequest() throws Exception {
        ResponseDtoV2 responseDto = ResponseDtoV2.fail("잘못된 요청입니다.");
        when(accountMailFacade.confirmAccountEmail(anyString())).thenReturn(responseDto);

        mockMvc.perform(get("/user/mail/confirm-mail")
                        .param("confirm", "token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCodeV2.FAIL.name()))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."));
    }
}