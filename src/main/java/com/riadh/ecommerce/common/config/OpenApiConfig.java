package com.riadh.ecommerce.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ecommerce API")
                        .version("1.0.0")
                        .description("API REST pour la gestion de l'e-commerce (produits, catégories, fichiers, images)")
                        .contact(new Contact()
                                .name("Riadh")
                                .email("riadh@ecommerce.com"))
                );
    }
}

