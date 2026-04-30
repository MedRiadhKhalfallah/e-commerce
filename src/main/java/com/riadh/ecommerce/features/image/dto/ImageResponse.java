package com.riadh.ecommerce.features.image.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageResponse {
    private Long id;
    private String fileName;
    private String fileType;
    private String downloadUrl;
    private Long productId;
}

