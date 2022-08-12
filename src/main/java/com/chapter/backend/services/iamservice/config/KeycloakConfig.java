package com.chapter.backend.services.iamservice.config;

import lombok.Setter;
import org.keycloak.OAuth2Constants;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "keycloak-admin")
@Setter
public class KeycloakConfig {

    private String authServerUrl;
    private String realmAdmin;
    private String clientIdAdmin;
    private String username;
    private String password;

    @Value("${keycloak.realm}")
    private String realmClient;

    @Bean
    public KeycloakSpringBootConfigResolver keycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    @Bean
    public RealmResource keycloakClient() {
        return keycloakInstance().realm(realmClient);
    }

    private Keycloak keycloakInstance() {
        return KeycloakBuilder.builder()
                .serverUrl(authServerUrl)
                .realm(realmAdmin)
                .clientId(clientIdAdmin)
                .grantType(OAuth2Constants.PASSWORD)
                .username(username)
                .password(password)
                .build();
    }
}