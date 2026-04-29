package com.riadh.ecommerce.features.category.service;

import com.riadh.ecommerce.common.exception.ElementNotFoundException;
import com.riadh.ecommerce.features.category.dto.CategoryCreateRequest;
import com.riadh.ecommerce.features.category.dto.CategoryResponse;
import com.riadh.ecommerce.features.category.dto.CategoryUpdateRequest;
import com.riadh.ecommerce.features.category.entity.Category;
import com.riadh.ecommerce.features.category.mapper.CategoryMapper;
import com.riadh.ecommerce.features.category.repository.CategoryRepository;
import com.riadh.ecommerce.features.category.specification.CategorySpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public CategoryResponse addCategory(CategoryCreateRequest dto) {
        Category category = categoryMapper.toEntity(dto);
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Category", id));
        return categoryMapper.toResponse(category);
    }

    @Override
    public void deleteCategoryById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ElementNotFoundException("Category", id);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateCategory(CategoryUpdateRequest dto, Long categoryId) {
        Category existing = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ElementNotFoundException("Category", categoryId));
        categoryMapper.updateCategoryFromDto(dto, existing);
        categoryRepository.save(existing);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream().map(categoryMapper::toResponse).toList();
    }

    @Override
    public List<CategoryResponse> getCategoriesWithFilters(Category category) {
        return categoryRepository.findAll(CategorySpecification.withFilters(category))
                .stream().map(categoryMapper::toResponse).toList();
    }
}
