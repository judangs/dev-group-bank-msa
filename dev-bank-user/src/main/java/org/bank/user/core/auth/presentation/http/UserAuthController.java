package org.bank.user.core.auth.presentation.http;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.bank.user.core.auth.application.usecase.UserAuthUseCase;
import org.bank.user.global.dto.ResponseDto;
import org.bank.user.dto.credential.LoginRequest;
import org.bank.user.dto.credential.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<? super LoginResponse> logout(HttpServletRequest request) {
        userAuthUseCase.logout(request);

        ResponseDto responseBody = ResponseDto.success("로그아웃이 정상적으로 완료되었습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }


}
