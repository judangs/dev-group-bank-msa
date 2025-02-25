package org.bank.pay.global.swagger.data;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SwaggerPayCardResponseExampleGenerator {

    public String generate(Class<?> classType) throws ClassNotFoundException {
        Optional<String> response = PayCardApi.response(classType);
        if(response.isEmpty())
            throw new ClassNotFoundException();

        return response.get();
    }
}
