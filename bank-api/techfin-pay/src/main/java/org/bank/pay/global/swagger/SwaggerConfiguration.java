package org.bank.pay.global.swagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.pay.global.swagger.annotation.ApiSpec;
import org.bank.pay.global.swagger.data.SwaggerPayCardResponseExampleGenerator;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Bean
    public OperationCustomizer customOperation(
            SwaggerPayCardResponseExampleGenerator swaggerPayCardResponseExampleGenerator
    ) {
        return (operation, handlerMethod) -> {

            ApiSpec apiSpec = handlerMethod.getMethodAnnotation(ApiSpec.class);
            if(apiSpec != null) {

                operation.setSummary(apiSpec.summary());
                operation.setDescription(apiSpec.description());

                ApiResponses apiResponses = operation.getResponses();
                ApiResponse apiResponse200 = new ApiResponse().description("요청 성공");

                MediaType mediaType = null;
                try {
                    String example = swaggerPayCardResponseExampleGenerator.generate(apiSpec.responseSpec());
                    mediaType = new MediaType().schema(new Schema()).example(example);
                } catch (Exception e) {
                    mediaType = new MediaType().schema(new Schema()).example(ResponseDtoV2.success("요청을 완료했습니다."));
                }

                apiResponse200.setContent(new Content().addMediaType("application/json", mediaType));

                ApiResponse apiResponse400 = new ApiResponse().description("잘못된 요청");
                apiResponse400.setContent(
                        new Content().addMediaType("application/json",
                                new MediaType().schema(new Schema()).example(ResponseDtoV2.fail("잘못된 요청입니다."))));

                ApiResponse apiResponse401 = new ApiResponse().description("잘못된 요청");
                apiResponse401.setContent(
                        new Content().addMediaType("application/json",
                                new MediaType().schema(new Schema()).example(ResponseDtoV2.unauthorized("인증에 실패했습니다."))));

                apiResponses.addApiResponse("200", apiResponse200);
                apiResponses.addApiResponse("400", apiResponse400);
                apiResponses.addApiResponse("401", apiResponse401);

                operation.setResponses(apiResponses);

            }

            return operation;
        };
    }



}
