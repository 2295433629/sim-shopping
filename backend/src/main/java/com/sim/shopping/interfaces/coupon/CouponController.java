package com.sim.shopping.interfaces.coupon;

import com.sim.shopping.application.coupon.CouponService;
import com.sim.shopping.infrastructure.persistence.entity.CouponDO;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.coupon.CouponResponse;
import com.sim.shopping.interfaces.dto.coupon.CouponStatisticsVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 优惠券管理控制器，处理优惠券的创建、发放、查询
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/admin/coupons")
@PreAuthorize("hasRole('ADMIN')")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    /**
     * 获取Coupon List
     * @return 返回结果
     */
    @GetMapping
    public ApiResponse<PageResponse<CouponResponse>> getCouponList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status) {
        return ApiResponse.success(couponService.getCouponList(page, size, keyword, status));
    }

    /**
     * 获取Coupon Detail
     * @param couponId couponId
     * @return 返回结果
     */
    @GetMapping("/{couponId}")
    public ApiResponse<CouponResponse> getCouponDetail(@PathVariable Long couponId) {
        return ApiResponse.success(couponService.getCouponDetail(couponId));
    }

    /**
     * 创建优惠券
     * @param coupon coupon
     * @return 返回结果
     */
    @PostMapping
    public ApiResponse<CouponResponse> createCoupon(@RequestBody CouponDO coupon) {
        return ApiResponse.success(couponService.createCoupon(coupon));
    }

    /**
     * 更新优惠券
     * @param couponId couponId
     * @param coupon coupon
     * @return 返回结果
     */
    @PutMapping("/{couponId}")
    public ApiResponse<CouponResponse> updateCoupon(@PathVariable Long couponId, @RequestBody CouponDO coupon) {
        return ApiResponse.success(couponService.updateCoupon(couponId, coupon));
    }

    /**
     * 删除优惠券
     * @param couponId couponId
     * @return 返回结果
     */
    @DeleteMapping("/{couponId}")
    public ApiResponse<Void> deleteCoupon(@PathVariable Long couponId) {
        couponService.deleteCoupon(couponId);
        return ApiResponse.success();
    }

    /**
     * 获取Coupon Statistics
     * @return 返回结果
     */
    @GetMapping("/statistics")
    public ApiResponse<CouponStatisticsVO> getCouponStatistics() {
        return ApiResponse.success(couponService.getCouponStatistics());
    }
}
