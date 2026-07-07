package com.sim.shopping.interfaces.cart;

import com.sim.shopping.application.cart.CartService;
import com.sim.shopping.infrastructure.security.SecurityUtils;
import com.sim.shopping.interfaces.dto.cart.*;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ApiResponse<CartResponse> getCart() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(cartService.getCart(userId));
    }

    @PostMapping
    public ApiResponse<CartResponse> addToCart(@RequestBody AddToCartRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(cartService.addToCart(userId, request.getProductId(),
                request.getSkuId(), request.getQuantity()));
    }

    @PutMapping("/items/{cartItemId}")
    public ApiResponse<CartResponse> updateCartItem(@PathVariable Long cartItemId,
                                                     @RequestBody UpdateCartItemRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(cartService.updateCartItem(userId, cartItemId,
                request.getQuantity(), request.getSelected()));
    }

    @DeleteMapping("/items/{cartItemId}")
    public ApiResponse<Void> removeCartItem(@PathVariable Long cartItemId) {
        Long userId = SecurityUtils.getCurrentUserId();
        cartService.removeCartItem(userId, cartItemId);
        return ApiResponse.success();
    }

    @DeleteMapping
    public ApiResponse<Void> clearCart() {
        Long userId = SecurityUtils.getCurrentUserId();
        cartService.clearCart(userId);
        return ApiResponse.success();
    }

    @PutMapping("/select-all")
    public ApiResponse<CartResponse> selectAll(@RequestBody SelectAllRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(cartService.selectAll(userId, request.getSelected()));
    }
}
