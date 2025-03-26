package org.bank.pay.global.swagger.spec;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bank.core.auth.AuthClaims;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.pay.dto.service.request.FamilyEventRequest;
import org.bank.pay.global.swagger.annotation.ApiSpec;
import org.bank.pay.global.swagger.annotation.SwaggerFamilyPath;
import org.bank.pay.global.swagger.annotation.SwaggerUserClaimsHeader;
import org.bank.pay.global.swagger.data.PayCardApi;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "그룹(패밀리) 기능 API")
public interface FamilyManagerSwaggerSpec {


    @ApiSpec(summary = "패밀리 상세 조회", description = "속해 있는 패밀리를 조회합니다.")
    @SwaggerUserClaimsHeader
    ResponseEntity<? extends ResponseDtoV2> view(@Parameter(hidden = true) AuthClaims user);

    @RequestBody(
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = @ExampleObject(
                            name = "패밀리 초대 요청 수락 예시",
                            value = PayCardApi.ACCEPT_FAMILY_INVITATION
                    )
            )
    )
    @ApiSpec(summary = "패밀리 초대 수락", description = "요청받은 패밀리 초대를 수락합니다.")
    @SwaggerUserClaimsHeader
    ResponseEntity<? extends ResponseDtoV2> participate(@Parameter(hidden = true) AuthClaims follower,
                                                        FamilyEventRequest request);


    @ApiSpec(summary = "리더 위임", description = "패밀리 리더를 그룹원에게 위임합니다.")
    @SwaggerUserClaimsHeader
    ResponseEntity<? extends ResponseDtoV2> delegate(@Parameter(hidden = true) AuthClaims leader,
                                                     @SwaggerFamilyPath UUID familyId,
                                                     @Parameter(name = "위임할 멤버 유저 아이디", required = true) String memberId);


    @ApiSpec(summary = "패밀리 멤버 초대", description = "사용자를 패밀리 멤버로 초대합니다.")
    @SwaggerUserClaimsHeader
    ResponseEntity<? extends ResponseDtoV2> invite(@Parameter(hidden = true) AuthClaims leader,
                                                   @SwaggerFamilyPath UUID familyId,
                                                   @Parameter(name = "멤버 유저 아이디", required = true) String userid);


    @ApiSpec(summary = "패밀리 멤버 추방", description = "패밀리 그룹원을 추방합니다.")
    @SwaggerUserClaimsHeader
    ResponseEntity<? extends ResponseDtoV2> expel(@Parameter(hidden = true) AuthClaims leader,
                                                  @SwaggerFamilyPath UUID familyId,
                                                  @Parameter(name = "멤버 유저 아이디", required = true) String memberId);

}
