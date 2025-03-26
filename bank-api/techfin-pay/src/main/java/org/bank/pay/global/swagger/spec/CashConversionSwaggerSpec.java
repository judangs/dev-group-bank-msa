package org.bank.pay.global.swagger.spec;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bank.core.auth.AuthClaims;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.pay.dto.service.request.CashConversionRequest;
import org.bank.pay.global.swagger.annotation.ApiSpec;
import org.bank.pay.global.swagger.annotation.SwaggerFamilyPath;
import org.bank.pay.global.swagger.annotation.SwaggerUserClaimsHeader;
import org.bank.pay.global.swagger.data.PayCardApi;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "패밀리 캐시 충전 API")
public interface CashConversionSwaggerSpec {

    @RequestBody(
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = @ExampleObject(
                            name = "패밀리 캐시 전환 예시",
                            value = PayCardApi.CONVERSION_CASH_REQUEST
                    )
            )
    )
    @ApiSpec(summary = "패밀리 캐시 전환", description = "보유한 캐시를 패밀리 캐시로 전환합니다.")
    @SwaggerUserClaimsHeader
    ResponseEntity<? extends ResponseDtoV2> conversion(@Parameter(hidden = true) AuthClaims user,
                                                       @SwaggerFamilyPath UUID familyId,
                                                       CashConversionRequest cashConversionRequest);
}
