package org.bank.user.core.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.auth.AuthConstants;
import org.bank.core.auth.AuthenticationException;
import org.bank.user.core.domain.account.Credential;
import org.bank.user.core.domain.account.repository.CredentialRepository;
import org.bank.user.core.domain.auth.TokenContents;
import org.bank.user.core.domain.crypto.PasswordProvider;
import org.bank.user.core.domain.jwt.service.JwtProvider;
import org.bank.user.core.domain.auth.repository.SessionTokenRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionManager implements AuthenticationService {


    private final CredentialRepository credentialRepository;
    private final SessionTokenRepository sessionTokenRepository;

    private final JwtProvider jwtProvider;
    private final PasswordProvider passwordProvider;

    @Override
    public String login(String userid, String password) throws AuthenticationException {

        Credential userCredential = credentialRepository.findByUserid(userid)
                .orElseThrow(AuthenticationException::new);


        Boolean authenticate =passwordProvider.matches(password, userCredential.getPassword());
        if(Boolean.FALSE.equals(authenticate)) {
            throw new AuthenticationException("아이디와 패스워드가 일치하지 않습니다.");
        }

        String refreshToken = jwtProvider.generate(userCredential, AuthConstants.TokenType.REFRESH);
        String accessToken = jwtProvider.generate(userCredential, AuthConstants.TokenType.ACCESS);

        sessionTokenRepository.save(accessToken, refreshToken);
        return accessToken;
    }

    @Override
    public void logout(AuthClaims user, String token) {
        sessionTokenRepository.deleteByTokenAndUser(token, user.getUserid());
    }

    @Override
    public String issueAccessToken(TokenContents tokenContents) {
        return jwtProvider.generate(tokenContents);
    }
}

