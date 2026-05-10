package com.riadh.ecommerce.features.file.service;

import com.riadh.ecommerce.features.file.dto.FileRequest;
import com.riadh.ecommerce.features.file.dto.FileResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IFileStorageService {

    String uploadFile(
            MultipartFile file,
            Long id,
            String pathType
    );

    byte[] downloadFile(
            Long id
    );

    void deleteFile(
            String fileName,
            Long id,
            String pathType
    );

    /**
     * Traite l'upload complet : stocke le fichier, persiste l'entité en base
     * et retourne le DTO de réponse.
     *
     * @param request demande contenant le fichier, l'identifiant et le type de chemin
     * @param baseUrl URL de base du contexte courant (ex: http://localhost:8080)
     * @return FileResponse avec l'URI de téléchargement
     */
    FileResponse processUpload(FileRequest request, String baseUrl);

    /**
     * Supprime le fichier du stockage et l'entité associée en base de données.
     *
     * @param id identifiant de l'entité File en base
     */
    void processDelete(Long id);
}
