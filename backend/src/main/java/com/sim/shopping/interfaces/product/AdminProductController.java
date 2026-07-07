package com.sim.shopping.interfaces.product;

import com.sim.shopping.application.product.ProductService;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.product.ProductDetailVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/products")
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ApiResponse<PageResponse<ProductDetailVO>> getAdminProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long shopId,
            @RequestParam(required = false) String keyword) {
        return ApiResponse.success(productService.getAdminProducts(page, size, status, shopId, keyword));
    }

    @GetMapping("/{productId}")
    public ApiResponse<ProductDetailVO> getAdminProductDetail(@PathVariable Long productId) {
        return ApiResponse.success(productService.getAdminProductDetail(productId));
    }

    @PatchMapping("/{productId}/force-offline")
    public ApiResponse<Void> forceOffline(@PathVariable Long productId) {
        productService.forceOffline(productId);
        return ApiResponse.success();
    }
}
