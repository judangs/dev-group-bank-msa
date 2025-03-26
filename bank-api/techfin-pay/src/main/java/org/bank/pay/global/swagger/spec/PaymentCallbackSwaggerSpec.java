package org.bank.pay.global.swagger.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bank.core.auth.AuthClaims;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.pay.global.swagger.annotation.ApiSpec;
import org.bank.pay.global.swagger.annotation.SwaggerPayCardPath;
import org.bank.pay.global.swagger.annotation.SwaggerUserClaimsHeader;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "결제 처리 API")
public interface PaymentCallbackSwaggerSpec {

    @ApiSpec(summary = "결제 처리 callback", description = "사용자가 결제한 내역을 처리합니다.")
    @SwaggerUserClaimsHeader
    @Operation(
            parameters = {
                    @Parameter(name = "reserveId", description = "처리된 결제 예약", required = true, example = "74cf2f22-2411-4349-834b-ebffbcab717c"),
                    @Parameter(name = "resultCode", description = "사용자 이름", required = true, example = "Success"),
                    @Parameter(name = "paymentId", description = "결제 번호", required = true, example = "6ba7b810-9dad-11d1-80b4-00c04fd430c8")
            }
    )
    ResponseEntity<? extends ResponseDtoV2> callback(@Parameter(hidden = true) AuthClaims user, UUID reserveId, String resultCode, String paymentId);

    @ApiSpec(summary = "결제 처리 callback", description = "사용자가 결제한 내역을 처리합니다.")
//    @SwaggerUserClaimsHeader
    @Operation(
            parameters = {
                    @Parameter(name = "user", description = "인코딩 된 유저 정보", required = true, example = "eyAgInVzZXJpZCI6ICJ0ZXN0dXNlciIsICAidXNlcm5hbWUiOiAi7YWM7Iqk7Yq4IiwgICJlbWFpbCI6ICJlb3RqZDIyOEBuYXZlci5jb20ifQ=="),
                    @Parameter(name = "reserveId", description = "처리된 결제 예약", required = true, example = "74cf2f22-2411-4349-834b-ebffbcab717c"),
                    @Parameter(name = "resultCode", description = "사용자 이름", required = true, example = "Success"),
                    @Parameter(name = "paymentId", description = "결제 번호", required = true, example = "6ba7b810-9dad-11d1-80b4-00c04fd430c8")
            }
    )
    ResponseEntity<? extends ResponseDtoV2> callback(@Parameter(hidden = true) AuthClaims user, @SwaggerPayCardPath UUID cardId, UUID reserveId, String resultCode, String paymentId);
}
