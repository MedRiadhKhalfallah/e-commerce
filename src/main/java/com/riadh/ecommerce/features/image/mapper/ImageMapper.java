package com.riadh.ecommerce.features.image.mapper;

import com.riadh.ecommerce.features.image.dto.ImageCreateRequest;
import com.riadh.ecommerce.features.image.dto.ImageResponse;
import com.riadh.ecommerce.features.image.dto.ImageUpdateRequest;
import com.riadh.ecommerce.features.image.entity.Image;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    Image toEntity(ImageCreateRequest dto);

    @Mapping(source = "product.id", target = "productId")
    ImageResponse toResponse(Image entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    void updateImageFromDto(ImageUpdateRequest dto, @MappingTarget Image entity);

    /**
     * Conversion byte[] -> java.sql.Blob utilisée automatiquement par MapStruct.
     */
    default Blob map(byte[] value) {
        if (value == null) return null;
        try {
            return new SerialBlob(value);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la conversion byte[] -> Blob", e);
        }
    }
}






