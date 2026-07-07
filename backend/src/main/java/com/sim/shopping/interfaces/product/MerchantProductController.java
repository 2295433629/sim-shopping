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

@RestController
@RequestMapping("/api/merchant/products")
public class MerchantProductController {

    private final ProductService productService;
    private final MerchantMapper merchantMapper;

    public MerchantProductController(ProductService productService, MerchantMapper merchantMapper) {
        this.productService = productService;
        this.merchantMapper = merchantMapper;
    }

    @PostMapping
    public ApiResponse<Long> createProduct(@Valid @RequestBody ProductCreateRequest req) {
        Long merchantId = resolveMerchantId();
        return ApiResponse.success(productService.createProduct(merchantId, req));
    }

    @PutMapping("/{productId}")
    public ApiResponse<Void> updateProduct(@PathVariable Long productId,
                                            @Valid @RequestBody ProductUpdateRequest req) {
        Long merchantId = resolveMerchantId();
        productService.updateProduct(merchantId, productId, req);
        return ApiResponse.success();
    }

    @PatchMapping("/{productId}/publish")
    public ApiResponse<Void> publishProduct(@PathVariable Long productId) {
        Long merchantId = resolveMerchantId();
        productService.publishProduct(merchantId, productId);
        return ApiResponse.success();
    }

    @PatchMapping("/{productId}/offline")
    public ApiResponse<Void> offlineProduct(@PathVariable Long productId) {
        Long merchantId = resolveMerchantId();
        productService.offlineProduct(merchantId, productId);
        return ApiResponse.success();
    }

    @DeleteMapping("/{productId}")
    public ApiResponse<Void> deleteProduct(@PathVariable Long productId) {
        Long merchantId = resolveMerchantId();
        productService.deleteProduct(merchantId, productId);
        return ApiResponse.success();
    }

    @GetMapping
    public ApiResponse<PageResponse<ProductDetailVO>> getMerchantProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword) {
        Long merchantId = resolveMerchantId();
        return ApiResponse.success(productService.getMerchantProducts(merchantId, page, size, status, keyword));
    }

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
