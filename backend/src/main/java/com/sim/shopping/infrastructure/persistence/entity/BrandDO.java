package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@TableName("t_brand")
public class BrandDO extends BaseEntity {
    private String status;
    private String brandName;
    private String brandLogo;
    private String brandDescription;

    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }
    public String getBrandName() { return this.brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }
    public String getBrandLogo() { return this.brandLogo; }
    public void setBrandLogo(String brandLogo) { this.brandLogo = brandLogo; }
    public String getBrandDescription() { return this.brandDescription; }
    public void setBrandDescription(String brandDescription) { this.brandDescription = brandDescription; }
}
