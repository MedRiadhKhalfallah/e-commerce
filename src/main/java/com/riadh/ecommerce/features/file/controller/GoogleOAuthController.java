package com.riadh.ecommerce.features.file.controller;

import com.riadh.ecommerce.features.file.service.GoogleDriveCredentialManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Google Drive OAuth2", description = "Flux d'autorisation OAuth2 pour Google Drive")
@RestController
@RequestMapping("/api/google")
public class GoogleOAuthController {

    private final GoogleDriveCredentialManager credentialManager;

    public GoogleOAuthController(GoogleDriveCredentialManager credentialManager) {
        this.credentialManager = credentialManager;
    }

    // -------------------------------------------------------------------------
    // Étape 1 : Générer l'URL d'autorisation
    // -------------------------------------------------------------------------

    @Operation(
            summary = "Obtenir l'URL d'autorisation Google",
            description = """
                    Retourne l'URL vers laquelle l'utilisateur doit naviguer pour autoriser
                    l'accès à Google Drive. Après avoir cliqué sur "Autoriser", Google redirige
                    vers le callback configuré avec un code d'autorisation.
                    """
    )
    @ApiResponse(responseCode = "200", description = "URL d'autorisation générée")
    @GetMapping("/authorize")
    public ResponseEntity<Map<String, String>> authorize() {
        String authUrl = credentialManager.buildAuthorizationUrl();
        return ResponseEntity.ok(Map.of(
                "status", "action_required",
                "message", "Ouvrez cette URL dans votre navigateur pour autoriser l'accès à Google Drive",
                "authorization_url", authUrl
        ));
    }

    // -------------------------------------------------------------------------
    // Étape 2 : Callback — échange du code contre les tokens
    // -------------------------------------------------------------------------

    @Operation(
            summary = "Callback OAuth2 Google",
            description = """
                    Endpoint appelé automatiquement par Google après l'autorisation de l'utilisateur.
                    Échange le code d'autorisation contre un access_token et un refresh_token,
                    puis les stocke de façon persistante dans le dossier 'tokens/'.
                    
                    ⚠️ L'URI de redirection (http://localhost:8080/api/google/callback) doit être
                    ajoutée dans Google Cloud Console sous "URI de redirection autorisés".
                    """
    )
    @ApiResponse(responseCode = "200", description = "Autorisation réussie, tokens stockés")
    @ApiResponse(responseCode = "400", description = "Code d'autorisation invalide ou manquant")
    @ApiResponse(responseCode = "500", description = "Erreur lors de l'échange des tokens")
    @GetMapping("/callback")
    public ResponseEntity<Map<String, String>> callback(
            @Parameter(description = "Code d'autorisation retourné par Google", required = true)
            @RequestParam String code
    ) {
        try {
            credentialManager.exchangeCodeAndStore(code);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Google Drive autorisé avec succès ! Vous pouvez maintenant uploader des fichiers."
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Erreur lors de l'échange du code : " + e.getMessage()
            ));
        }
    }

    // -------------------------------------------------------------------------
    // Statut de l'autorisation
    // -------------------------------------------------------------------------

    @Operation(
            summary = "Vérifier le statut de l'autorisation Google Drive",
            description = "Indique si l'application est déjà autorisée à accéder à Google Drive"
    )
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> status() {
        boolean authorized = credentialManager.isAuthorized();
        return ResponseEntity.ok(Map.of(
                "authorized", authorized,
                "message", authorized
                        ? "Google Drive est connecté et prêt à l'emploi."
                        : "Non autorisé. Visitez GET /api/google/authorize pour démarrer le flux OAuth2."
        ));
    }
}

