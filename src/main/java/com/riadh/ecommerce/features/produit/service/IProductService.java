package com.riadh.ecommerce.features.produit.service;

import com.riadh.ecommerce.features.produit.dto.ProductCreateRequest;
import com.riadh.ecommerce.features.produit.dto.ProductFilterRequest;
import com.riadh.ecommerce.features.produit.dto.ProductResponse;
import com.riadh.ecommerce.features.produit.dto.ProductUpdateRequest;

import java.util.List;

public interface IProductService {
    ProductResponse addProduct(ProductCreateRequest dto);
    ProductResponse getProductById(Long id);
    void deleteProductById(Long id);
    void updateProduct(ProductUpdateRequest dto, Long productId);
    List<ProductResponse> getAllProducts();
    List<ProductResponse> getProductsByCategoryId(Long categoryId);
    List<ProductResponse> getProductsByName(String name);
    Long countProductsByByCategoryId(Long categoryId);
    List<ProductResponse> getProductsWithFilters(ProductFilterRequest filter);
}
