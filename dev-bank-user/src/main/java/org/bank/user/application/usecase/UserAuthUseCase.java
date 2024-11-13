package org.bank.user.application.usecase;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bank.user.dto.credential.LoginActionRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserAuthUseCase extends UseCase, UserDetailsService {
    void login(LoginActionRequest request, HttpServletResponse response);
    void logout(HttpServletRequest request);

    // 계정 관련
    //ActionGroupResponse findAllCredentials();
    //ActionGroupResponse findCredentialById();
    //ActionGroupResponse changePassword();
}
