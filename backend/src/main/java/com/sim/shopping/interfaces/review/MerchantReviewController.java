package com.sim.shopping.interfaces.review;

import com.sim.shopping.application.merchant.MerchantService;
import com.sim.shopping.application.review.ReviewService;
import com.sim.shopping.infrastructure.security.SecurityUtils;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.review.MerchantReviewResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;

/**
 * MerchantReview控制器，处理相关业务请求
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/merchant/reviews")
@PreAuthorize("hasRole('MERCHANT')")
public class MerchantReviewController {

    private final ReviewService reviewService;
    private final MerchantService merchantService;

    public MerchantReviewController(ReviewService reviewService,
                                    MerchantService merchantService) {
        this.reviewService = reviewService;
        this.merchantService = merchantService;
    }

    /**
     * 查询商家商品评价
     * @return 返回结果
     */
    @GetMapping
    public ApiResponse<PageResponse<MerchantReviewResponse>> getMerchantReviews(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long userId = SecurityUtils.getCurrentUserId();
        Long shopId = merchantService.getShopIdByUserId(userId);
        return ApiResponse.success(reviewService.getMerchantReviews(shopId, page, size));
    }

    /**
     * 回复评价
     * @param reviewId reviewId
     * @param body body
     * @return 返回结果
     */
    @PostMapping("/{reviewId}/reply")
    public ApiResponse<Void> replyReview(@PathVariable Long reviewId, @RequestBody Map<String, String> body) {
        Long userId = SecurityUtils.getCurrentUserId();
        Long shopId = merchantService.getShopIdByUserId(userId);
        String content = body.get("content");
        reviewService.replyReview(shopId, reviewId, content);
        return ApiResponse.success();
    }
}
