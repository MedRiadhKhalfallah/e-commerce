package com.riadh.ecommerce.features.produit.controller;

import com.riadh.ecommerce.features.produit.dto.ProductCreateRequest;
import com.riadh.ecommerce.features.produit.dto.ProductFilterRequest;
import com.riadh.ecommerce.features.produit.dto.ProductResponse;
import com.riadh.ecommerce.features.produit.dto.ProductUpdateRequest;
import com.riadh.ecommerce.features.produit.entity.Product;
import com.riadh.ecommerce.features.produit.service.IProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private IProductService productService;

    @PostMapping
    public ProductResponse addProduct(@Valid @RequestBody ProductCreateRequest dto) {
        return productService.addProduct(dto);
    }

    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
    }

    @PutMapping("/{id}")
    public void updateProduct(@Valid @RequestBody ProductUpdateRequest dto, @PathVariable Long id) {
        productService.updateProduct(dto, id);
    }

    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping("/filter")
    public List<ProductResponse> getProductsWithFilters(@RequestBody ProductFilterRequest filter) {
        return productService.getProductsWithFilters(filter);
    }
}
