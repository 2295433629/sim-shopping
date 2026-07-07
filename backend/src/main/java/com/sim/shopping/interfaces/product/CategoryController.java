package com.sim.shopping.interfaces.product;

import com.sim.shopping.application.product.CategoryService;
import com.sim.shopping.infrastructure.persistence.entity.CategoryDO;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('ADMIN')")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/categories")
    public ApiResponse<List<CategoryService.CategoryNode>> getPublicCategoryTree() {
        return ApiResponse.success(categoryService.getCategoryTree());
    }

    @GetMapping("/admin/categories")
    public ApiResponse<List<CategoryService.CategoryNode>> getAdminCategoryTree() {
        return ApiResponse.success(categoryService.getCategoryTree());
    }

    @PostMapping("/admin/categories")
    public ApiResponse<CategoryDO> createCategory(@Valid @RequestBody CategoryDO category) {
        return ApiResponse.success(categoryService.createCategory(category));
    }

    @PutMapping("/admin/categories/{categoryId}")
    public ApiResponse<CategoryDO> updateCategory(@PathVariable Long categoryId,
                                                   @Valid @RequestBody CategoryDO category) {
        return ApiResponse.success(categoryService.updateCategory(categoryId, category));
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ApiResponse<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ApiResponse.success();
    }
}
