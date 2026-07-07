package com.sim.shopping.interfaces.shop;

import com.sim.shopping.application.shop.ShopService;
import com.sim.shopping.infrastructure.persistence.entity.ProductDO;
import com.sim.shopping.infrastructure.security.SecurityUtils;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.shop.DashboardResponse;
import com.sim.shopping.interfaces.dto.shop.ShopBannerRequest;
import com.sim.shopping.interfaces.dto.shop.ShopBannerResponse;
import com.sim.shopping.interfaces.dto.shop.ShopResponse;
import com.sim.shopping.interfaces.dto.shop.ShopUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class ShopController {

    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    // ===== Merchant shop endpoints (requires login + merchant identity) =====

    @GetMapping("/api/merchant/shop")
    public ApiResponse<ShopResponse> getShopInfo() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(shopService.getShopInfo(userId));
    }

    @PutMapping("/api/merchant/shop")
    public ApiResponse<ShopResponse> updateShopInfo(@Valid @RequestBody ShopUpdateRequest req) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(shopService.updateShopInfo(userId, req));
    }

    @GetMapping("/api/merchant/dashboard")
    public ApiResponse<DashboardResponse> getDashboard() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(shopService.getDashboard(userId));
    }

    @PostMapping("/api/merchant/shop/banners")
    public ApiResponse<ShopBannerResponse> addBanner(@Valid @RequestBody ShopBannerRequest req) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(shopService.addBanner(userId, req));
    }

    @DeleteMapping("/api/merchant/shop/banners/{bannerId}")
    public ApiResponse<Void> removeBanner(@PathVariable Long bannerId) {
        Long userId = SecurityUtils.getCurrentUserId();
        shopService.removeBanner(userId, bannerId);
        return ApiResponse.success();
    }

    // ===== Public endpoints (no auth required) =====

    @GetMapping("/api/public/shops/{shopId}")
    public ApiResponse<ShopResponse> getPublicShop(@PathVariable Long shopId) {
        return ApiResponse.success(shopService.getPublicShop(shopId));
    }

    @GetMapping("/api/public/shops/{shopId}/products")
    public ApiResponse<PageResponse<ProductDO>> getShopProducts(
            @PathVariable Long shopId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(shopService.getShopProducts(shopId, page, size));
    }
}
