package io.github.mtbarr.challenge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Java Challenge API")
                        .version("1.0.0")
                        .description("API para gerenciamento simples de livros")
                        .contact(new Contact().name("Seu Nome").email("seu.email@example.com")));
    }
}

