package com.riadh.ecommerce.features.image.controller;

import com.riadh.ecommerce.features.image.dto.ImageCreateRequest;
import com.riadh.ecommerce.features.image.dto.ImageResponse;
import com.riadh.ecommerce.features.image.dto.ImageUpdateRequest;
import com.riadh.ecommerce.features.image.entity.Image;
import com.riadh.ecommerce.features.image.service.IImageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {

    @Autowired
    private IImageService imageService;

    @PostMapping
    public ImageResponse addImage(@Valid @RequestBody ImageCreateRequest dto) {
        return imageService.addImage(dto);
    }

    @GetMapping("/{id}")
    public ImageResponse getImageById(@PathVariable Long id) {
        return imageService.getImageById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteImageById(@PathVariable Long id) {
        imageService.deleteImageById(id);
    }

    @PutMapping("/{id}")
    public void updateImage(@Valid @RequestBody ImageUpdateRequest dto, @PathVariable Long id) {
        imageService.updateImage(dto, id);
    }

    @GetMapping
    public List<ImageResponse> getAllImages() {
        return imageService.getAllImages();
    }

    @PostMapping("/filter")
    public List<ImageResponse> getImagesWithFilters(@RequestBody Image image) {
        return imageService.getImagesWithFilters(image);
    }
}
