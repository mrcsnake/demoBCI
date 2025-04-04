package com.example.demo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Swagger para la documentación de la API.
 * Define la información general de la API y el esquema de seguridad para la autenticación JWT.
 * @author [Marco Hermosilla]
 * @version 1.0
 * @since [03-04-2025]
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "Mi API", version = "v1"))
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class SwaggerConfig {
}