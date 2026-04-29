package com.riadh.ecommerce.features.image.specification;

import com.riadh.ecommerce.features.image.entity.Image;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ImageSpecification {
    public static Specification<Image> withFilters(Image image) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (image.getFileName() != null) {
                predicates.add(cb.like(cb.lower(root.get("fileName")), "%" + image.getFileName().toLowerCase() + "%"));
            }
            if (image.getFileType() != null) {
                predicates.add(cb.equal(root.get("fileType"), image.getFileType()));
            }
            if (image.getDownloadUrl() != null) {
                predicates.add(cb.like(cb.lower(root.get("downloadUrl")), "%" + image.getDownloadUrl().toLowerCase() + "%"));
            }
            if (image.getProduct() != null && image.getProduct().getId() != null) {
                predicates.add(cb.equal(root.get("product").get("id"), image.getProduct().getId()));
            }
            // Ajoutez d'autres attributs si nécessaire
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

