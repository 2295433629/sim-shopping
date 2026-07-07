package com.sim.shopping.interfaces.dto.shop;

public class ShopBannerResponse {

    private Long id;
    private Long shopId;
    private String imageUrl;
    private Integer sortOrder;
    private String linkUrl;

    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }
    public Long getShopId() { return this.shopId; }
    public void setShopId(Long shopId) { this.shopId = shopId; }
    public String getImageUrl() { return this.imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Integer getSortOrder() { return this.sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public String getLinkUrl() { return this.linkUrl; }
    public void setLinkUrl(String linkUrl) { this.linkUrl = linkUrl; }
}
