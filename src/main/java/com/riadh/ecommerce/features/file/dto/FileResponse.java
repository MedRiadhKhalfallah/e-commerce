package com.riadh.ecommerce.features.file.dto;

public record FileResponse(
        Long id,
        String fileName,
        String fileDownloadUri,
        String fileType,
        String extension,
        long size,
        String pathType,
        Long entityId
) {}

