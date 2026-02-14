package com.cognizant.carservice.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPISecurityConfig {

    @Value("${keycloak.issuer}")
    String issuer;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("keycloakAdmin", oauthScheme("admin", "Admin access"))
                        .addSecuritySchemes("keycloakUser", oauthScheme("user", "Normal User access"))
                )
                // Don't force both scopes; Swagger will let you choose which scheme to authorize
                .addSecurityItem(new SecurityRequirement().addList("keycloakAdmin"))
                .addSecurityItem(new SecurityRequirement().addList("keycloakUser"))
                .info(new Info()
                        .title("Car Management Service")
                        .description("API with OAuth2 Security")
                        .version("1.0"));
    }

    private SecurityScheme oauthScheme(String scope, String desc) {
        String authUrl  = issuer + "/protocol/openid-connect/auth";
        String tokenUrl = issuer + "/protocol/openid-connect/token";

        OAuthFlow code = new OAuthFlow()
                .authorizationUrl(authUrl)
                .tokenUrl(tokenUrl)
                .scopes(new Scopes().addString(scope, desc));

        return new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .flows(new OAuthFlows().authorizationCode(code));
    }
}