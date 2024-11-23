package org.bank.user.core.auth.application.service;

import exception.MissingHeaderException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.bank.common.constants.auth.TokenConstants;
import org.bank.user.core.auth.application.provider.JwtProvider;
import org.bank.user.core.auth.application.usecase.UserAuthUseCase;
import org.bank.user.core.auth.domain.repository.RefreshTokenRedisRepository;
import org.bank.user.core.user.domain.credential.UserCredential;
import org.bank.user.core.user.domain.credential.repository.jpa.UserCredentialJpaRepository;
import org.bank.user.dto.credential.LoginRequest;
import org.bank.user.global.exception.PermissionException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        String tokenId = refreshTokenRedisRepository.createId(() ->
                String.join(":", accessToken, userCredential.getUserid()));
        refreshTokenRedisRepository.save(tokenId, refreshToken);
        //userCredential.publish(new UserAuthEvent(DomainEvent.LOGIN, DomainEvent.SUCCESS));

        jwtProvider.addJwtToResponseHeader(response, accessToken);
    }

    @Override
    public void logout(HttpServletRequest request) {

        try {
            String accessToken = jwtProvider.getTokenFromRequest(request);
            String userid = jwtProvider.getUseridFromRequest(request);

            refreshTokenRedisRepository.deleteTokenByUser(accessToken, userid);
            // 로그아웃 이벤트 발행

        } catch (MissingHeaderException e) {
            throw new PermissionException("부적절한 인증 정보입니다.");
        }
    }
}
