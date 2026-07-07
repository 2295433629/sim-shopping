package com.sim.shopping.interfaces.review;

import com.sim.shopping.application.review.ReviewService;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.review.ReviewResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/reviews")
public class AdminReviewController {

    private final ReviewService reviewService;

    public AdminReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ApiResponse<PageResponse<ReviewResponse>> getAdminReviews(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        return ApiResponse.success(reviewService.getAdminReviews(page, size, keyword));
    }

    @PatchMapping("/{reviewId}/hide")
    public ApiResponse<Void> hideReview(@PathVariable Long reviewId) {
        reviewService.hideReview(reviewId);
        return ApiResponse.success();
    }

    @PatchMapping("/{reviewId}/show")
    public ApiResponse<Void> showReview(@PathVariable Long reviewId) {
        reviewService.showReview(reviewId);
        return ApiResponse.success();
    }

    @DeleteMapping("/{reviewId}")
    public ApiResponse<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReviewByAdmin(reviewId);
        return ApiResponse.success();
    }
}
