package com.sim.shopping.interfaces.dto.review;

import java.time.LocalDateTime;

/**
 * Review响应对象，封装接口出参，继承BaseReviewResponse复用公共字段
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class ReviewResponse extends BaseReviewResponse {

    private LocalDateTime merchantRepliedAt;

    /**
     * 获取Merchant Replied At
     * @return 返回结果
     */
    public LocalDateTime getMerchantRepliedAt() { return this.merchantRepliedAt; }
    /**
     * set Merchant Replied At
     * @param merchantRepliedAt merchantRepliedAt
     */
    public void setMerchantRepliedAt(LocalDateTime merchantRepliedAt) { this.merchantRepliedAt = merchantRepliedAt; }
}
