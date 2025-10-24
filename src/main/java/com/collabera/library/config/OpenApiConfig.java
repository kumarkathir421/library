package com.collabera.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

	@Bean
    public OpenAPI libraryApiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("📚 Library Management API")
                        .description("A simple RESTful API to manage borrowers and books.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Gnanaprakash")
                                .email("gnaanaprakaash@gmail.com")
                                .url("https://github.com/kumarkathir421/library")));
    }
}
