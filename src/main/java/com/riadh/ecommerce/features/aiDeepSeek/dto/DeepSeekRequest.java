package com.riadh.ecommerce.features.aiDeepSeek.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeepSeekRequest {

    @NotBlank(message = "Le texte ne peut pas être vide")
    @Size(min = 10, max = 5000, message = "Le texte doit contenir entre 10 et 5000 caractères")
    @Schema(description = "Texte original à résumer", example = "Spring Boot is an open-source Java-based framework used to create micro-services...")
    private String text;
}

