package com.riadh.ecommerce.features.image.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageUpdateRequest {
    private String fileName;
    private String fileType;
    private byte[] image;
    private String downloadUrl;
    private Long productId;
}

