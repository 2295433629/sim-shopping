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
import com.sim.shopping.infrastructure.aop.Log;

/**
 * 店铺管理控制器，处理店铺信息的增删改查
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
public class ShopController {

    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    // ===== Merchant shop endpoints (requires login + merchant identity) =====

    /**
     * 获取Shop Info
     * @return 返回结果
     */
    @GetMapping("/api/merchant/shop")
    public ApiResponse<ShopResponse> getShopInfo() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(shopService.getShopInfo(userId));
    }

    /**
     * 更新Shop Info
     * @param req req
     * @return 返回结果
     */
    @PutMapping("/api/merchant/shop")
    @Log(module = "店铺", type = "修改")
    public ApiResponse<ShopResponse> updateShopInfo(@Valid @RequestBody ShopUpdateRequest req) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(shopService.updateShopInfo(userId, req));
    }

    /**
     * 获取Dashboard
     * @return 返回结果
     */
    @GetMapping("/api/merchant/dashboard")
    public ApiResponse<DashboardResponse> getDashboard() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(shopService.getDashboard(userId));
    }

    /**
     * 添加Banner
     * @param req req
     * @return 返回结果
     */
    @PostMapping("/api/merchant/shop/banners")
    @Log(module = "店铺", type = "新增")
    public ApiResponse<ShopBannerResponse> addBanner(@Valid @RequestBody ShopBannerRequest req) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(shopService.addBanner(userId, req));
    }

    /**
     * 移除Banner
     * @param bannerId bannerId
     * @return 返回结果
     */
    @DeleteMapping("/api/merchant/shop/banners/{bannerId}")
    @Log(module = "店铺", type = "删除")
    public ApiResponse<Void> removeBanner(@PathVariable Long bannerId) {
        Long userId = SecurityUtils.getCurrentUserId();
        shopService.removeBanner(userId, bannerId);
        return ApiResponse.success();
    }

    // ===== Public endpoints (no auth required) =====

    /**
     * 获取Public Shop
     * @param shopId shopId
     * @return 返回结果
     */
    @GetMapping("/api/public/shops/{shopId}")
    public ApiResponse<ShopResponse> getPublicShop(@PathVariable Long shopId) {
        return ApiResponse.success(shopService.getPublicShop(shopId));
    }

    /**
     * 获取Shop Products
     * @return 返回结果
     */
    @GetMapping("/api/public/shops/{shopId}/products")
    public ApiResponse<PageResponse<ProductDO>> getShopProducts(
            @PathVariable Long shopId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(shopService.getShopProducts(shopId, page, size));
    }
}
