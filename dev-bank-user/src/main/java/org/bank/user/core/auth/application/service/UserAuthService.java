package org.bank.user.core.auth.application.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.bank.user.core.auth.application.usecase.UserAuthUseCase;
import org.bank.user.core.auth.application.provider.JwtProvider;
import org.bank.user.core.user.domain.credential.RoleClassification;
import org.bank.user.core.user.domain.credential.UserCredential;
import org.bank.user.core.auth.domain.repository.RefreshTokenRedisRepository;
import org.bank.user.core.user.domain.credential.repository.jpa.UserCredentialJpaRepository;
import org.bank.user.dto.credential.LoginRequest;
import org.bank.user.global.exception.PermissionException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAuthService implements UserAuthUseCase {


    private final UserCredentialJpaRepository userCredentialJpaRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Custom Exception 생성하기
        UserCredential userCredential = userCredentialJpaRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(username));

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(
                RoleClassification.UserRole.of(
                        userCredential.getUserType(), userCredential.getUsername()
                ).getType()
        ));

        return new User(
                userCredential.getUsername(),
                userCredential.getPassword(), authorities);

    }

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

        String refreshToken = jwtProvider.generate(userCredential, JwtProvider.REFRESH);
        String accessToken = jwtProvider.generate(userCredential, JwtProvider.ACCESS);

        String tokenId = refreshTokenRedisRepository.createId(() -> userCredential.getUserid());
        refreshTokenRedisRepository.save(tokenId, refreshToken);
        //userCredential.publish(new UserAuthEvent(DomainEvent.LOGIN, DomainEvent.SUCCESS));

        jwtProvider.addTokenToResponse(response, refreshToken, accessToken);
    }

    @Override
    public void logout(HttpServletRequest request) {

        String tokenid;

        Cookie[] cookies = request.getCookies();
        Optional<Cookie> refreshCookie  =  Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(jwtProvider.REFRESH))
                .findFirst();

        if(refreshCookie.isEmpty()) {


            Optional<String> accessToken = jwtProvider.getTokenFromRequest(request);

            if(accessToken.isEmpty()) {
                throw new PermissionException("전송된 인증 정보가 없습니다.");
            }

            tokenid = refreshTokenRedisRepository.createId(() -> jwtProvider.getUseridFromToken(accessToken.get()));
        }
        else {

            String refreshToken = refreshCookie.get().getValue();
            tokenid = refreshTokenRedisRepository.createId(() -> jwtProvider.getUseridFromToken(refreshToken));
        }

        refreshTokenRedisRepository.delete(tokenid);

        // 로그아웃 이벤트 발행
    }

}
