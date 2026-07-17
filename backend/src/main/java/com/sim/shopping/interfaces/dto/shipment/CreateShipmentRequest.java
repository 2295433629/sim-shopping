package com.sim.shopping.interfaces.dto.shipment;

/**
 * CreateShipment请求对象，封装接口入参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class CreateShipmentRequest {
    private String orderNo;
    private String logisticsCompany;
    private String trackingNo;

    /**
     * 获取Order No
     * @return 返回结果
     */
    public String getOrderNo() { return orderNo; }
    /**
     * set Order No
     * @param orderNo orderNo
     */
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
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
     * 获取Tracking No
     * @return 返回结果
     */
    public String getTrackingNo() { return trackingNo; }
    /**
     * set Tracking No
     * @param trackingNo trackingNo
     */
    public void setTrackingNo(String trackingNo) { this.trackingNo = trackingNo; }
}
