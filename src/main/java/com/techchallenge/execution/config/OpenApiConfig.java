package com.techchallenge.execution.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI executionServiceOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Execution Service API")
                .description("Execução e Produção — FIAP Tech Challenge Fase 4")
                .version("1.0.0"));
    }
}
