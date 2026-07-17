package com.sim.shopping.interfaces.dto.shop;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * ShopBanner请求对象，封装接口入参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class ShopBannerRequest {

    @NotBlank(message = "Banner图片URL不能为空")
    @Size(max = 500, message = "图片URL长度不能超过500")
    private String imageUrl;

    private Integer sortOrder;

    @Size(max = 500, message = "链接URL长度不能超过500")
    private String linkUrl;

    /**
     * 获取Image Url
     * @return 返回结果
     */
    public String getImageUrl() { return this.imageUrl; }
    /**
     * set Image Url
     * @param imageUrl imageUrl
     */
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    /**
     * 获取Sort Order
     * @return 返回结果
     */
    public Integer getSortOrder() { return this.sortOrder; }
    /**
     * set Sort Order
     * @param sortOrder sortOrder
     */
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    /**
     * 获取Link Url
     * @return 返回结果
     */
    public String getLinkUrl() { return this.linkUrl; }
    /**
     * set Link Url
     * @param linkUrl linkUrl
     */
    public void setLinkUrl(String linkUrl) { this.linkUrl = linkUrl; }
}
