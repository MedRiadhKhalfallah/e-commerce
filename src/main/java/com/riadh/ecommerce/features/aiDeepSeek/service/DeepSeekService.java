package com.riadh.ecommerce.features.aiDeepSeek.service;

import com.riadh.ecommerce.common.exception.ElementNotFoundException;
import com.riadh.ecommerce.features.aiDeepSeek.dto.DeepSeekRequest;
import com.riadh.ecommerce.features.aiDeepSeek.dto.DeepSeekResponse;
import com.riadh.ecommerce.features.aiDeepSeek.entity.DeepSeekRecord;
import com.riadh.ecommerce.features.aiDeepSeek.repository.DeepSeekRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class DeepSeekService implements IDeepSeekService {

    private final ChatClient chatClient;
    private final DeepSeekRecordRepository deepSeekRecordRepository;

    public DeepSeekService(@Qualifier("deepSeekChatClient") ChatClient chatClient,
                           DeepSeekRecordRepository deepSeekRecordRepository) {
        this.chatClient = chatClient;
        this.deepSeekRecordRepository = deepSeekRecordRepository;
    }

    @Override
    public DeepSeekResponse summarize(DeepSeekRequest request) {
        log.info("[DeepSeek] Demande de résumé pour un texte de {} caractères", request.getText().length());

        String prompt = """
Tu es un assistant spécialisé dans l’analyse d’annonces produits...

Contenu :                %s
Format de reponse : format html pour qu'il sera facile a afficher dasn l'interface avec un style modern et responsive
                """.formatted(request.getText());

        String summary = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        log.info("[DeepSeek] Résumé généré avec succès");

        DeepSeekRecord record = DeepSeekRecord.builder()
                .originalText(request.getText())
                .summary(summary)
                .createdAt(LocalDateTime.now())
                .build();

        DeepSeekRecord saved = deepSeekRecordRepository.save(record);
        log.info("[DeepSeek] Résumé sauvegardé en base avec id={}", saved.getId());

        return toResponse(saved);
    }

    @Override
    public List<DeepSeekResponse> getAll() {
        return deepSeekRecordRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public DeepSeekResponse getById(Long id) {
        return deepSeekRecordRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> {
                    log.warn("[DeepSeek] Résumé introuvable : id={}", id);
                    return new ElementNotFoundException("DeepSeekRecord", id);
                });
    }

    private DeepSeekResponse toResponse(DeepSeekRecord record) {
        return new DeepSeekResponse(
                record.getId(),
                record.getOriginalText(),
                record.getSummary(),
                record.getCreatedAt()
        );
    }
}

