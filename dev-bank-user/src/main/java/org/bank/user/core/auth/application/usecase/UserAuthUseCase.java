package org.bank.user.core.auth.application.usecase;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bank.user.global.usecase.UseCase;
import org.bank.user.dto.credential.LoginRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserAuthUseCase extends UseCase, UserDetailsService {
    void login(LoginRequest request, HttpServletResponse response);
    void logout(HttpServletRequest request);

    // 계정 관련
    //ActionGroupResponse findAllCredentials();
    //ActionGroupResponse findCredentialById();
    //ActionGroupResponse changePassword();
}
