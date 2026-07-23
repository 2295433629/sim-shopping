package com.sim.shopping.interfaces.product;

import com.sim.shopping.application.product.CategoryService;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.product.CategoryRequest;
import com.sim.shopping.interfaces.dto.product.CategoryResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.sim.shopping.infrastructure.aop.Log;

/**
 * 商品分类管理控制器，处理分类的增删改查和层级管理
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 获取Public Category Tree
     * @return 返回结果
     */
    @GetMapping("/public/categories")
    public ApiResponse<List<CategoryService.CategoryNode>> getPublicCategoryTree() {
        return ApiResponse.success(categoryService.getCategoryTree());
    }

    /**
     * 获取Admin Category Tree
     * @return 返回结果
     */
    @GetMapping("/admin/categories")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<CategoryService.CategoryNode>> getAdminCategoryTree() {
        return ApiResponse.success(categoryService.getCategoryTree());
    }

    /**
     * 创建分类
     * @param request request
     * @return 返回结果
     */
    @PostMapping("/admin/categories")
    @Log(module = "商品", type = "新增")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        return ApiResponse.success(categoryService.createCategory(request));
    }

    /**
     * 更新分类
     * @param categoryId categoryId
     * @param request request
     * @return 返回结果
     */
    @PutMapping("/admin/categories/{categoryId}")
    @Log(module = "商品", type = "修改")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CategoryResponse> updateCategory(@PathVariable Long categoryId,
                                                   @Valid @RequestBody CategoryRequest request) {
        return ApiResponse.success(categoryService.updateCategory(categoryId, request));
    }

    /**
     * 删除分类
     * @param categoryId categoryId
     * @return 返回结果
     */
    @DeleteMapping("/admin/categories/{categoryId}")
    @Log(module = "商品", type = "删除")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ApiResponse.success();
    }
}
