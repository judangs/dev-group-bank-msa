package org.bank.user.core.auth.presentation.http;


import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.bank.common.constants.auth.AuthHeaders;
import org.bank.user.core.auth.application.usecase.UserAuthUseCase;
import org.bank.user.dto.credential.LoginRequest;
import org.bank.user.dto.credential.LoginResponse;
import org.bank.user.global.dto.ResponseDto;
import org.bank.user.global.exception.PermissionException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/auth")
public class UserAuthController {

    private final UserAuthUseCase userAuthUseCase;

    @PostMapping("/login")
    public ResponseEntity<? super LoginResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {

        userAuthUseCase.login(request, response);

        ResponseDto responseBody = ResponseDto.success("로그인에 성공했습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @PostMapping("/logout")
    public ResponseEntity<? super LoginResponse> logout(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @RequestHeader(AuthHeaders.USER_ID) String userid
    ) {
        userAuthUseCase.logout(authorization, userid);

        ResponseDto responseBody = ResponseDto.success("로그아웃이 정상적으로 완료되었습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @PostMapping("/expire-token")
    public Mono<String> expireToken(@RequestBody String token) {

        try {
            String newToken = userAuthUseCase.reIssue(token);
            return Mono.just(newToken);
        } catch (PermissionException e) {
            return Mono.error(e.getCause());
        }

    }
}
