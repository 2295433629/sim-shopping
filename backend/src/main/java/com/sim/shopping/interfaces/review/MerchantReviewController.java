package com.sim.shopping.interfaces.review;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sim.shopping.application.review.ReviewService;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.infrastructure.persistence.entity.MerchantDO;
import com.sim.shopping.infrastructure.persistence.entity.ShopDO;
import com.sim.shopping.infrastructure.persistence.mapper.MerchantMapper;
import com.sim.shopping.infrastructure.persistence.mapper.ShopMapper;
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
    private final MerchantMapper merchantMapper;
    private final ShopMapper shopMapper;

    public MerchantReviewController(ReviewService reviewService,
                                    MerchantMapper merchantMapper,
                                    ShopMapper shopMapper) {
        this.reviewService = reviewService;
        this.merchantMapper = merchantMapper;
        this.shopMapper = shopMapper;
    }

    /**
     * 查询商家商品评价
     * @return 返回结果
     */
    @GetMapping
    public ApiResponse<PageResponse<MerchantReviewResponse>> getMerchantReviews(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long shopId = resolveShopId();
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
        Long shopId = resolveShopId();
        String content = body.get("content");
        reviewService.replyReview(shopId, reviewId, content);
        return ApiResponse.success();
    }

    private Long resolveShopId() {
        Long userId = SecurityUtils.getCurrentUserId();
        LambdaQueryWrapper<MerchantDO> merchantWrapper = new LambdaQueryWrapper<>();
        merchantWrapper.eq(MerchantDO::getUserId, userId);
        MerchantDO merchant = merchantMapper.selectOne(merchantWrapper);
        if (merchant == null) {
            throw new BusinessException(403, "非商家用户");
        }
        LambdaQueryWrapper<ShopDO> shopWrapper = new LambdaQueryWrapper<>();
        shopWrapper.eq(ShopDO::getMerchantId, merchant.getId());
        ShopDO shop = shopMapper.selectOne(shopWrapper);
        if (shop == null) {
            throw new BusinessException(403, "店铺不存在");
        }
        return shop.getId();
    }
}
