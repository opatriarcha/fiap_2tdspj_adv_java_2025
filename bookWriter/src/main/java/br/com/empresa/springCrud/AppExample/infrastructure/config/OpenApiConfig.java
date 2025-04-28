package br.com.empresa.springCrud.AppExample.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Exemplo - Order Management")
                        .version("1.0")
                        .description("Documentação automática gerada com Springdoc OpenAPI 3"));
    }
}
