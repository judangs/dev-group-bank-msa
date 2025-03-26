package org.bank.pay.global.swagger.spec;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bank.core.auth.AuthClaims;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.pay.dto.service.response.CommunityEventAlarmResponse;
import org.bank.pay.global.swagger.annotation.ApiSpec;
import org.bank.pay.global.swagger.annotation.SwaggerUserClaimsHeader;
import org.springframework.http.ResponseEntity;

@Tag(name = "알림 API")
public interface CommunityAlarmSwaggerSpec {

    @ApiSpec(summary = "사용자 알림 확인", description = "사용자가 받은 알림 목록을 확인합니다.", responseSpec = CommunityEventAlarmResponse.class)
    @SwaggerUserClaimsHeader
    ResponseEntity<? extends ResponseDtoV2> list(@Parameter(hidden = true) AuthClaims user);
}
