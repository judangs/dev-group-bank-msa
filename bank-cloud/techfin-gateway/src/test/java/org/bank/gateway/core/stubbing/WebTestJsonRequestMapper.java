package org.bank.gateway.core.stubbing;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class WebTestJsonRequestMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String body(String file) throws IOException {
        ClassPathResource resource = new ClassPathResource("/stubbing/app/user/__files/" + file);
        return objectMapper.readTree(resource.getInputStream()).toString();
    }

}
