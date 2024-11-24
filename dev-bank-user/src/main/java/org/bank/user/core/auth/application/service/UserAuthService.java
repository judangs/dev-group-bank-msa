package org.bank.user.core.auth.application.service;

import exception.TokenExpiredException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.bank.common.constants.auth.TokenConstants;
import org.bank.user.core.auth.application.provider.JwtProvider;
import org.bank.user.core.auth.application.usecase.UserAuthUseCase;
import org.bank.user.core.auth.domain.TokenPayload;
import org.bank.user.core.auth.domain.repository.RefreshTokenRedisRepository;
import org.bank.user.core.user.domain.credential.UserCredential;
import org.bank.user.core.user.domain.credential.repository.jpa.UserCredentialJpaRepository;
import org.bank.user.dto.credential.LoginRequest;
import org.bank.user.global.exception.PermissionException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAuthService implements UserAuthUseCase {


    private final UserCredentialJpaRepository userCredentialJpaRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;


    // 이벤트 publish 로직 작성하기.
    @Override
    public void login(LoginRequest request, HttpServletResponse response) {


        UserCredential userCredential = userCredentialJpaRepository
                .findByUserid(request.getUserid())
                .orElseThrow(() -> new PermissionException("아이디나 패스워드가 일치하지 않습니다."));


        String password = userCredential.getPassword();
        if(!passwordEncoder.matches(request.getPassword(), password)) {
            //userCredential.publish(new UserAuthEvent(DomainEvent.LOGIN, DomainEvent.FAILURE));
            throw new PermissionException("아이디나 패스워드가 일치하지 않습니다.");
        }

        String refreshToken = jwtProvider.generate(userCredential, TokenConstants.REFRESH);
        String accessToken = jwtProvider.generate(userCredential, TokenConstants.ACCESS);

        // ex) refresh token id: accessToken:userid
        String tokenid = refreshTokenRedisRepository.createId(() ->
                String.join(":", accessToken, userCredential.getUserid()));
        refreshTokenRedisRepository.save(tokenid, refreshToken);
        //userCredential.publish(new UserAuthEvent(DomainEvent.LOGIN, DomainEvent.SUCCESS));

        jwtProvider.addJwtToResponseHeader(response, accessToken);
    }

    @Override
    public void logout(String authorization, String userid) {

        String token = authorization.substring(TokenConstants.BEARER_PREFIX.length());
        refreshTokenRedisRepository.deleteByTokenAndUser(token, userid);

    }

    @Override
    public String reIssue(String token) throws PermissionException {

        String accessToken = "";

        Optional<String> refreshToken = refreshTokenRedisRepository.findByToken(token);
        if(refreshToken.isEmpty()) {
            throw new PermissionException("로그인 인증이 필요합니다.");
        }

        try {
            TokenPayload payload = jwtProvider.decode(refreshToken.get());
            accessToken = jwtProvider.generate(payload);

            String finalAccessToken = accessToken;
            String tokenid = refreshTokenRedisRepository.createId(() ->
                    String.join(":", finalAccessToken, payload.getSubject()));


            refreshTokenRedisRepository.deleteByTokenAndUser(token, payload.getSubject());
            refreshTokenRedisRepository.save(tokenid, refreshToken.get());

        } catch (TokenExpiredException e) {
            throw new PermissionException("인증 기간이 만료되었습니다. 사용자 인증이 필요합니다.");
        }

        return accessToken;
    }
}
