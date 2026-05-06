package com.riadh.ecommerce.features.aiDeepSeek.controller;

import com.riadh.ecommerce.features.aiDeepSeek.dto.DeepSeekRequest;
import com.riadh.ecommerce.features.aiDeepSeek.dto.DeepSeekResponse;
import com.riadh.ecommerce.features.aiDeepSeek.service.IDeepSeekService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/ai/deepseek")
@RequiredArgsConstructor
@Tag(name = "AI DeepSeek", description = "Résumé de texte via DeepSeek (API compatible OpenAI)")
public class DeepSeekController {

    private final IDeepSeekService deepSeekService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Générer un résumé avec DeepSeek",
            description = "Envoie un texte au modèle DeepSeek, génère un résumé et le sauvegarde en base de données"
    )
    public DeepSeekResponse summarize(@Valid @RequestBody DeepSeekRequest request) {
        return deepSeekService.summarize(request);
    }

    @GetMapping
    @Operation(summary = "Lister tous les résumés DeepSeek", description = "Retourne l'ensemble des résumés générés par DeepSeek")
    public List<DeepSeekResponse> getAll() {
        return deepSeekService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un résumé DeepSeek par ID", description = "Retourne un résumé spécifique par son identifiant")
    public DeepSeekResponse getById(@PathVariable Long id) {
        return deepSeekService.getById(id);
    }
}

