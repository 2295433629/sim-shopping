package com.sim.shopping.domain.event;

/**
 * ShipmentCreatedEvent
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class ShipmentCreatedEvent {
    private final String orderNo;
    private final Long shipmentId;
    private final String trackingNo;
    private final String logisticsCompany;

    public ShipmentCreatedEvent(String orderNo, Long shipmentId, String trackingNo, String logisticsCompany) {
        this.orderNo = orderNo;
        this.shipmentId = shipmentId;
        this.trackingNo = trackingNo;
        this.logisticsCompany = logisticsCompany;
    }

    /**
     * 获取Order No
     * @return 返回结果
     */
    public String getOrderNo() { return orderNo; }
    /**
     * 获取Shipment Id
     * @return 返回结果
     */
    public Long getShipmentId() { return shipmentId; }
    /**
     * 获取Tracking No
     * @return 返回结果
     */
    public String getTrackingNo() { return trackingNo; }
    /**
     * 获取Logistics Company
     * @return 返回结果
     */
    public String getLogisticsCompany() { return logisticsCompany; }
}
