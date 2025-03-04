package org.bank.pay.core.domain.familly;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.bank.core.auth.AuthClaims;

public class AuthToMemberConverter implements Converter<AuthClaims.ConcreteAuthClaims, MemberClaims> {


    @Override
    public MemberClaims convert(AuthClaims.ConcreteAuthClaims concreteAuthClaims) {
        return MemberClaims.of(concreteAuthClaims);
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return typeFactory.constructType(AuthClaims.ConcreteAuthClaims.class);
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return typeFactory.constructType(MemberClaims.class);
    }
}
