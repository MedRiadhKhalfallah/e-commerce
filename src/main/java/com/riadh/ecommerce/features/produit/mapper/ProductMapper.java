package com.riadh.ecommerce.features.produit.mapper;

import com.riadh.ecommerce.features.category.mapper.CategoryMapper;
import com.riadh.ecommerce.features.produit.dto.ProductCreateRequest;
import com.riadh.ecommerce.features.produit.dto.ProductResponse;
import com.riadh.ecommerce.features.produit.dto.ProductUpdateRequest;
import com.riadh.ecommerce.features.produit.entity.Product;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "images", ignore = true)
    Product toEntity(ProductCreateRequest dto);

    ProductResponse toResponse(Product entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "images", ignore = true)
    void updateProductFromDto(ProductUpdateRequest dto, @MappingTarget Product entity);
}








