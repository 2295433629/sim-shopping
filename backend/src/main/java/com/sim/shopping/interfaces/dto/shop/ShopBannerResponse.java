package com.sim.shopping.interfaces.dto.shop;

/**
 * ShopBanner响应对象，封装接口出参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class ShopBannerResponse {

    private Long id;
    private Long shopId;
    private String imageUrl;
    private Integer sortOrder;
    private String linkUrl;

    /** 获取Id */
    public Long getId() { return this.id; }
    /** set Id */
    public void setId(Long id) { this.id = id; }
    /**
     * 获取Shop Id
     * @return 返回结果
     */
    public Long getShopId() { return this.shopId; }
    /**
     * set Shop Id
     * @param shopId shopId
     */
    public void setShopId(Long shopId) { this.shopId = shopId; }
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
