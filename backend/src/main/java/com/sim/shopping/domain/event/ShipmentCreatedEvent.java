package com.sim.shopping.domain.event;

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

    public String getOrderNo() { return orderNo; }
    public Long getShipmentId() { return shipmentId; }
    public String getTrackingNo() { return trackingNo; }
    public String getLogisticsCompany() { return logisticsCompany; }
}
