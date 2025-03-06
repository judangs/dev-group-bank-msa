package org.bank.pay.global.swagger.spec;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bank.core.auth.AuthClaims;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.pay.dto.service.request.CardPaymentRequest;
import org.bank.pay.dto.service.request.UpdateCardAliasRequest;
import org.bank.pay.dto.service.response.PaymentCardsListResponse;
import org.bank.pay.global.swagger.annotation.ApiSpec;
import org.bank.pay.global.swagger.annotation.SwaggerPayCardPath;
import org.bank.pay.global.swagger.annotation.SwaggerUserClaimsHeader;
import org.bank.pay.global.swagger.data.PayCardApi;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "사용자 결제 카드 API 테스트")
public interface PayCardSupportSwaggerSpec {


    @ApiSpec(summary = "모든 결제 카드 조회", description = "사용자가 등록한 모든 결제 카드를 조회합니다.", responseSpec = PaymentCardsListResponse.class)
    @SwaggerUserClaimsHeader
    ResponseEntity<? extends ResponseDtoV2> card(AuthClaims user);



    @RequestBody(
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = @ExampleObject(
                            name = "등록 요청 예시",
                            value = PayCardApi.REGISTER_REQUEST
                    )
            )
    )
    @ApiSpec(summary = "카드 등록", description = "사용자가 서비스 이용 중에 사용할 카드를 등록합니다.")
    @SwaggerUserClaimsHeader
    ResponseEntity<? extends ResponseDtoV2> register(AuthClaims user, CardPaymentRequest request);


    @RequestBody(
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = @ExampleObject(
                            name = "카드 이름 변경 예시",
                            value = PayCardApi.ALIAS_REQUEST
                    )
            )
    )
    @ApiSpec(summary = "카드 이름 변경", description = "사용자가 사용하는 카드의 이름을 변경합니다.")
    @SwaggerPayCardPath
    @SwaggerUserClaimsHeader
    ResponseEntity<? extends ResponseDtoV2> alias(AuthClaims user, UUID cardId, UpdateCardAliasRequest request);


    @ApiSpec(summary = "카드 해지 요청", description = "사용자가 등록했던 카드를 해지합니다.")
    @SwaggerPayCardPath
    @SwaggerUserClaimsHeader
    ResponseEntity<? extends ResponseDtoV2> delete(AuthClaims user, UUID cardId);
}
