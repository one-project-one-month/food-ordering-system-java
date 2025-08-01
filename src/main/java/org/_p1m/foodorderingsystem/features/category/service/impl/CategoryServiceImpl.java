package org._p1m.foodorderingsystem.features.category.service.impl;

import lombok.RequiredArgsConstructor;
import org._p1m.foodorderingsystem.config.exceptions.EntityNotFoundException;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.features.category.dto.reponse.CategoryResponseDto;
import org._p1m.foodorderingsystem.features.category.dto.request.CreateCategoryRequest;
import org._p1m.foodorderingsystem.features.category.dto.request.UpdateCategoryRequest;
import org._p1m.foodorderingsystem.features.category.repository.CategoryRepository;
import org._p1m.foodorderingsystem.features.category.service.CategoryService;
import org._p1m.foodorderingsystem.features.menu.dto.responses.MenuResponseDto;
import org._p1m.foodorderingsystem.features.restaurant.repository.RestaurantRepository;
import org._p1m.foodorderingsystem.model.Category;
import org._p1m.foodorderingsystem.model.Restaurant;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;

    @Override
    public ApiResponse createCategory(CreateCategoryRequest createCategoryRequest) {
        Restaurant restaurant = this.restaurantRepository.findById(createCategoryRequest.getRestaurantId())
                .orElseThrow(()-> new EntityNotFoundException("Restaurant not found"));

        Category category = new Category();
        category.setName(createCategoryRequest.getName());
        category.setRestaurant(restaurant);

        this.categoryRepository.save(category);

        CategoryResponseDto dto = modelMapper.map(category, CategoryResponseDto.class);
        return ApiResponse.builder().success(1).code(HttpStatus.CREATED.value())
                .data(Map.of("created Category", dto))
                .message("Category created successful.").build();
    }

    @Override
    public ApiResponse getAllCategoriesByResId(Long restaurantId) {

        List<Category> categories = this.categoryRepository.findByRestaurantId(restaurantId);
        List<CategoryResponseDto> dtos = new ArrayList<>();
        if (CollectionUtils.isEmpty(categories)) {
            System.out.println("No categories found for restaurant with id: " + restaurantId + ".");
        } else {
            dtos = categories.stream()
                    .map(category -> CategoryResponseDto.builder()
                            .id(category.getId())
                            .name(category.getName())
                            .restaurantId(category.getRestaurant().getId())
                            .build())
                    .toList();
        }
        return ApiResponse.builder().success(1).code(HttpStatus.OK.value())
                .data(Map.of("categories", dtos))
                .message("Fetch all categories successful.").build();

    }

    @Override
    public ApiResponse deleteCategoryById(Long id) {
        Category category = this.categoryRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Category not found exception"));
        this.categoryRepository.delete(category);
        return ApiResponse.builder().success(1)
                .code(HttpStatus.OK.value())
                .message("Category Deleted successful.").build();
    }

    @Override
    public ApiResponse updateCategory(Long id, UpdateCategoryRequest updateCategoryRequest) {
        Category category = this.categoryRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Category not found exception"));

        category.setName(updateCategoryRequest.getName());
        this.categoryRepository.save(category);
        CategoryResponseDto dto = modelMapper.map(category, CategoryResponseDto.class);
        return ApiResponse.builder().success(1).code(HttpStatus.OK.value())
                .data(Map.of("updated Category", dto))
                .message("Category updated successful.").build();
    }
}
