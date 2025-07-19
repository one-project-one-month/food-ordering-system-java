package org._p1m.foodorderingsystem.features.category.service;

import jakarta.validation.Valid;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.features.category.dto.request.CreateCategoryRequest;
import org._p1m.foodorderingsystem.features.category.dto.request.UpdateCategoryRequest;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {
    ApiResponse createCategory(@Valid CreateCategoryRequest createCategoryRequest);

    ApiResponse getAllCategoriesByResId(Long restaurantId);

    ApiResponse deleteCategoryById(Long id);

    ApiResponse updateCategory(Long id, @Valid UpdateCategoryRequest updateCategoryRequest);
}
