package com.riadh.ecommerce.features.produit.dto;

import com.riadh.ecommerce.features.category.dto.CategoryResponse;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private CategoryResponse category;
}


