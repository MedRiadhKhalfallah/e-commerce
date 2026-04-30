package com.riadh.ecommerce.features.produit.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductFilterRequest {

    private String name;

    private String description;

    // Filtre par égalité exacte
    private BigDecimal price;

    // Filtre par plage de prix
    private BigDecimal priceMin;
    private BigDecimal priceMax;

    // Filtre par égalité exacte
    private Integer stock;

    // Filtre par plage de stock
    private Integer stockMin;
    private Integer stockMax;

    // Filtre par catégorie
    private Long categoryId;
}

