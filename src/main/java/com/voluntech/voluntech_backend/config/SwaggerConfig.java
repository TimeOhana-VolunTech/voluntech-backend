package com.voluntech.voluntech_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("VolunTech API")
                        .version("1.0")
                        .description("API para gerenciamento de ONGs e Voluntários - Projeto Pipoca Ágil"));
    }
}