package com.riadh.ecommerce.features.file.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StorageFactory {

    @Autowired
    @Qualifier("localStorage")
    private IFileStorageService localStorage;

    @Autowired
    @Qualifier("s3Storage")
    private IFileStorageService s3Storage;

    @Autowired
    @Qualifier("googleDriveStorage")
    private IFileStorageService googleDriveStorage;

    @Value("${storage.provider}")
    private String provider;

    /**
     * Retourne le service de stockage configuré via storage.provider dans application.yaml.
     *
     * Valeurs possibles :
     *  - "local"       → stockage local sur le serveur (défaut)
     *  - "s3"          → Amazon S3
     *  - "googleDrive" → Google Drive
     */
    public IFileStorageService getStorage() {

        return switch (provider) {
            case "s3"          -> s3Storage;
            case "googleDrive" -> googleDriveStorage;
            default            -> localStorage;
        };
    }
}
