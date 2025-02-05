package org.bank.user.global.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.auth.AuthenticatedUser;
import org.bank.core.auth.AuthenticationException;
import org.bank.core.common.Base64Converter;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


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


        String userClaims = webRequest.getHeader(annotation.headerName());

        try {
            if(userClaims == null)
                throw new AuthenticationException("인증 절차가 수행되지 않았습니다.");

            return objectMapper.readValue(Base64Converter.decode(userClaims), AuthClaims.class);

        } catch (Exception e) {
            throw new AuthenticationException("헤더 정보가 부적절합니다.", e);
        }
    }
}
