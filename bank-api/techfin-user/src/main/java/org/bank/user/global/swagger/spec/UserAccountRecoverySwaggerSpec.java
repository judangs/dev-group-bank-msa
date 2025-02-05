package org.bank.user.global.swagger.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bank.user.dto.service.response.AccountIdListResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "사용자 정보 찾기 API 테스트")
public interface UserAccountRecoverySwaggerSpec {


    @Operation(
            summary = "아이디 찾기",
            description = "주어진 username과 email을 사용하여 아이디를 찾습니다.",
            parameters = {
                    @Parameter(name = "username", description = "사용자 이름", required = true, example = "테스트"),
                    @Parameter(name = "email", description = "이메일", required = true, example = "eotjd228@naver.com")
            }
    )
    ResponseEntity<? super AccountIdListResponse> findAccountUserid(String username, String email);

    @Operation(
            summary = "비밀번호 찾기",
            description = "userid와 email을 사용하여 비밀번호를 찾습니다.",
            parameters = {
                    @Parameter(name = "userid", description = "사용자 아이디", required = true, example = "testuser"),
                    @Parameter(name = "email", description = "이메일", required = true, example = "eotjd228@naver.com")
            }
    )
    ResponseEntity<? super AccountIdListResponse> findAccountUserPassword(String userid, String email);
}
