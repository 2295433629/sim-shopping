package com.sim.shopping.interfaces.review;

import com.sim.shopping.application.review.ReviewService;
import com.sim.shopping.infrastructure.security.SecurityUtils;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.review.CreateReviewRequest;
import com.sim.shopping.interfaces.dto.review.ReviewResponse;
import org.springframework.web.bind.annotation.*;

/**
 * UserReview控制器，处理相关业务请求
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/user/reviews")
public class UserReviewController {

    private final ReviewService reviewService;

    public UserReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * 提交商品评价
     * @param request request
     * @return 返回结果
     */
    @PostMapping
    public ApiResponse<ReviewResponse> submitReview(@RequestBody CreateReviewRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(reviewService.submitReview(userId, request));
    }

    /**
     * 查询我的评价
     * @return 返回结果
     */
    @GetMapping
    public ApiResponse<PageResponse<ReviewResponse>> getMyReviews(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(reviewService.getMyReviews(userId, page, size));
    }

    /**
     * 删除Review
     * @param reviewId reviewId
     * @return 返回结果
     */
    @DeleteMapping("/{reviewId}")
    public ApiResponse<Void> deleteReview(@PathVariable Long reviewId) {
        Long userId = SecurityUtils.getCurrentUserId();
        reviewService.deleteReview(userId, reviewId);
        return ApiResponse.success();
    }
}
