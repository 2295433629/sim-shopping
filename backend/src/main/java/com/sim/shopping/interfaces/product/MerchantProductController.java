package com.sim.shopping.interfaces.product;

import com.sim.shopping.application.merchant.MerchantService;
import com.sim.shopping.application.product.ProductService;
import com.sim.shopping.infrastructure.security.SecurityUtils;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.product.*;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;
import com.sim.shopping.infrastructure.aop.Log;

/**
 * MerchantProduct控制器，处理相关业务请求
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/merchant/products")
@PreAuthorize("hasRole('MERCHANT')")
public class MerchantProductController {

    private final ProductService productService;
    private final MerchantService merchantService;

    public MerchantProductController(ProductService productService, MerchantService merchantService) {
        this.productService = productService;
        this.merchantService = merchantService;
    }

    /**
     * 发布商品
     * @param req req
     * @return 返回结果
     */
    @PostMapping
    @Log(module = "商品", type = "新增")
    public ApiResponse<Long> createProduct(@Valid @RequestBody ProductCreateRequest req) {
        Long userId = SecurityUtils.getCurrentUserId();
        Long merchantId = merchantService.getMerchantIdByUserId(userId);
        return ApiResponse.success(productService.createProduct(merchantId, req));
    }

    /**
     * 编辑商品
     * @return 返回结果
     */
    @PutMapping("/{productId}")
    @Log(module = "商品", type = "修改")
    public ApiResponse<Void> updateProduct(@PathVariable Long productId,
                                            @Valid @RequestBody ProductUpdateRequest req) {
        Long userId = SecurityUtils.getCurrentUserId();
        Long merchantId = merchantService.getMerchantIdByUserId(userId);
        productService.updateProduct(merchantId, productId, req);
        return ApiResponse.success();
    }

    /**
     * publish Product
     * @param productId productId
     * @return 返回结果
     */
    @PatchMapping("/{productId}/publish")
    @Log(module = "商品", type = "修改")
    public ApiResponse<Void> publishProduct(@PathVariable Long productId) {
        Long userId = SecurityUtils.getCurrentUserId();
        Long merchantId = merchantService.getMerchantIdByUserId(userId);
        productService.publishProduct(merchantId, productId);
        return ApiResponse.success();
    }

    /**
     * offline Product
     * @param productId productId
     * @return 返回结果
     */
    @PatchMapping("/{productId}/offline")
    @Log(module = "商品", type = "修改")
    public ApiResponse<Void> offlineProduct(@PathVariable Long productId) {
        Long userId = SecurityUtils.getCurrentUserId();
        Long merchantId = merchantService.getMerchantIdByUserId(userId);
        productService.offlineProduct(merchantId, productId);
        return ApiResponse.success();
    }

    /**
     * 删除商品
     * @param productId productId
     * @return 返回结果
     */
    @DeleteMapping("/{productId}")
    @Log(module = "商品", type = "删除")
    public ApiResponse<Void> deleteProduct(@PathVariable Long productId) {
        Long userId = SecurityUtils.getCurrentUserId();
        Long merchantId = merchantService.getMerchantIdByUserId(userId);
        productService.deleteProduct(merchantId, productId);
        return ApiResponse.success();
    }

    /**
     * 查询商家商品列表
     * @return 返回结果
     */
    @GetMapping
    public ApiResponse<PageResponse<ProductDetailVO>> getMerchantProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword) {
        Long userId = SecurityUtils.getCurrentUserId();
        Long merchantId = merchantService.getMerchantIdByUserId(userId);
        return ApiResponse.success(productService.getMerchantProducts(merchantId, page, size, status, keyword));
    }

    /**
     * 获取Merchant Product Detail
     * @param productId productId
     * @return 返回结果
     */
    @GetMapping("/{productId}")
    public ApiResponse<ProductDetailVO> getMerchantProductDetail(@PathVariable Long productId) {
        Long userId = SecurityUtils.getCurrentUserId();
        Long merchantId = merchantService.getMerchantIdByUserId(userId);
        return ApiResponse.success(productService.getMerchantProductDetail(merchantId, productId));
    }
}
