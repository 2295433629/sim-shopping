package com.sim.shopping.interfaces.coupon;

import com.sim.shopping.application.coupon.CouponService;
import com.sim.shopping.infrastructure.persistence.entity.CouponDO;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.coupon.CouponResponse;
import com.sim.shopping.interfaces.dto.coupon.CouponStatisticsVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/coupons")
@PreAuthorize("hasRole('ADMIN')")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping
    public ApiResponse<PageResponse<CouponResponse>> getCouponList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status) {
        return ApiResponse.success(couponService.getCouponList(page, size, keyword, status));
    }

    @GetMapping("/{couponId}")
    public ApiResponse<CouponResponse> getCouponDetail(@PathVariable Long couponId) {
        return ApiResponse.success(couponService.getCouponDetail(couponId));
    }

    @PostMapping
    public ApiResponse<CouponResponse> createCoupon(@RequestBody CouponDO coupon) {
        return ApiResponse.success(couponService.createCoupon(coupon));
    }

    @PutMapping("/{couponId}")
    public ApiResponse<CouponResponse> updateCoupon(@PathVariable Long couponId, @RequestBody CouponDO coupon) {
        return ApiResponse.success(couponService.updateCoupon(couponId, coupon));
    }

    @DeleteMapping("/{couponId}")
    public ApiResponse<Void> deleteCoupon(@PathVariable Long couponId) {
        couponService.deleteCoupon(couponId);
        return ApiResponse.success();
    }

    @GetMapping("/statistics")
    public ApiResponse<CouponStatisticsVO> getCouponStatistics() {
        return ApiResponse.success(couponService.getCouponStatistics());
    }
}
