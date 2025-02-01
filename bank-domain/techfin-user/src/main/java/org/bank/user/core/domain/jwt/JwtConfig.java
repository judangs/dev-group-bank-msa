package org.bank.user.core.domain.jwt;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/auth-token-jwt.properties")
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfig {

    @Bean
    public JwtProperties jwtProperties() {
        return new JwtProperties();
    }

//    @Bean
//    public TokenEncoder<TokenContents, String> tokenEncoder(JwtProperties jwtProperties) {
//        return new JwtEncoder(jwtProperties);
//    }
//
//    @Bean
//    public TokenDecoder<TokenContents> tokenDecoder(JwtProperties jwtProperties) {
//        return new JwtDecoder(jwtProperties);
//    }

}
