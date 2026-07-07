package com.sim.shopping.interfaces.review;

import com.sim.shopping.application.review.ReviewService;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.review.ReviewResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/products")
public class PublicReviewController {

    private final ReviewService reviewService;

    public PublicReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{productId}/reviews")
    public ApiResponse<PageResponse<ReviewResponse>> getProductReviews(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(reviewService.getProductReviews(productId, page, size));
    }
}
