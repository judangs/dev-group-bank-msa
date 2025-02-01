package org.bank.user.core.facade.account;


import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.auth.AuthenticationException;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.user.core.domain.auth.service.AuthenticationService;
import org.bank.user.core.domain.auth.service.SessionRenewalService;
import org.bank.user.dto.service.response.LoginResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAccountAuthenticationFacade {

    private final AuthenticationService authenticationService;
    private final SessionRenewalService sessionRenewalService;

    public ResponseDtoV2 login(String userid, String password) {
        try {

            String accessToken = authenticationService.login(userid, password);
            return LoginResponse.success(accessToken);

        } catch (AuthenticationException e) {
            return ResponseDtoV2.unauthorized(e.getMessage());
        }
    }

    public void logout(AuthClaims user, String token) {
        authenticationService.logout(user, token);
    }

    public ResponseDtoV2 reissue(String token) {
        try {

            String accessToken = sessionRenewalService.validateAndRenewTokenIfNotExpired(token);
            return LoginResponse.success(accessToken);

        } catch (AuthenticationException e) {
            return ResponseDtoV2.unauthorized(e.getMessage());
        }
    }
}
