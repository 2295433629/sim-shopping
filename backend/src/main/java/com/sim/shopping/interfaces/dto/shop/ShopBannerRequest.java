package com.sim.shopping.interfaces.dto.shop;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ShopBannerRequest {

    @NotBlank(message = "Banner图片URL不能为空")
    @Size(max = 500, message = "图片URL长度不能超过500")
    private String imageUrl;

    private Integer sortOrder;

    @Size(max = 500, message = "链接URL长度不能超过500")
    private String linkUrl;

    public String getImageUrl() { return this.imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Integer getSortOrder() { return this.sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public String getLinkUrl() { return this.linkUrl; }
    public void setLinkUrl(String linkUrl) { this.linkUrl = linkUrl; }
}
