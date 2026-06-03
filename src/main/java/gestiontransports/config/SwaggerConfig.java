package gestiontransports.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration OpenAPI/Swagger exposant la documentation interactive de l'API REST avec authentification JWT.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Construit le bean {@link OpenAPI} décrivant l'API et enregistre le schéma de sécurité Bearer JWT
     * afin que Swagger UI permette de tester les endpoints protégés.
     *
     * @return l'instance {@link OpenAPI} configurée
     */
    @Bean
    public OpenAPI openAPI() {
        final String schemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Gestion des transports API")
                        .version("1.0.0")
                        .description("Backend Spring Boot — Diginamic groupe 3"))
                .addSecurityItem(new SecurityRequirement().addList(schemeName))
                .components(new Components()
                        .addSecuritySchemes(schemeName, new SecurityScheme()
                                .name(schemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
