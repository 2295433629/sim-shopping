package com.sim.shopping.interfaces.product;

import com.sim.shopping.application.product.FavoriteService;
import com.sim.shopping.infrastructure.security.SecurityUtils;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.product.ProductCardVO;
import org.springframework.web.bind.annotation.*;
import com.sim.shopping.infrastructure.aop.Log;

/**
 * 商品收藏控制器，处理用户收藏/取消收藏商品
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/user/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    /**
     * 收藏商品
     * @param productId productId
     * @return 返回结果
     */
    @PostMapping("/{productId}")
    @Log(module = "商品", type = "新增")
    public ApiResponse<Void> addFavorite(@PathVariable Long productId) {
        Long userId = SecurityUtils.getCurrentUserId();
        favoriteService.addFavorite(userId, productId);
        return ApiResponse.success();
    }

    /**
     * 取消收藏
     * @param productId productId
     * @return 返回结果
     */
    @DeleteMapping("/{productId}")
    @Log(module = "商品", type = "删除")
    public ApiResponse<Void> removeFavorite(@PathVariable Long productId) {
        Long userId = SecurityUtils.getCurrentUserId();
        favoriteService.removeFavorite(userId, productId);
        return ApiResponse.success();
    }

    /**
     * 获取Favorite List
     * @return 返回结果
     */
    @GetMapping
    public ApiResponse<PageResponse<ProductCardVO>> getFavoriteList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(favoriteService.getFavoriteList(userId, page, size));
    }

    /**
     * is Favorite
     * @param productId productId
     * @return 返回结果
     */
    @GetMapping("/check/{productId}")
    public ApiResponse<Boolean> isFavorite(@PathVariable Long productId) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(favoriteService.isFavorite(userId, productId));
    }
}
