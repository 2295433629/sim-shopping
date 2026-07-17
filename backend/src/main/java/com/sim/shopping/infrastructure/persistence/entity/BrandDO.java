package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * 品牌实体，对应 t_brand 表，存储品牌信息
 *
 * @author Sim Team
 * @since 1.0.0
 */
@TableName("t_brand")
public class BrandDO extends BaseEntity {
    private String status;
    private String brandName;
    private String brandLogo;
    private String brandDescription;

    /**
     * 获取Status
     * @return 返回结果
     */
    public String getStatus() { return this.status; }
    /**
     * set Status
     * @param status status
     */
    public void setStatus(String status) { this.status = status; }
    /**
     * 获取Brand Name
     * @return 返回结果
     */
    public String getBrandName() { return this.brandName; }
    /**
     * set Brand Name
     * @param brandName brandName
     */
    public void setBrandName(String brandName) { this.brandName = brandName; }
    /**
     * 获取Brand Logo
     * @return 返回结果
     */
    public String getBrandLogo() { return this.brandLogo; }
    /**
     * set Brand Logo
     * @param brandLogo brandLogo
     */
    public void setBrandLogo(String brandLogo) { this.brandLogo = brandLogo; }
    /**
     * 获取Brand Description
     * @return 返回结果
     */
    public String getBrandDescription() { return this.brandDescription; }
    /**
     * set Brand Description
     * @param brandDescription brandDescription
     */
    public void setBrandDescription(String brandDescription) { this.brandDescription = brandDescription; }
}
