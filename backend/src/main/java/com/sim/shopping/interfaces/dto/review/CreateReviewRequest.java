package com.sim.shopping.interfaces.dto.review;

import java.util.List;

/**
 * CreateReview请求对象，封装接口入参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class CreateReviewRequest {
    private Long productId;
    private Long orderId;
    private String orderNo;
    private Integer rating;
    private String content;
    private List<String> imageUrls;

    /**
     * 获取Product Id
     * @return 返回结果
     */
    public Long getProductId() { return this.productId; }
    /**
     * set Product Id
     * @param productId productId
     */
    public void setProductId(Long productId) { this.productId = productId; }
    /**
     * 获取Order Id
     * @return 返回结果
     */
    public Long getOrderId() { return this.orderId; }
    /**
     * set Order Id
     * @param orderId orderId
     */
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    /**
     * 获取Order No
     * @return 返回结果
     */
    public String getOrderNo() { return this.orderNo; }
    /**
     * set Order No
     * @param orderNo orderNo
     */
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    /**
     * 获取Rating
     * @return 返回结果
     */
    public Integer getRating() { return this.rating; }
    /**
     * set Rating
     * @param rating rating
     */
    public void setRating(Integer rating) { this.rating = rating; }
    /**
     * 获取Content
     * @return 返回结果
     */
    public String getContent() { return this.content; }
    /**
     * set Content
     * @param content content
     */
    public void setContent(String content) { this.content = content; }
    /**
     * 获取Image Urls
     * @return 返回结果
     */
    public List<String> getImageUrls() { return this.imageUrls; }
    /**
     * set Image Urls
     * @param imageUrls imageUrls
     */
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }
}
