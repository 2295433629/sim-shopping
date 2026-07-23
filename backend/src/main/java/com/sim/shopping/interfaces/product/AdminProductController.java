package com.sim.shopping.interfaces.product;

import com.sim.shopping.application.product.ProductService;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.product.ProductDetailVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.sim.shopping.infrastructure.aop.Log;

/**
 * AdminProduct控制器，处理相关业务请求
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/admin/products")
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 查询全平台商品列表（管理员）
     * @return 返回结果
     */
    @GetMapping
    public ApiResponse<PageResponse<ProductDetailVO>> getAdminProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long shopId,
            @RequestParam(required = false) String keyword) {
        return ApiResponse.success(productService.getAdminProducts(page, size, status, shopId, keyword));
    }

    /**
     * 获取Admin Product Detail
     * @param productId productId
     * @return 返回结果
     */
    @GetMapping("/{productId}")
    public ApiResponse<ProductDetailVO> getAdminProductDetail(@PathVariable Long productId) {
        return ApiResponse.success(productService.getAdminProductDetail(productId));
    }

    /**
     * force Offline
     * @param productId productId
     * @return 返回结果
     */
    @PatchMapping("/{productId}/force-offline")
    @Log(module = "商品", type = "修改")
    public ApiResponse<Void> forceOffline(@PathVariable Long productId) {
        productService.forceOffline(productId);
        return ApiResponse.success();
    }
}
