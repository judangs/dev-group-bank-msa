package org.bank.user.global.resolver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.auth.AuthenticatedUser;
import org.bank.core.auth.AuthenticationException;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.nio.charset.StandardCharsets;
import java.util.Base64;


@Component
@RequiredArgsConstructor
public class AuthenticatedUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final ObjectMapper objectMapper;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(AuthClaims.class) &&
                parameter.hasParameterAnnotation(AuthenticatedUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        AuthenticatedUser annotation = parameter.getParameterAnnotation(AuthenticatedUser.class);
        String claims = webRequest.getHeader(annotation.headerName());

        if(claims == null) {
            if(annotation.required()) {
                throw new AuthenticationException("인증 절차가 수행되지 않았습니다.");
            }

            return claims;
        }

        try {
            byte[] decodedBytes = Base64.getDecoder().decode(claims);
            String decodedJson = new String(decodedBytes, StandardCharsets.UTF_8);

            return objectMapper.readValue(decodedJson, AuthClaims.class);
        } catch (IllegalArgumentException | JsonProcessingException e) {
            throw new AuthenticationException("헤더 정보가 부적절합니다.", e);
        }
    }
}
