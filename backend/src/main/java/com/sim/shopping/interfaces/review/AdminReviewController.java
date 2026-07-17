package com.sim.shopping.interfaces.review;

import com.sim.shopping.application.review.ReviewService;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.review.ReviewResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * AdminReview控制器，处理相关业务请求
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/admin/reviews")
@PreAuthorize("hasRole('ADMIN')")
public class AdminReviewController {

    private final ReviewService reviewService;

    public AdminReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * 查询全平台评价（管理员）
     * @return 返回结果
     */
    @GetMapping
    public ApiResponse<PageResponse<ReviewResponse>> getAdminReviews(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer rating,
            @RequestParam(required = false) Boolean hasImage,
            @RequestParam(required = false) Boolean replied) {
        return ApiResponse.success(reviewService.getAdminReviews(page, size, keyword, rating, hasImage, replied));
    }

    /**
     * hide Review
     * @param reviewId reviewId
     * @return 返回结果
     */
    @PatchMapping("/{reviewId}/hide")
    public ApiResponse<Void> hideReview(@PathVariable Long reviewId) {
        reviewService.hideReview(reviewId);
        return ApiResponse.success();
    }

    /**
     * show Review
     * @param reviewId reviewId
     * @return 返回结果
     */
    @PatchMapping("/{reviewId}/show")
    public ApiResponse<Void> showReview(@PathVariable Long reviewId) {
        reviewService.showReview(reviewId);
        return ApiResponse.success();
    }

    /**
     * 删除Review
     * @param reviewId reviewId
     * @return 返回结果
     */
    @DeleteMapping("/{reviewId}")
    public ApiResponse<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReviewByAdmin(reviewId);
        return ApiResponse.success();
    }
}
