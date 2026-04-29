package com.riadh.ecommerce.features.category.specification;

import com.riadh.ecommerce.features.category.entity.Category;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class CategorySpecification {
    public static Specification<Category> withFilters(Category category) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (category.getName() != null) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + category.getName().toLowerCase() + "%"));
            }
            // Ajoutez d'autres attributs si nécessaire
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

