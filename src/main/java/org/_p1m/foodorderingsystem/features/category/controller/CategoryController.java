package org._p1m.foodorderingsystem.features.category.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.config.response.util.ResponseUtils;
import org._p1m.foodorderingsystem.features.category.dto.request.CreateCategoryRequest;
import org._p1m.foodorderingsystem.features.category.dto.request.UpdateCategoryRequest;
import org._p1m.foodorderingsystem.features.category.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.base.path}/category")
@Tag(name = "Category API", description = "Endpoints for managing category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }



    @PostMapping
    @Operation(
            summary = "Create a new category",
            description = "create a new category for restaurant",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Create Category request",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateCategoryRequest.class))
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Category created successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    public ResponseEntity<ApiResponse> createCategory(@Valid @RequestBody CreateCategoryRequest createCategoryRequest, HttpServletRequest request){
        final ApiResponse response = this.categoryService.createCategory(createCategoryRequest);
        return ResponseUtils.buildResponse(request, response);
    }


    @GetMapping
    @Operation(
            summary = "Get all categories by restaurant id",
            description = "Retrieve a list of all categories",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Categories retrieved successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request")

            }
    )
    public ResponseEntity<ApiResponse> getAllCategories(@RequestParam(name = "restaurantId") Long restaurantId,HttpServletRequest request) {
        final ApiResponse response = this.categoryService.getAllCategoriesByResId(restaurantId);
        return ResponseUtils.buildResponse(request, response);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete category by ID",
            description = "Delete a category using its ID",
            parameters = {
                    @Parameter(name = "id", description = "ID of the category to delete", required = true)
            },
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category deleted successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category not found"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
            }
    )
    public ResponseEntity<ApiResponse> deleteCategoryById(@PathVariable(name = "id") Long id, HttpServletRequest request) {
        final ApiResponse response = this.categoryService.deleteCategoryById(id);
        return ResponseUtils.buildResponse(request, response);
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Update category by ID",
            description = "Update the category details using its ID",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Update Category request",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateCategoryRequest.class))
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category updated successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category not found"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable(name = "id") Long id,
                                                      @Valid @RequestBody UpdateCategoryRequest updateCategoryRequest,
                                                      HttpServletRequest request) {
        final ApiResponse response = this.categoryService.updateCategory(id, updateCategoryRequest);
        return ResponseUtils.buildResponse(request, response);
    }
}
