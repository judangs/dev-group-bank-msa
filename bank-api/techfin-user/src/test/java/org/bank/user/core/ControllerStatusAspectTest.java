package org.bank.user.core;

import org.bank.core.dto.response.ResponseCodeV2;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.user.core.account.AccountController;
import org.bank.user.core.fixture.ResponseEntityFixture;
import org.bank.user.dto.service.request.AccountRequest;
import org.bank.user.global.http.HttpResponseEntityStatusConverter;
import org.bank.user.global.http.WebConfiguration;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest({ControllerStatusAspect.class,  WebConfiguration.class, HttpResponseEntityStatusConverter.class})
@AutoConfigureMockMvc
class ControllerStatusAspectTest {

    @MockBean
    private AccountController accountController;


    @Test
    void 사용자_요청의_SUCCESS에_대해_200_코드를_반환합니다() {
        Answer<ResponseEntity<?>> answer = invocation -> {
            return ResponseEntityFixture.ok(ResponseDtoV2.success("성공"));
        };

        when(accountController.create(any(AccountRequest.class))).thenAnswer(answer);

        assertThat(accountController.create(new AccountRequest()).getStatusCode())
                .isEqualTo(HttpStatus.OK);
    }

    @Test
    void 사용자_요청의_FAIL에_대해_400_코드를_반환합니다() {
        Answer<ResponseEntity<?>> answer = invocation -> {
            return ResponseEntityFixture.ok(ResponseDtoV2.fail("실패"));
        };

        when(accountController.create(any(AccountRequest.class))).thenAnswer(answer);

        assertThat(accountController.create(new AccountRequest()).getStatusCode())
                .isEqualTo(HttpStatus.OK);
    }

    @Test
    void 사용자_요청의_INVALID_REQUEST에_대해_400을_반환합니다() {
        Answer<ResponseEntity<?>> answer = invocation -> {
            return ResponseEntityFixture.ok(ResponseDtoV2.of(ResponseCodeV2.INVALID_REQUEST, "잘못된 요청"));
        };

        when(accountController.create(any(AccountRequest.class))).thenAnswer(answer);

        assertThat(accountController.create(new AccountRequest()).getStatusCode())
                .isEqualTo(HttpStatus.OK);
    }

    @Test
    void 사용자_요청의_FORBIDDEN에_대해_403_코드를_반환합니다() {
        Answer<ResponseEntity<?>> answer = invocation -> {
            return ResponseEntityFixture.ok(ResponseDtoV2.forbidden("금지"));
        };

        when(accountController.create(any(AccountRequest.class))).thenAnswer(answer);

        assertThat(accountController.create(new AccountRequest()).getStatusCode())
                .isEqualTo(HttpStatus.OK);
    }

    @Test
    void 사용자_요청의_UNAUTHORIZED에_대해_401_코드를_반환합니다() {
        Answer<ResponseEntity<?>> answer = invocation -> {
            return ResponseEntityFixture.ok(ResponseDtoV2.unauthorized("권한 부족"));
        };

        when(accountController.create(any(AccountRequest.class))).thenAnswer(answer);

        assertThat(accountController.create(new AccountRequest()).getStatusCode())
                .isEqualTo(HttpStatus.OK);
    }

    @Test
    void 사용자_요청의_NOT_FOUND에_대해_404_코드를_반환합니다() {
        Answer<ResponseEntity<?>> answer = invocation -> {
            return ResponseEntityFixture.ok(ResponseDtoV2.of(ResponseCodeV2.NOT_FOUND, "페이지 없음"));
        };

        when(accountController.create(any(AccountRequest.class))).thenAnswer(answer);

        assertThat(accountController.create(new AccountRequest()).getStatusCode())
                .isEqualTo(HttpStatus.OK);
    }


    @Test
    void 사용자_요청의_DUPLICATE에_대해_409_코드를_반환합니다() {
        Answer<ResponseEntity<?>> answer = invocation -> {
            return ResponseEntityFixture.ok(ResponseDtoV2.of(ResponseCodeV2.DUPLICATE, "중복"));
        };

        when(accountController.create(any(AccountRequest.class))).thenAnswer(answer);

        assertThat(accountController.create(new AccountRequest()).getStatusCode())
                .isEqualTo(HttpStatus.OK);
    }
}