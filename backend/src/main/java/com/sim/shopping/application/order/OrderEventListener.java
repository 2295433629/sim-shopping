package com.sim.shopping.application.order;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sim.shopping.domain.event.LogisticsDeliveredEvent;
import com.sim.shopping.domain.event.OrderCancelledEvent;
import com.sim.shopping.domain.event.OrderCreatedEvent;
import com.sim.shopping.domain.event.ReviewCreatedEvent;
import com.sim.shopping.domain.event.ShipmentCreatedEvent;
import com.sim.shopping.infrastructure.persistence.entity.MerchantDO;
import com.sim.shopping.infrastructure.persistence.entity.OrderDO;
import com.sim.shopping.infrastructure.persistence.entity.ShopDO;
import com.sim.shopping.infrastructure.persistence.entity.SysNotificationDO;
import com.sim.shopping.infrastructure.persistence.mapper.MerchantMapper;
import com.sim.shopping.infrastructure.persistence.mapper.OrderMapper;
import com.sim.shopping.infrastructure.persistence.mapper.ShopMapper;
import com.sim.shopping.infrastructure.persistence.mapper.SysNotificationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * OrderEvent事件监听器
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Component
public class OrderEventListener {

    private static final Logger log = LoggerFactory.getLogger(OrderEventListener.class);

    private final SysNotificationMapper sysNotificationMapper;
    private final ShopMapper shopMapper;
    private final MerchantMapper merchantMapper;
    private final OrderMapper orderMapper;

    public OrderEventListener(SysNotificationMapper sysNotificationMapper,
                              ShopMapper shopMapper,
                              MerchantMapper merchantMapper,
                              OrderMapper orderMapper) {
        this.sysNotificationMapper = sysNotificationMapper;
        this.shopMapper = shopMapper;
        this.merchantMapper = merchantMapper;
        this.orderMapper = orderMapper;
    }

    /**
     * 处理Order Created
     * @param event event
     */
    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("订单创建事件: orderNo={}, userId={}, shopId={}, totalAmount={}",
                event.getOrderNo(), event.getUserId(), event.getShopId(), event.getTotalAmount());
        // 通知商家有新订单
        try {
            Long merchantUserId = getMerchantUserIdByShopId(event.getShopId());
            if (merchantUserId != null) {
                createNotification(merchantUserId, "MERCHANT", "ORDER",
                        "新订单通知", "您有一笔新订单 " + event.getOrderNo() + "，金额: " + event.getTotalAmount() + "元",
                        event.getShopId(), "SHOP");
            }
        } catch (Exception e) {
            log.error("通知商家新订单失败: orderNo={}, error={}", event.getOrderNo(), e.getMessage());
        }
    }

    /**
     * 处理Order Cancelled
     * @param event event
     */
    @EventListener
    public void handleOrderCancelled(OrderCancelledEvent event) {
        log.info("订单取消事件: orderNo={}, userId={}, shopId={}",
                event.getOrderNo(), event.getUserId(), event.getShopId());
        // 通知商家订单已取消
        try {
            Long merchantUserId = getMerchantUserIdByShopId(event.getShopId());
            if (merchantUserId != null) {
                createNotification(merchantUserId, "MERCHANT", "ORDER",
                        "订单取消通知", "订单 " + event.getOrderNo() + " 已被用户取消",
                        event.getShopId(), "SHOP");
            }
        } catch (Exception e) {
            log.error("通知商家订单取消失败: orderNo={}, error={}", event.getOrderNo(), e.getMessage());
        }
    }

    /**
     * 处理Shipment Created
     * @param event event
     */
    @EventListener
    public void handleShipmentCreated(ShipmentCreatedEvent event) {
        log.info("发货事件: orderNo={}, shipmentId={}, trackingNo={}, logisticsCompany={}",
                event.getOrderNo(), event.getShipmentId(), event.getTrackingNo(), event.getLogisticsCompany());
        // 通知用户已发货
        try {
            Long userId = getUserIdByOrderNo(event.getOrderNo());
            if (userId != null) {
                createNotification(userId, "USER", "SHIPMENT",
                        "发货通知", "您的订单 " + event.getOrderNo() + " 已发货，物流公司: " + event.getLogisticsCompany() + "，运单号: " + event.getTrackingNo(),
                        event.getShipmentId(), "SHIPMENT");
            }
        } catch (Exception e) {
            log.error("通知用户发货失败: orderNo={}, error={}", event.getOrderNo(), e.getMessage());
        }
    }

    /**
     * 处理Logistics Delivered
     * @param event event
     */
    @EventListener
    public void handleLogisticsDelivered(LogisticsDeliveredEvent event) {
        log.info("物流送达事件: orderNo={}, orderId={}",
                event.getOrderNo(), event.getOrderId());
        // 通知用户已签收
        try {
            Long userId = getUserIdByOrderNo(event.getOrderNo());
            if (userId != null) {
                createNotification(userId, "USER", "LOGISTICS",
                        "签收通知", "您的订单 " + event.getOrderNo() + " 已签收，请确认收货",
                        event.getOrderId(), "ORDER");
            }
        } catch (Exception e) {
            log.error("通知用户签收失败: orderNo={}, error={}", event.getOrderNo(), e.getMessage());
        }
    }

    /**
     * 处理Review Created
     * @param event event
     */
    @EventListener
    public void handleReviewCreated(ReviewCreatedEvent event) {
        log.info("评价创建事件: orderNo={}, productId={}, rating={}",
                event.getOrderNo(), event.getProductId(), event.getRating());
        // 商品评分已在ReviewService中直接更新，此处仅记录日志
    }

    // ========== Private helper methods ==========

    private void createNotification(Long userId, String userType, String notificationType,
                                   String title, String content, Long referenceId, String referenceType) {
        SysNotificationDO notif = new SysNotificationDO();
        notif.setUserId(userId);
        notif.setUserType(userType);
        notif.setNotificationType(notificationType);
        notif.setTitle(title);
        notif.setContent(content);
        notif.setIsRead(0);
        notif.setReferenceId(referenceId);
        notif.setReferenceType(referenceType);
        sysNotificationMapper.insert(notif);
    }

    private Long getMerchantUserIdByShopId(Long shopId) {
        if (shopId == null) return null;
        ShopDO shop = shopMapper.selectById(shopId);
        if (shop == null || shop.getMerchantId() == null) return null;
        MerchantDO merchant = merchantMapper.selectById(shop.getMerchantId());
        return merchant != null ? merchant.getUserId() : null;
    }

    private Long getUserIdByOrderNo(String orderNo) {
        if (orderNo == null) return null;
        LambdaQueryWrapper<OrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDO::getOrderNo, orderNo);
        OrderDO order = orderMapper.selectOne(wrapper);
        return order != null ? order.getUserId() : null;
    }
}
