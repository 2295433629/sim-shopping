package com.sim.shopping.interfaces.coupon;

import com.sim.shopping.application.coupon.CouponService;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.coupon.CouponResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/coupons")
public class PublicCouponController {

    private final CouponService couponService;

    public PublicCouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping("/available")
    public ApiResponse<List<CouponResponse>> getAvailableCoupons() {
        return ApiResponse.success(couponService.getPublicAvailableCoupons());
    }
}
