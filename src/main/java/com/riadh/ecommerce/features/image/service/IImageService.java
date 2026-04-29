package com.riadh.ecommerce.features.image.service;

import com.riadh.ecommerce.features.image.dto.ImageCreateRequest;
import com.riadh.ecommerce.features.image.dto.ImageResponse;
import com.riadh.ecommerce.features.image.dto.ImageUpdateRequest;
import com.riadh.ecommerce.features.image.entity.Image;
import java.util.List;

public interface IImageService {
    ImageResponse addImage(ImageCreateRequest dto);
    ImageResponse getImageById(Long id);
    void deleteImageById(Long id);
    void updateImage(ImageUpdateRequest dto, Long imageId);
    List<ImageResponse> getAllImages();
    List<ImageResponse> getImagesWithFilters(Image image);
}
