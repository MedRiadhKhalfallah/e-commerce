package com.riadh.ecommerce.features.category.mapper;

import com.riadh.ecommerce.features.category.dto.CategoryCreateRequest;
import com.riadh.ecommerce.features.category.dto.CategoryResponse;
import com.riadh.ecommerce.features.category.dto.CategoryUpdateRequest;
import com.riadh.ecommerce.features.category.entity.Category;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    Category toEntity(CategoryCreateRequest dto);

    CategoryResponse toResponse(Category entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    void updateCategoryFromDto(CategoryUpdateRequest dto, @MappingTarget Category entity);
}





