package com.example.llmbackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI mixtralBackendOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mixtral 8x7B Survey Generator API")
                        .description("API для генерации опросов с помощью Mixtral 8x7B")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("AI Development Team")
                                .email("ai@company.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Локальный сервер разработки"),
                        new Server()
                                .url("http://your-server-ip:8080")
                                .description("Продакшен сервер")
                ));
    }
}