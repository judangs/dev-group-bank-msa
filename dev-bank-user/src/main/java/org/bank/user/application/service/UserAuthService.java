package org.bank.user.application.service;

import lombok.RequiredArgsConstructor;
import org.bank.user.application.usecase.UserAuthUseCase;
import org.bank.user.application.util.JwtUtility;
import org.bank.user.domain.credential.RoleClassification;
import org.bank.user.domain.credential.UserCredential;
import org.bank.user.domain.credential.infra.jpa.UserCredentialJpaRepository;
import org.bank.user.dto.ActionGroupResponse;
import org.bank.user.dto.credential.LoginActionRequest;
import org.bank.user.dto.credential.LoginActionResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAuthService implements UserAuthUseCase {


    private final UserCredentialJpaRepository userCredentialJpaRepository;
    //private final SessionRedisRepository sessionRedisRepository;

    private final JwtUtility jwtUtility;
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

    @Override
    public ActionGroupResponse login(LoginActionRequest request) {


        // UserNotFoundExcpetion 추가하기.
        UserCredential userCredential = userCredentialJpaRepository
                .findByUserid(request.getUserid())
                .orElseThrow(RuntimeException::new);


        String password = userCredential.getPassword();
        if(!passwordEncoder.matches(request.getPassword(), password)) {
            throw new IllegalArgumentException("Wrong password");
        }



        String refreshToken = jwtUtility.generateToken(userCredential.getUsername(), JwtUtility.REFRESH);
        String accessToken = jwtUtility.generateToken(userCredential.getUsername(), JwtUtility.ACCESS);

        // AOP로 해결해보기.

        // redis KeyGenerator
        // refreshToken / accessToken redis 저장

        // 로그인 이력 저장

        // Response 반환
        ActionGroupResponse response = new ActionGroupResponse();
        response.nextAction(LoginActionResponse.class, 200, "로그인이 완료되었습니다.");
        return response;
    }

    @Override
    public ActionGroupResponse logout(String token) {
        return null;
    }

}
