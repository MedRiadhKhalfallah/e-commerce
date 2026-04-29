package com.riadh.ecommerce.features.produit.service;

import com.riadh.ecommerce.common.exception.ElementNotFoundException;
import com.riadh.ecommerce.features.category.entity.Category;
import com.riadh.ecommerce.features.category.repository.CategoryRepository;
import com.riadh.ecommerce.features.produit.dto.ProductCreateRequest;
import com.riadh.ecommerce.features.produit.dto.ProductFilterRequest;
import com.riadh.ecommerce.features.produit.dto.ProductResponse;
import com.riadh.ecommerce.features.produit.dto.ProductUpdateRequest;
import com.riadh.ecommerce.features.produit.entity.Product;
import com.riadh.ecommerce.features.produit.mapper.ProductMapper;
import com.riadh.ecommerce.features.produit.repository.ProductRepository;
import com.riadh.ecommerce.features.produit.specification.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductMapper productMapper;

    @Override
    @Transactional
    public ProductResponse addProduct(ProductCreateRequest dto) {
        Product product = productMapper.toEntity(dto);
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ElementNotFoundException("Category", dto.getCategoryId()));
            product.setCategory(category);
        }
        return productMapper.toResponse(productRepository.save(product));
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Product", id));
        return productMapper.toResponse(product);
    }

    @Override
    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ElementNotFoundException("Product", id);
        }
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateProduct(ProductUpdateRequest dto, Long productId) {
        Product existing = productRepository.findById(productId)
                .orElseThrow(() -> new ElementNotFoundException("Product", productId));
        productMapper.updateProductFromDto(dto, existing);
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ElementNotFoundException("Category", dto.getCategoryId()));
            existing.setCategory(category);
        }
        productRepository.save(existing);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream().map(productMapper::toResponse).toList();
    }

    @Override
    public List<ProductResponse> getProductsByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId)
                .stream().map(productMapper::toResponse).toList();
    }

    @Override
    public List<ProductResponse> getProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name)
                .stream().map(productMapper::toResponse).toList();
    }

    @Override
    public Long countProductsByByCategoryId(Long categoryId) {
        return productRepository.countByCategoryId(categoryId);
    }

    @Override
    public List<ProductResponse> getProductsWithFilters(ProductFilterRequest filter) {
        return productRepository.findAll(ProductSpecification.withFilters(filter))
                .stream().map(productMapper::toResponse).toList();
    }
}
