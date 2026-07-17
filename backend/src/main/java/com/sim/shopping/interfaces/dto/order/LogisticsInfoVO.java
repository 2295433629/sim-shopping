package com.sim.shopping.interfaces.dto.order;

/**
 * LogisticsInfo视图对象，用于前端展示
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class LogisticsInfoVO {
    private String trackingNo;
    private String logisticsCompany;
    private String status;

    /**
     * 获取Tracking No
     * @return 返回结果
     */
    public String getTrackingNo() { return trackingNo; }
    /**
     * set Tracking No
     * @param trackingNo trackingNo
     */
    public void setTrackingNo(String trackingNo) { this.trackingNo = trackingNo; }
    /**
     * 获取Logistics Company
     * @return 返回结果
     */
    public String getLogisticsCompany() { return logisticsCompany; }
    /**
     * set Logistics Company
     * @param logisticsCompany logisticsCompany
     */
    public void setLogisticsCompany(String logisticsCompany) { this.logisticsCompany = logisticsCompany; }
    /**
     * 获取Status
     * @return 返回结果
     */
    public String getStatus() { return status; }
    /**
     * set Status
     * @param status status
     */
    public void setStatus(String status) { this.status = status; }
}
