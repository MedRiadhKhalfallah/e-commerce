package com.riadh.ecommerce.features.file.dto;

import org.springframework.web.multipart.MultipartFile;

public record FileRequest(
        MultipartFile file,
        Long entityId,
        String pathType
) {}

