package com.sim.shopping.application.review;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.domain.event.ReviewCreatedEvent;
import com.sim.shopping.infrastructure.persistence.entity.*;
import com.sim.shopping.infrastructure.persistence.mapper.*;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.review.CreateReviewRequest;
import com.sim.shopping.interfaces.dto.review.MerchantReviewResponse;
import com.sim.shopping.interfaces.dto.review.ReviewResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 评价服务，处理商品评价的提交、查询、回复和审核
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Service
public class ReviewService {

    private final ReviewMapper reviewMapper;
    private final ReviewImageMapper reviewImageMapper;
    private final OrderMapper orderMapper;
    private final ProductMapper productMapper;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher eventPublisher;

    private static final String REVIEW_STATUS_VISIBLE = "VISIBLE";
    private static final String REVIEW_STATUS_HIDDEN = "HIDDEN";
    private static final String ORDER_STATUS_COMPLETED = "COMPLETED";

    public ReviewService(ReviewMapper reviewMapper,
                          ReviewImageMapper reviewImageMapper,
                          OrderMapper orderMapper,
                          ProductMapper productMapper,
                          UserMapper userMapper,
                          ApplicationEventPublisher eventPublisher) {
        this.reviewMapper = reviewMapper;
        this.reviewImageMapper = reviewImageMapper;
        this.orderMapper = orderMapper;
        this.productMapper = productMapper;
        this.userMapper = userMapper;
        this.eventPublisher = eventPublisher;
    }

    /**
     * 提交商品评价
     * @param userId userId
     * @param req req
     * @return 返回结果
     */
    @Transactional
    public ReviewResponse submitReview(Long userId, CreateReviewRequest req) {
        // Validate order exists and belongs to user, status is COMPLETED
        OrderDO order = orderMapper.selectById(req.getOrderId());
        if (order == null) {
            throw new BusinessException(400, "订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权评价此订单");
        }
        if (!"DELIVERED".equals(order.getStatus()) && !"COMPLETED".equals(order.getStatus())) {
            throw new BusinessException(400, "只能评价已签收或已完成的订单");
        }

        // Validate no duplicate review for same order + product
        LambdaQueryWrapper<ReviewDO> dupWrapper = new LambdaQueryWrapper<>();
        dupWrapper.eq(ReviewDO::getOrderId, req.getOrderId())
                .eq(ReviewDO::getProductId, req.getProductId())
                .eq(ReviewDO::getUserId, userId);
        ReviewDO existing = reviewMapper.selectOne(dupWrapper);
        if (existing != null) {
            throw new BusinessException(400, "该商品已评价，不可重复评价");
        }

        // Validate rating
        if (req.getRating() == null || req.getRating() < 1 || req.getRating() > 5) {
            throw new BusinessException(400, "评分必须在1-5之间");
        }

        // Create ReviewDO
        ReviewDO review = new ReviewDO();
        review.setUserId(userId);
        review.setProductId(req.getProductId());
        review.setOrderId(req.getOrderId());
        review.setOrderNo(req.getOrderNo());
        review.setShopId(order.getShopId());
        review.setRating(req.getRating());
        review.setContent(req.getContent());
        review.setStatus(REVIEW_STATUS_VISIBLE);
        reviewMapper.insert(review);

        // Create ReviewImageDO list
        if (req.getImageUrls() != null && !req.getImageUrls().isEmpty()) {
            for (int i = 0; i < req.getImageUrls().size(); i++) {
                ReviewImageDO image = new ReviewImageDO();
                image.setReviewId(review.getId());
                image.setImageUrl(req.getImageUrls().get(i));
                image.setSortOrder(i);
                reviewImageMapper.insert(image);
            }
        }

        // Update product avgReviewScore and reviewCount
        updateProductRating(req.getProductId());

        // Publish event
        eventPublisher.publishEvent(new ReviewCreatedEvent(req.getOrderNo(), req.getProductId(), req.getRating()));

        return convertToReviewResponse(review);
    }

    /**
     * 查询商品评价列表
     * @param productId productId
     * @param page page
     * @param size size
     * @param rating rating
     * @return 返回结果
     */
    public PageResponse<ReviewResponse> getProductReviews(Long productId, int page, int size, Integer rating) {
        Page<ReviewDO> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<ReviewDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ReviewDO::getProductId, productId)
                .eq(ReviewDO::getStatus, REVIEW_STATUS_VISIBLE);
        if (rating != null) {
            wrapper.eq(ReviewDO::getRating, rating);
        }
        wrapper.orderByDesc(ReviewDO::getCreatedAt);

        IPage<ReviewDO> result = reviewMapper.selectPage(pageObj, wrapper);

        List<ReviewResponse> list = result.getRecords().stream()
                .map(this::convertToReviewResponse)
                .collect(Collectors.toList());

        return PageResponse.of(list, result.getTotal(), page, size);
    }

    /**
     * 查询我的评价
     * @param userId userId
     * @param page page
     * @param size size
     * @return 返回结果
     */
    public PageResponse<ReviewResponse> getMyReviews(Long userId, int page, int size) {
        Page<ReviewDO> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<ReviewDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ReviewDO::getUserId, userId)
                .orderByDesc(ReviewDO::getCreatedAt);

        IPage<ReviewDO> result = reviewMapper.selectPage(pageObj, wrapper);

        List<ReviewResponse> list = result.getRecords().stream()
                .map(this::convertToReviewResponse)
                .collect(Collectors.toList());

        return PageResponse.of(list, result.getTotal(), page, size);
    }

    /**
     * 删除Review
     * @param userId userId
     * @param reviewId reviewId
     */
    @Transactional
    public void deleteReview(Long userId, Long reviewId) {
        ReviewDO review = reviewMapper.selectById(reviewId);
        if (review == null) {
            throw new BusinessException(400, "评价不存在");
        }
        if (!review.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权删除此评价");
        }

        // Delete review images
        LambdaQueryWrapper<ReviewImageDO> imgWrapper = new LambdaQueryWrapper<>();
        imgWrapper.eq(ReviewImageDO::getReviewId, reviewId);
        reviewImageMapper.delete(imgWrapper);

        // Delete review
        reviewMapper.deleteById(reviewId);

        // Update product rating
        updateProductRating(review.getProductId());
    }

    /**
     * 查询商家商品评价
     * @param shopId shopId
     * @param page page
     * @param size size
     * @return 返回结果
     */
    public PageResponse<MerchantReviewResponse> getMerchantReviews(Long shopId, int page, int size) {
        Page<ReviewDO> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<ReviewDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ReviewDO::getShopId, shopId)
                .orderByDesc(ReviewDO::getCreatedAt);

        IPage<ReviewDO> result = reviewMapper.selectPage(pageObj, wrapper);

        List<MerchantReviewResponse> list = result.getRecords().stream()
                .map(this::convertToMerchantReviewResponse)
                .collect(Collectors.toList());

        return PageResponse.of(list, result.getTotal(), page, size);
    }

    /**
     * 回复评价
     * @param shopId shopId
     * @param reviewId reviewId
     * @param reply reply
     */
    @Transactional
    public void replyReview(Long shopId, Long reviewId, String reply) {
        ReviewDO review = reviewMapper.selectById(reviewId);
        if (review == null) {
            throw new BusinessException(400, "评价不存在");
        }
        if (!review.getShopId().equals(shopId)) {
            throw new BusinessException(403, "无权回复此评价");
        }

        review.setMerchantReply(reply);
        review.setMerchantRepliedAt(java.time.LocalDateTime.now());
        reviewMapper.updateById(review);
    }

    /**
     * 查询全平台评价（管理员）
     * @return 返回结果
     */
    public PageResponse<ReviewResponse> getAdminReviews(int page, int size, String keyword,
                                                         Integer rating, Boolean hasImage, Boolean replied) {
        Page<ReviewDO> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<ReviewDO> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(ReviewDO::getContent, keyword)
                    .or().like(ReviewDO::getOrderNo, keyword));
        }
        if (rating != null) {
            wrapper.eq(ReviewDO::getRating, rating);
        }
        if (hasImage != null) {
            // t_review 表没有 hasImage 字段，通过 t_review_image 关联查询
            LambdaQueryWrapper<ReviewImageDO> imgWrapper = new LambdaQueryWrapper<>();
            imgWrapper.select(ReviewImageDO::getReviewId).groupBy(ReviewImageDO::getReviewId);
            List<Long> reviewIdsWithImage = reviewImageMapper.selectList(imgWrapper)
                    .stream().map(ReviewImageDO::getReviewId).distinct().collect(Collectors.toList());
            if (hasImage) {
                if (reviewIdsWithImage.isEmpty()) {
                    // 强制返回空结果
                    wrapper.eq(ReviewDO::getId, -1L);
                } else {
                    wrapper.in(ReviewDO::getId, reviewIdsWithImage);
                }
            } else {
                if (!reviewIdsWithImage.isEmpty()) {
                    wrapper.notIn(ReviewDO::getId, reviewIdsWithImage);
                }
            }
        }
        if (replied != null) {
            if (replied) {
                wrapper.isNotNull(ReviewDO::getMerchantReply);
            } else {
                wrapper.isNull(ReviewDO::getMerchantReply);
            }
        }
        wrapper.orderByDesc(ReviewDO::getCreatedAt);

        IPage<ReviewDO> result = reviewMapper.selectPage(pageObj, wrapper);

        List<ReviewResponse> list = result.getRecords().stream()
                .map(this::convertToReviewResponse)
                .collect(Collectors.toList());

        return PageResponse.of(list, result.getTotal(), page, size);
    }

    /**
     * hide Review
     * @param reviewId reviewId
     */
    @Transactional
    public void hideReview(Long reviewId) {
        ReviewDO review = reviewMapper.selectById(reviewId);
        if (review == null) {
            throw new BusinessException(400, "评价不存在");
        }
        review.setStatus(REVIEW_STATUS_HIDDEN);
        reviewMapper.updateById(review);
    }

    /**
     * show Review
     * @param reviewId reviewId
     */
    @Transactional
    public void showReview(Long reviewId) {
        ReviewDO review = reviewMapper.selectById(reviewId);
        if (review == null) {
            throw new BusinessException(400, "评价不存在");
        }
        review.setStatus(REVIEW_STATUS_VISIBLE);
        reviewMapper.updateById(review);
    }

    /**
     * 删除Review By Admin
     * @param reviewId reviewId
     */
    @Transactional
    public void deleteReviewByAdmin(Long reviewId) {
        ReviewDO review = reviewMapper.selectById(reviewId);
        if (review == null) {
            throw new BusinessException(400, "评价不存在");
        }

        // Delete review images
        LambdaQueryWrapper<ReviewImageDO> imgWrapper = new LambdaQueryWrapper<>();
        imgWrapper.eq(ReviewImageDO::getReviewId, reviewId);
        reviewImageMapper.delete(imgWrapper);

        // Delete review
        reviewMapper.deleteById(reviewId);

        // Update product rating
        updateProductRating(review.getProductId());
    }

    // ========== Private helper methods ==========

    private void updateProductRating(Long productId) {
        ProductDO product = productMapper.selectById(productId);
        if (product == null) return;

        LambdaQueryWrapper<ReviewDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ReviewDO::getProductId, productId)
                .eq(ReviewDO::getStatus, REVIEW_STATUS_VISIBLE);
        List<ReviewDO> reviews = reviewMapper.selectList(wrapper);

        if (reviews.isEmpty()) {
            product.setAvgReviewScore(BigDecimal.ZERO);
            product.setReviewCount(0);
        } else {
            int totalRating = 0;
            for (ReviewDO r : reviews) {
                totalRating += r.getRating();
            }
            BigDecimal avgScore = BigDecimal.valueOf(totalRating)
                    .divide(BigDecimal.valueOf(reviews.size()), 1, RoundingMode.HALF_UP);
            product.setAvgReviewScore(avgScore);
            product.setReviewCount(reviews.size());
        }
        productMapper.updateById(product);
    }

    private List<String> getReviewImages(Long reviewId) {
        LambdaQueryWrapper<ReviewImageDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ReviewImageDO::getReviewId, reviewId)
                .orderByAsc(ReviewImageDO::getSortOrder);
        List<ReviewImageDO> images = reviewImageMapper.selectList(wrapper);
        List<String> urls = new ArrayList<>();
        for (ReviewImageDO img : images) {
            urls.add(img.getImageUrl());
        }
        return urls;
    }

    private String getProductName(Long productId) {
        if (productId == null) return "";
        ProductDO product = productMapper.selectById(productId);
        return product != null ? product.getName() : "";
    }

    private String getUsername(Long userId) {
        if (userId == null) return "";
        UserDO user = userMapper.selectById(userId);
        return user != null ? user.getUsername() : "";
    }

    private ReviewResponse convertToReviewResponse(ReviewDO review) {
        ReviewResponse resp = new ReviewResponse();
        resp.setId(review.getId());
        resp.setOrderNo(review.getOrderNo());
        resp.setProductId(review.getProductId());
        resp.setProductName(getProductName(review.getProductId()));
        resp.setUserId(review.getUserId());
        resp.setUsername(getUsername(review.getUserId()));
        resp.setRating(review.getRating());
        resp.setContent(review.getContent());
        resp.setImages(getReviewImages(review.getId()));
        resp.setStatus(review.getStatus());
        resp.setMerchantReply(review.getMerchantReply());
        resp.setMerchantRepliedAt(review.getMerchantRepliedAt());
        resp.setCreatedAt(review.getCreatedAt());
        return resp;
    }

    private MerchantReviewResponse convertToMerchantReviewResponse(ReviewDO review) {
        MerchantReviewResponse resp = new MerchantReviewResponse();
        resp.setId(review.getId());
        resp.setOrderNo(review.getOrderNo());
        resp.setProductId(review.getProductId());
        resp.setProductName(getProductName(review.getProductId()));
        resp.setUserId(review.getUserId());
        resp.setUsername(getUsername(review.getUserId()));
        resp.setRating(review.getRating());
        resp.setContent(review.getContent());
        resp.setImages(getReviewImages(review.getId()));
        resp.setStatus(review.getStatus());
        resp.setMerchantReply(review.getMerchantReply());
        resp.setCreatedAt(review.getCreatedAt());
        return resp;
    }
}
