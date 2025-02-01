package org.bank.user.core.domain.jwt.service;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthConstants;
import org.bank.core.auth.TokenDecoder;
import org.bank.user.core.domain.auth.TokenContents;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtValidator {

    private final TokenDecoder<TokenContents> tokenDecoder;

    public Optional<TokenContents> retrieveContentsIfNotExpired(String token) {

        TokenContents contents = tokenDecoder.decode(token, AuthConstants.TokenType.REFRESH);

        if(Objects.isNull(contents.getSubject())) {
            return Optional.empty();
        }

        return Optional.of(contents);
    }
}
