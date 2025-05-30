package com.sample.insurance.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI insuranceServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Insurance Service API")
                        .description("API for managing insurance products and retrieving insurance information by personal ID")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Insurance Team")
                                .email("insurance@sample.com")));
    }
}
