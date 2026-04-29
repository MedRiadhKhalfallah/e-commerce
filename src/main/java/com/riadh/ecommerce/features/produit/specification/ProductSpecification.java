package com.riadh.ecommerce.features.produit.specification;

import com.riadh.ecommerce.features.produit.dto.ProductFilterRequest;
import com.riadh.ecommerce.features.produit.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<Product> withFilters(ProductFilterRequest filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getName() != null && !filter.getName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
            }
            if (filter.getDescription() != null && !filter.getDescription().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("description")), "%" + filter.getDescription().toLowerCase() + "%"));
            }
            // Prix exact
            if (filter.getPrice() != null) {
                predicates.add(cb.equal(root.get("price"), filter.getPrice()));
            }
            // Plage de prix
            if (filter.getPriceMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), filter.getPriceMin()));
            }
            if (filter.getPriceMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), filter.getPriceMax()));
            }
            // Stock exact
            if (filter.getStock() != null) {
                predicates.add(cb.equal(root.get("stock"), filter.getStock()));
            }
            // Plage de stock
            if (filter.getStockMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("stock"), filter.getStockMin()));
            }
            if (filter.getStockMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("stock"), filter.getStockMax()));
            }
            // Catégorie
            if (filter.getCategoryId() != null) {
                predicates.add(cb.equal(root.get("category").get("id"), filter.getCategoryId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
