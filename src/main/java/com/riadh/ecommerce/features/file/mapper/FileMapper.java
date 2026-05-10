package com.riadh.ecommerce.features.file.mapper;

import com.riadh.ecommerce.features.file.dto.FileResponse;
import com.riadh.ecommerce.features.file.entity.File;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileMapper {

    FileResponse toResponse(File file);
}

