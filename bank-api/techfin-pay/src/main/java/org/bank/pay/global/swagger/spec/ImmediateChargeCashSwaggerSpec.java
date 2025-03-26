package org.bank.pay.global.swagger.spec;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bank.core.auth.AuthClaims;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.pay.dto.service.request.CashLimitRequest;
import org.bank.pay.dto.service.request.ChargeRequest;
import org.bank.pay.dto.service.response.CashBalanceResponse;
import org.bank.pay.dto.service.response.PaymentReserveResponse;
import org.bank.pay.global.swagger.annotation.ApiSpec;
import org.bank.pay.global.swagger.annotation.SwaggerPayCardPath;
import org.bank.pay.global.swagger.annotation.SwaggerUserClaimsHeader;
import org.bank.pay.global.swagger.data.PayCardApi;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "사용자 캐시 충전 API 테스트")
public interface ImmediateChargeCashSwaggerSpec {

    @ApiSpec(summary = "카드 잔고 확인", description = "소지하고 있는 결제 카드의 잔고를 확인합니다.", responseSpec = CashBalanceResponse.class)
    @SwaggerUserClaimsHeader
    ResponseEntity<? extends ResponseDtoV2> cash(@Parameter(hidden = true) AuthClaims user, @SwaggerPayCardPath UUID cardId);

    @ApiSpec(summary = "상품 구매(캐시 충전)", description = "서비스 상품을 구매합니다.", responseSpec = PaymentReserveResponse.class)
    @SwaggerUserClaimsHeader
    @RequestBody(
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = @ExampleObject(
                            name = "캐시 충전 예시",
                            value = PayCardApi.CHARGE_CASH_REQUEST
                    )
            )
    )
    ResponseEntity<? extends ResponseDtoV2> immediate(@Parameter(hidden = true) AuthClaims user, ChargeRequest request);

    @SwaggerUserClaimsHeader
    @RequestBody(
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = @ExampleObject(
                            name = "캐시 사용 제한 예시",
                            value = PayCardApi.LIMIT_CASH_REQUEST
                    )
            )
    )
    ResponseEntity<? extends ResponseDtoV2> limit(@Parameter(hidden = true) AuthClaims authClaims, @SwaggerPayCardPath UUID cardId, CashLimitRequest request);

}
