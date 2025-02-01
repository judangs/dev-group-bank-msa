package org.bank.user.core.domain.jwt.service;


import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthConstants;
import org.bank.core.auth.TokenEncoder;
import org.bank.user.core.domain.account.Credential;
import org.bank.user.core.domain.auth.TokenClaims;
import org.bank.user.core.domain.auth.TokenContents;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtProvider {

    private final TokenEncoder<TokenContents, String> tokenEncoder;

    public String generate(Credential credential, AuthConstants.TokenType type) {

        TokenContents tokenContents = TokenContents.builder()
                .subject(credential.getUserid())
                .claims(TokenClaims.of(credential))
                .build();

        return tokenEncoder.encode(tokenContents, type);
    }

    public String generate(TokenContents tokenContents) {
        return tokenEncoder.encode(tokenContents, AuthConstants.TokenType.ACCESS);
    }

    public String generate(TokenContents tokenContents, AuthConstants.TokenType type) {
        return tokenEncoder.encode(tokenContents, type);
    }
}
