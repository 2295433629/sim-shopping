package com.sim.shopping.interfaces.product;

import com.sim.shopping.application.product.ProductService;
import com.sim.shopping.infrastructure.persistence.entity.MerchantDO;
import com.sim.shopping.infrastructure.persistence.mapper.MerchantMapper;
import com.sim.shopping.infrastructure.security.SecurityUtils;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.product.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import org.springframework.security.access.prepost.PreAuthorize;

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
    private final MerchantMapper merchantMapper;

    public MerchantProductController(ProductService productService, MerchantMapper merchantMapper) {
        this.productService = productService;
        this.merchantMapper = merchantMapper;
    }

    /**
     * 发布商品
     * @param req req
     * @return 返回结果
     */
    @PostMapping
    public ApiResponse<Long> createProduct(@Valid @RequestBody ProductCreateRequest req) {
        Long merchantId = resolveMerchantId();
        return ApiResponse.success(productService.createProduct(merchantId, req));
    }

    /**
     * 编辑商品
     * @return 返回结果
     */
    @PutMapping("/{productId}")
    public ApiResponse<Void> updateProduct(@PathVariable Long productId,
                                            @Valid @RequestBody ProductUpdateRequest req) {
        Long merchantId = resolveMerchantId();
        productService.updateProduct(merchantId, productId, req);
        return ApiResponse.success();
    }

    /**
     * publish Product
     * @param productId productId
     * @return 返回结果
     */
    @PatchMapping("/{productId}/publish")
    public ApiResponse<Void> publishProduct(@PathVariable Long productId) {
        Long merchantId = resolveMerchantId();
        productService.publishProduct(merchantId, productId);
        return ApiResponse.success();
    }

    /**
     * offline Product
     * @param productId productId
     * @return 返回结果
     */
    @PatchMapping("/{productId}/offline")
    public ApiResponse<Void> offlineProduct(@PathVariable Long productId) {
        Long merchantId = resolveMerchantId();
        productService.offlineProduct(merchantId, productId);
        return ApiResponse.success();
    }

    /**
     * 删除商品
     * @param productId productId
     * @return 返回结果
     */
    @DeleteMapping("/{productId}")
    public ApiResponse<Void> deleteProduct(@PathVariable Long productId) {
        Long merchantId = resolveMerchantId();
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
        Long merchantId = resolveMerchantId();
        return ApiResponse.success(productService.getMerchantProducts(merchantId, page, size, status, keyword));
    }

    /**
     * 获取Merchant Product Detail
     * @param productId productId
     * @return 返回结果
     */
    @GetMapping("/{productId}")
    public ApiResponse<ProductDetailVO> getMerchantProductDetail(@PathVariable Long productId) {
        Long merchantId = resolveMerchantId();
        return ApiResponse.success(productService.getMerchantProductDetail(merchantId, productId));
    }

    private Long resolveMerchantId() {
        Long userId = SecurityUtils.getCurrentUserId();
        LambdaQueryWrapper<MerchantDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MerchantDO::getUserId, userId);
        MerchantDO merchant = merchantMapper.selectOne(wrapper);
        if (merchant == null) {
            throw new com.sim.shopping.domain.common.exception.BusinessException(403, "非商家用户");
        }
        return merchant.getId();
    }
}
