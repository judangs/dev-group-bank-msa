package org.bank.user.global.swagger.spec;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bank.core.auth.AuthClaims;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.user.dto.service.request.AccountRequest;
import org.bank.user.dto.service.response.AccountIdListResponse;
import org.bank.user.global.swagger.SwaggerUserClaimsHeader;
import org.bank.user.global.swagger.data.AccountPayloadFixture;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "사용자 회원 및 계정 API 호출 테스트")
public interface UserAccountSwaggerSpec {

    @Operation(
            requestBody = @RequestBody(
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(name = "회원 가입", value = AccountPayloadFixture.CREATE_PAYLOAD)
                                    }
                            )
                    }
            )
    )
    ResponseEntity<? super AccountIdListResponse> create(AccountRequest accountRequest);

    @Operation(
            requestBody = @RequestBody(
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(name = "회원 정보 수정", value = AccountPayloadFixture.EDIT_PAYLOAD)
                                    }
                            )
                    }
            )
    )
    @SwaggerUserClaimsHeader
    ResponseEntity<? extends ResponseDtoV2> edit(AccountRequest accountRequest, AuthClaims user);

    @Operation(
            requestBody = @RequestBody(
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(name = "회원 탈퇴", value = AccountPayloadFixture.EDIT_PAYLOAD)
                                    }
                            )
                    }
            )
    )
    @SwaggerUserClaimsHeader
    ResponseEntity<? extends ResponseDtoV2> withdraw(AuthClaims user);


}

