package com.riadh.ecommerce.features.image.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageUpdateRequest {
    private String fileName;
    private String fileType;
    private byte[] image;
    private String downloadUrl;
    private Long productId;
}

