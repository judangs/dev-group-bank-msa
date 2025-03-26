package org.bank.pay.global.swagger.spec;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bank.core.auth.AuthClaims;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.pay.dto.service.request.ReservedChargeRequest;
import org.bank.pay.global.swagger.annotation.ApiSpec;
import org.bank.pay.global.swagger.annotation.SwaggerUserClaimsHeader;
import org.bank.pay.global.swagger.data.PayCardApi;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "캐시 예약 충전 API")
public interface ReservationChargeCashSwaggerSpec {

    @ApiSpec(summary = "캐시 충전 예약", description = "설정한 조건으로 캐시 충전을 예약합니다")
    @SwaggerUserClaimsHeader
    @RequestBody(
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = @ExampleObject(
                            name = "캐시 충전 예약 예시",
                            value = PayCardApi.RESERVATION_CHARGE_CASH_REQUEST
                    )
            )
    )
    ResponseEntity<? extends ResponseDtoV2> scheduled(@Parameter(hidden = true) AuthClaims authClaims, ReservedChargeRequest request);

    @ApiSpec(summary = "캐시 충전 예약 취소", description = "캐시 충전 예약을 해제합니다.")
    ResponseEntity<Void> cancelled(@Parameter(name = "scheduledId", description = "예약 충전 ID", required = true) UUID scheduledId);
}
