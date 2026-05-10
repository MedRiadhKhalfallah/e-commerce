package com.riadh.ecommerce.features.file.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Gère le cycle de vie des credentials OAuth2 Google Drive.
 * - Au démarrage, charge les tokens persistés si disponibles.
 * - Après le callback OAuth, stocke les nouveaux tokens.
 * - Fournit un {@link Drive} prêt à l'emploi.
 */
@Component
public class GoogleDriveCredentialManager {

    private static final Logger log = LoggerFactory.getLogger(GoogleDriveCredentialManager.class);
    private static final String USER_ID = "user";

    private final GoogleAuthorizationCodeFlow flow;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${google.drive.redirect-uri}")
    private String redirectUri;

    private volatile Credential credential;

    public GoogleDriveCredentialManager(GoogleAuthorizationCodeFlow flow) {
        this.flow = flow;
    }

    // -------------------------------------------------------------------------
    // Initialisation au démarrage
    // -------------------------------------------------------------------------

    @PostConstruct
    public void init() {
        try {
            credential = flow.loadCredential(USER_ID);
            if (credential != null) {
                log.info("[GoogleDrive] Tokens OAuth2 chargés depuis le stockage persistant.");
            } else {
                log.warn("[GoogleDrive] Aucun token trouvé. Visitez GET /api/google/authorize pour autoriser l'accès.");
            }
        } catch (Exception e) {
            log.error("[GoogleDrive] Erreur lors du chargement des credentials : {}", e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // URL d'autorisation
    // -------------------------------------------------------------------------

    /**
     * Génère l'URL vers laquelle rediriger l'utilisateur pour l'autorisation Google.
     */
    public String buildAuthorizationUrl() {
        return flow.newAuthorizationUrl()
                .setRedirectUri(redirectUri)
                .setState("google-drive-auth")
                .build();
    }

    // -------------------------------------------------------------------------
    // Échange du code d'autorisation contre les tokens
    // -------------------------------------------------------------------------

    /**
     * Échange le code retourné par Google contre un access_token + refresh_token.
     * Stocke les tokens de façon persistante.
     */
    public void exchangeCodeAndStore(String code) throws Exception {
        GoogleTokenResponse tokenResponse = flow.newTokenRequest(code)
                .setRedirectUri(redirectUri)
                .execute();

        credential = flow.createAndStoreCredential(tokenResponse, USER_ID);
        log.info("[GoogleDrive] Tokens OAuth2 stockés avec succès.");
    }

    // -------------------------------------------------------------------------
    // Accès au service Drive
    // -------------------------------------------------------------------------

    /**
     * Retourne une instance {@link Drive} configurée avec les credentials OAuth2 courants.
     *
     * @throws IllegalStateException si l'utilisateur n'a pas encore autorisé l'accès.
     */
    public Drive getDriveService() throws Exception {
        if (credential == null) {
            throw new IllegalStateException(
                    "Google Drive non autorisé. Visitez GET /api/google/authorize pour lancer le flux OAuth2."
            );
        }

        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                credential
        ).setApplicationName(applicationName).build();
    }

    /**
     * Indique si l'utilisateur a déjà accordé les permissions.
     */
    public boolean isAuthorized() {
        return credential != null;
    }
}

