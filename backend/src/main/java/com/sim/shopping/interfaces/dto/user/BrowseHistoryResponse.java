package com.sim.shopping.interfaces.dto.user;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BrowseHistoryResponse {

    private Long id;
    private Long productId;
    private String productName;
    private String productImage;
    private BigDecimal price;
    private LocalDateTime viewedAt;

    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductId() { return this.productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductName() { return this.productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getProductImage() { return this.productImage; }
    public void setProductImage(String productImage) { this.productImage = productImage; }
    public BigDecimal getPrice() { return this.price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public LocalDateTime getViewedAt() { return this.viewedAt; }
    public void setViewedAt(LocalDateTime viewedAt) { this.viewedAt = viewedAt; }
}
