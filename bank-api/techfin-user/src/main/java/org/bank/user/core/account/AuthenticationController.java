package org.bank.user.core.account;


import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.auth.AuthenticatedUser;
import org.bank.core.dto.response.ResponseCodeV2;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.user.core.facade.account.UserAccountAuthenticationFacade;
import org.bank.user.dto.service.request.LoginRequest;
import org.bank.user.dto.service.response.LoginResponse;
import org.bank.user.global.http.AuthorizationHeaderResolver;
import org.bank.user.global.swagger.spec.UserAuthenticationSwaggerSpec;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/auth")
public class AuthenticationController implements UserAuthenticationSwaggerSpec {

    private final AuthorizationHeaderResolver authorizationHeaderResolver;
    private final UserAccountAuthenticationFacade userAccountAuthenticationFacade;

    @PostMapping("/login")
    public ResponseEntity<? super LoginResponse> login(@RequestBody LoginRequest request, HttpServletResponse httpServletResponse) {

        ResponseDtoV2 loginResponse = userAccountAuthenticationFacade.login(request.getUserid(), request.getPassword());
        if(loginResponse instanceof LoginResponse response) {
            if(loginResponse.getCode().equals(ResponseCodeV2.SUCCESS)) {
                authorizationHeaderResolver.addTokenToResponseHeader(httpServletResponse, response.getToken().getAccess());
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<? super LoginResponse> logout(
            @AuthenticatedUser AuthClaims user,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    ) {

        String token = authorizationHeaderResolver.parseRequestHeaderToToken(authorization);
        userAccountAuthenticationFacade.logout(user, token);

        ResponseDtoV2 response = ResponseDtoV2.success("로그아웃에 성공했습니다.");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

