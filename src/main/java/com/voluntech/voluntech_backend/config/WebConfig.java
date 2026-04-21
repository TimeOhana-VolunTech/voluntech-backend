package com.voluntech.voluntech_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Libera todos os endpoints da API
                .allowedOriginPatterns(
                    "http://localhost:4200", 
                    "https://voluntech-frontend.netlify.app",
                    "https://--ohana-voluntech.netlify.app", // Padrão para os deploys do Netlify
                    "https://*.netlify.app" // Aceita qualquer preview ou subdomínio do Netlify
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // Métodos permitidos
                .allowedHeaders("*") // Permite todos os headers (importante para Content-Type, Authorization, etc)
                .allowCredentials(true); // Permite envio de cookies/autenticação se necessário
    }
}