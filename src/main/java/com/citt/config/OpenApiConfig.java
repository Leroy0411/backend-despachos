package com.citt.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "API REST Despachos - Innovatech Chile",
        version = "1.0",
        description = "API para gestión de despachos"
    )
)
public class OpenApiConfig {
}
