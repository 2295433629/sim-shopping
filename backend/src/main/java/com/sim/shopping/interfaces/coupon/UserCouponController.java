package com.sim.shopping.interfaces.coupon;

import com.sim.shopping.application.coupon.UserCouponService;
import com.sim.shopping.infrastructure.security.SecurityUtils;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.coupon.UserCouponResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户优惠券控制器，处理用户领取优惠券和使用
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/user/coupons")
public class UserCouponController {

    private final UserCouponService userCouponService;

    public UserCouponController(UserCouponService userCouponService) {
        this.userCouponService = userCouponService;
    }

    /**
     * claim Coupon
     * @param couponId couponId
     * @return 返回结果
     */
    @PostMapping("/{couponId}/claim")
    public ApiResponse<UserCouponResponse> claimCoupon(@PathVariable Long couponId) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(userCouponService.claimCoupon(userId, couponId));
    }

    /**
     * 查询我的优惠券
     * @return 返回结果
     */
    @GetMapping
    public ApiResponse<PageResponse<UserCouponResponse>> getMyCoupons(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(userCouponService.getMyCoupons(userId, page, size, status));
    }

    /**
     * 获取My Available Coupons
     * @return 返回结果
     */
    @GetMapping("/available")
    public ApiResponse<List<UserCouponResponse>> getMyAvailableCoupons() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(userCouponService.getMyAvailableCoupons(userId));
    }

    /**
     * 使用优惠券
     * @return 返回结果
     */
    @PostMapping("/{userCouponId}/use")
    public ApiResponse<UserCouponResponse> useCoupon(
            @PathVariable Long userCouponId,
            @RequestBody Map<String, String> request) {
        Long userId = SecurityUtils.getCurrentUserId();
        String orderNo = request.get("orderNo");
        return ApiResponse.success(userCouponService.useCoupon(userId, userCouponId, orderNo));
    }
}
