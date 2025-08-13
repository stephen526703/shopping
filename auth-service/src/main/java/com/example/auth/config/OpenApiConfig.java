package com.example.auth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.*;

@Configuration
public class OpenApiConfig {
    @Bean public OpenAPI api(){ return new OpenAPI().info(new Info().title("Auth Service API").version("1.0.0")); }
}
