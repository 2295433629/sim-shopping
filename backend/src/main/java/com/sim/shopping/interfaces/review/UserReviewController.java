package com.sim.shopping.interfaces.review;

import com.sim.shopping.application.review.ReviewService;
import com.sim.shopping.infrastructure.security.SecurityUtils;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.review.CreateReviewRequest;
import com.sim.shopping.interfaces.dto.review.ReviewResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/reviews")
public class UserReviewController {

    private final ReviewService reviewService;

    public UserReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ApiResponse<ReviewResponse> submitReview(@RequestBody CreateReviewRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(reviewService.submitReview(userId, request));
    }

    @GetMapping
    public ApiResponse<PageResponse<ReviewResponse>> getMyReviews(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(reviewService.getMyReviews(userId, page, size));
    }

    @DeleteMapping("/{reviewId}")
    public ApiResponse<Void> deleteReview(@PathVariable Long reviewId) {
        Long userId = SecurityUtils.getCurrentUserId();
        reviewService.deleteReview(userId, reviewId);
        return ApiResponse.success();
    }
}
