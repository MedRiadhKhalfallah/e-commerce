package com.riadh.ecommerce.features.produit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
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

