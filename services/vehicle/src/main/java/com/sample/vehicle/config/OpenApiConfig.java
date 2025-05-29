package com.sample.vehicle.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI vehicleServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Vehicle Service API")
                        .description("API documentation for the Vehicle microservice.")
                        .version("1.0.0"));
    }
}
