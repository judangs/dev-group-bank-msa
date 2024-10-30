package org.bank.user.application.usecase;


import org.bank.user.dto.ActionGroupResponse;
import org.bank.user.dto.credential.LoginActionRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserAuthUseCase extends UseCase, UserDetailsService {
    ActionGroupResponse login(LoginActionRequest request);
    ActionGroupResponse logout(String token);

    // 계정 관련
    //ActionGroupResponse findAllCredentials();
    //ActionGroupResponse findCredentialById();
    //ActionGroupResponse changePassword();
}
