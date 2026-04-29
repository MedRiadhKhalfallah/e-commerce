package com.riadh.ecommerce.features.category.controller;

import com.riadh.ecommerce.features.category.dto.CategoryCreateRequest;
import com.riadh.ecommerce.features.category.dto.CategoryResponse;
import com.riadh.ecommerce.features.category.dto.CategoryUpdateRequest;
import com.riadh.ecommerce.features.category.entity.Category;
import com.riadh.ecommerce.features.category.service.ICategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @PostMapping
    public CategoryResponse addCategory(@Valid @RequestBody CategoryCreateRequest dto) {
        return categoryService.addCategory(dto);
    }

    @GetMapping("/{id}")
    public CategoryResponse getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
    }

    @PutMapping("/{id}")
    public void updateCategory(@Valid @RequestBody CategoryUpdateRequest dto, @PathVariable Long id) {
        categoryService.updateCategory(dto, id);
    }

    @GetMapping
    public List<CategoryResponse> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping("/filter")
    public List<CategoryResponse> getCategoriesWithFilters(@RequestBody Category category) {
        return categoryService.getCategoriesWithFilters(category);
    }
}
