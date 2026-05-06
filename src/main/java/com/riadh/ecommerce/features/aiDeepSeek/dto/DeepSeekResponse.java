package com.riadh.ecommerce.features.aiDeepSeek.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Réponse du résumé DeepSeek")
public record DeepSeekResponse(

        @Schema(description = "Identifiant du résumé sauvegardé")
        Long id,

        @Schema(description = "Texte original soumis")
        String originalText,

        @Schema(description = "Résumé généré par DeepSeek")
        String summary,

        @Schema(description = "Date de création")
        LocalDateTime createdAt
) {}

