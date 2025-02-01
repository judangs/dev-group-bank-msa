package org.bank.user.core.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthenticationException;
import org.bank.core.auth.TokenExpiredException;
import org.bank.user.core.domain.auth.TokenContents;
import org.bank.user.core.domain.jwt.service.JwtValidator;
import org.bank.user.core.domain.auth.repository.TokenRedisRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionRenewalService {

    private final AuthenticationService authenticationService;
    private final JwtValidator jwtValidator;

    private final TokenRedisRepository tokenRedisRepository;

    public String validateAndRenewTokenIfNotExpired(String token) throws AuthenticationException{

        Optional<String> refreshToken = tokenRedisRepository.findByToken(token);
        if (refreshToken.isEmpty()) {
            throw new AuthenticationException("로그인 인증이 필요합니다.");
        }

        try {
            Optional<TokenContents> optionalTokenContents = refreshToken.map(jwtValidator::retrieveContentsIfNotExpired)
                    .orElse(null);

            String access = optionalTokenContents.map(tokenContents -> {
                tokenRedisRepository.deleteByTokenAndUser(token, tokenContents.getSubject());
                return authenticationService.issueAccessToken(tokenContents);
            }).orElse(null);

            if(Objects.isNull(access)) {
                throw new AuthenticationException("인증 기간이 만료되었습니다. 사용자 인증이 필요합니다.");
            }

            refreshToken.ifPresent(refresh -> tokenRedisRepository.save(access, refresh));
            return access;

        } catch (TokenExpiredException e) {
            throw new AuthenticationException("인증 기간이 만료되었습니다. 사용자 인증이 필요합니다.");
        }
    }
}
