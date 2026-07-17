package com.sim.shopping.interfaces.cart;

import com.sim.shopping.application.cart.CartService;
import com.sim.shopping.infrastructure.security.SecurityUtils;
import com.sim.shopping.interfaces.dto.cart.*;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import org.springframework.web.bind.annotation.*;

/**
 * 购物车管理控制器，处理购物车商品的增删改查和结算
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/user/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * 查询用户购物车
     * @return 返回结果
     */
    @GetMapping
    public ApiResponse<CartResponse> getCart() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(cartService.getCart(userId));
    }

    /**
     * 添加商品到购物车
     * @param request request
     * @return 返回结果
     */
    @PostMapping
    public ApiResponse<CartResponse> addToCart(@RequestBody AddToCartRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(cartService.addToCart(userId, request.getProductId(),
                request.getSkuId(), request.getQuantity()));
    }

    /**
     * 更新购物车商品数量
     * @return 返回结果
     */
    @PutMapping("/items/{cartItemId}")
    public ApiResponse<CartResponse> updateCartItem(@PathVariable Long cartItemId,
                                                     @RequestBody UpdateCartItemRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(cartService.updateCartItem(userId, cartItemId,
                request.getQuantity(), request.getSelected()));
    }

    /**
     * 移除Cart Item
     * @param cartItemId cartItemId
     * @return 返回结果
     */
    @DeleteMapping("/items/{cartItemId}")
    public ApiResponse<Void> removeCartItem(@PathVariable Long cartItemId) {
        Long userId = SecurityUtils.getCurrentUserId();
        cartService.removeCartItem(userId, cartItemId);
        return ApiResponse.success();
    }

    /**
     * 清空购物车
     * @return 返回结果
     */
    @DeleteMapping
    public ApiResponse<Void> clearCart() {
        Long userId = SecurityUtils.getCurrentUserId();
        cartService.clearCart(userId);
        return ApiResponse.success();
    }

    /**
     * 查询All
     * @param request request
     * @return 返回结果
     */
    @PutMapping("/select-all")
    public ApiResponse<CartResponse> selectAll(@RequestBody SelectAllRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(cartService.selectAll(userId, request.getSelected()));
    }
}
