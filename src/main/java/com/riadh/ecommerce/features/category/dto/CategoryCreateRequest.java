package com.riadh.ecommerce.features.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreateRequest {

    @NotBlank(message = "Le nom de la catégorie est obligatoire")
    private String name;
}
