package com.riadh.ecommerce.features.category.service;

import com.riadh.ecommerce.features.category.dto.CategoryCreateRequest;
import com.riadh.ecommerce.features.category.dto.CategoryResponse;
import com.riadh.ecommerce.features.category.dto.CategoryUpdateRequest;
import com.riadh.ecommerce.features.category.entity.Category;
import java.util.List;

public interface ICategoryService {
    CategoryResponse addCategory(CategoryCreateRequest dto);
    CategoryResponse getCategoryById(Long id);
    void deleteCategoryById(Long id);
    void updateCategory(CategoryUpdateRequest dto, Long categoryId);
    List<CategoryResponse> getAllCategories();
    List<CategoryResponse> getCategoriesWithFilters(Category category);
}

