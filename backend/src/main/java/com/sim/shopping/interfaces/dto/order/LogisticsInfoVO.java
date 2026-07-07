package com.sim.shopping.interfaces.dto.order;

public class LogisticsInfoVO {
    private String trackingNo;
    private String logisticsCompany;
    private String status;

    public String getTrackingNo() { return trackingNo; }
    public void setTrackingNo(String trackingNo) { this.trackingNo = trackingNo; }
    public String getLogisticsCompany() { return logisticsCompany; }
    public void setLogisticsCompany(String logisticsCompany) { this.logisticsCompany = logisticsCompany; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
