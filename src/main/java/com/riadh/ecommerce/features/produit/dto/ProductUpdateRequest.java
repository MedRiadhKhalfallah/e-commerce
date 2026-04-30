package com.riadh.ecommerce.features.produit.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateRequest {

    private String name;

    private String description;

    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être supérieur à 0")
    private BigDecimal price;

    @Min(value = 0, message = "Le stock ne peut pas être négatif")
    private Integer stock;

    private Long categoryId;
}
