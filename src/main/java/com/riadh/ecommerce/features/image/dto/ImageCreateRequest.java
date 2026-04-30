package com.riadh.ecommerce.features.image.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageCreateRequest {

    @NotBlank(message = "Le nom du fichier est obligatoire")
    private String fileName;

    @NotBlank(message = "Le type du fichier est obligatoire")
    private String fileType;

    private byte[] image;

    private String downloadUrl;

    @NotNull(message = "Le produit associé est obligatoire")
    private Long productId;
}
