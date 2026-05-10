package com.riadh.ecommerce.features.file.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.DriveScopes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

@Configuration
public class GoogleDriveConfig {

    @Value("${google.drive.credentials-path}")
    private String credentialsPath;

    @Value("${google.drive.tokens-directory}")
    private String tokensDirectory;

    private static final List<String> SCOPES = List.of(DriveScopes.DRIVE);

    @Bean
    public GoogleClientSecrets googleClientSecrets() throws Exception {
        try (InputStreamReader reader = new InputStreamReader(
                new FileInputStream(credentialsPath))) {
            return GoogleClientSecrets.load(GsonFactory.getDefaultInstance(), reader);
        }
    }

    @Bean
    public GoogleAuthorizationCodeFlow googleAuthorizationCodeFlow(
            GoogleClientSecrets clientSecrets
    ) throws Exception {
        return new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                clientSecrets,
                SCOPES
        )
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(tokensDirectory)))
                .setAccessType("offline")   // pour obtenir un refresh_token
                .build();
    }
}

