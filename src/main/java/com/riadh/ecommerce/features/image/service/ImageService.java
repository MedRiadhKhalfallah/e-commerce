package com.riadh.ecommerce.features.image.service;

import com.riadh.ecommerce.common.exception.ElementNotFoundException;
import com.riadh.ecommerce.features.image.dto.ImageCreateRequest;
import com.riadh.ecommerce.features.image.dto.ImageResponse;
import com.riadh.ecommerce.features.image.dto.ImageUpdateRequest;
import com.riadh.ecommerce.features.image.entity.Image;
import com.riadh.ecommerce.features.image.mapper.ImageMapper;
import com.riadh.ecommerce.features.image.repository.ImageRepository;
import com.riadh.ecommerce.features.image.specification.ImageSpecification;
import com.riadh.ecommerce.features.produit.entity.Product;
import com.riadh.ecommerce.features.produit.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ImageService implements IImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageMapper imageMapper;

    @Override
    @Transactional
    public ImageResponse addImage(ImageCreateRequest dto) {
        Image image = imageMapper.toEntity(dto);
        if (dto.getProductId() != null) {
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new ElementNotFoundException("Product", dto.getProductId()));
            image.setProduct(product);
        }
        return imageMapper.toResponse(imageRepository.save(image));
    }

    @Override
    public ImageResponse getImageById(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Image", id));
        return imageMapper.toResponse(image);
    }

    @Override
    public void deleteImageById(Long id) {
        if (!imageRepository.existsById(id)) {
            throw new ElementNotFoundException("Image", id);
        }
        imageRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateImage(ImageUpdateRequest dto, Long imageId) {
        Image existingImage = imageRepository.findById(imageId)
                .orElseThrow(() -> new ElementNotFoundException("Image", imageId));
        imageMapper.updateImageFromDto(dto, existingImage);
        if (dto.getProductId() != null) {
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new ElementNotFoundException("Product", dto.getProductId()));
            existingImage.setProduct(product);
        }
        imageRepository.save(existingImage);
    }

    @Override
    public List<ImageResponse> getAllImages() {
        return imageRepository.findAll()
                .stream().map(imageMapper::toResponse).toList();
    }

    @Override
    public List<ImageResponse> getImagesWithFilters(Image image) {
        return imageRepository.findAll(ImageSpecification.withFilters(image))
                .stream().map(imageMapper::toResponse).toList();
    }
}
