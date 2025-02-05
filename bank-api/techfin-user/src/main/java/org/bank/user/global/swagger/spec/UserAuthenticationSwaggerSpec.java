package org.bank.user.global.swagger.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.bank.core.auth.AuthClaims;
import org.bank.user.dto.service.request.LoginRequest;
import org.bank.user.dto.service.response.LoginResponse;
import org.bank.user.global.swagger.SwaggerUserClaimsHeader;
import org.bank.user.global.swagger.data.AccountPayloadFixture;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "사용자 계정 인증 API 테스트")
public interface UserAuthenticationSwaggerSpec {

    @Operation(
            summary = "로그인",
            description = "사용자의 로그인 요청을 처리합니다. 로그인 성공 시 토큰을 반환합니다.",
            requestBody = @RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "로그인 요청 예시",
                                    value = AccountPayloadFixture.LOGIN_PAYLOAD
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = LoginResponse.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패")
            }
    )
    ResponseEntity<? super LoginResponse> login(LoginRequest request, HttpServletResponse httpServletResponse);

    @Operation(
            summary = "로그아웃",
            description = "사용자가 로그아웃 요청을 할 때 호출됩니다. 로그아웃 시 서버에서 토큰을 삭제하거나 무효화합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패")
            }
    )
    @SwaggerUserClaimsHeader
    ResponseEntity<? super LoginResponse> logout(AuthClaims user, String authorization);
}
