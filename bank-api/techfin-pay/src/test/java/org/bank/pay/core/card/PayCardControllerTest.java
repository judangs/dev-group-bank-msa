package org.bank.pay.core.card;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bank.core.auth.AuthClaims;
import org.bank.core.auth.AuthConstants;
import org.bank.core.dto.response.ResponseCodeV2;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.pay.core.fixture.UserClaimsFixture;
import org.bank.pay.dto.service.request.PaymentCardRegisterRequest;
import org.bank.pay.global.http.HttpResponseEntityStatusConverter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest({
        PayCardSupportController.class, HttpResponseEntityStatusConverter.class
})
class PayCardControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CardFacade cardFacade;

    @Test
    void 사용자_결제_카드_등록을_요청하고_완료되었다는_응답을_받습니다() throws Exception {
        when(cardFacade.registerCard(any(AuthClaims.class), any(PaymentCardRegisterRequest.class)))
                .thenReturn(ResponseDtoV2.success("결제 카드 등록에 성공했습니다."));

        mockMvc.perform(post("/pay/card")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new PaymentCardRegisterRequest("1111-2222-3333-4444", "111", "12", LocalDate.now().plusYears(5), "카드 별칭")))
                .header(HttpHeaders.AUTHORIZATION, "access-token")
                .header(AuthConstants.HeaderField.X_AUTH_CLAIMS, UserClaimsFixture.header())
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(ResponseCodeV2.SUCCESS.name()));
    }

    @Test
    void 부적절한_사용자가_결제_카드_등록을_요청하고_인증에_실패했다는_응답을_받습니다() throws Exception {
        when(cardFacade.registerCard(any(AuthClaims.class), any(PaymentCardRegisterRequest.class)))
                .thenReturn(ResponseDtoV2.success("결제 카드 등록에 성공했습니다."));

        mockMvc.perform(post("/pay/card")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new PaymentCardRegisterRequest("1111-2222-3333-4444", "111", "12", LocalDate.now().plusYears(5), "카드 별칭")))
                        .header(HttpHeaders.AUTHORIZATION, "access-token")
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCodeV2.UNAUTHORIZED.name()));
    }

}