package com.sim.shopping.interfaces.coupon;

import com.sim.shopping.application.coupon.UserCouponService;
import com.sim.shopping.infrastructure.security.SecurityUtils;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.coupon.UserCouponResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user/coupons")
public class UserCouponController {

    private final UserCouponService userCouponService;

    public UserCouponController(UserCouponService userCouponService) {
        this.userCouponService = userCouponService;
    }

    @PostMapping("/{couponId}/claim")
    public ApiResponse<UserCouponResponse> claimCoupon(@PathVariable Long couponId) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(userCouponService.claimCoupon(userId, couponId));
    }

    @GetMapping
    public ApiResponse<PageResponse<UserCouponResponse>> getMyCoupons(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(userCouponService.getMyCoupons(userId, page, size, status));
    }

    @GetMapping("/available")
    public ApiResponse<List<UserCouponResponse>> getMyAvailableCoupons() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(userCouponService.getMyAvailableCoupons(userId));
    }

    @PostMapping("/{userCouponId}/use")
    public ApiResponse<UserCouponResponse> useCoupon(
            @PathVariable Long userCouponId,
            @RequestBody Map<String, String> request) {
        Long userId = SecurityUtils.getCurrentUserId();
        String orderNo = request.get("orderNo");
        return ApiResponse.success(userCouponService.useCoupon(userId, userCouponId, orderNo));
    }
}
