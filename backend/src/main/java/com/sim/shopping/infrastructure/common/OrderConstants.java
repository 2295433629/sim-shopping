package com.sim.shopping.infrastructure.common;

import java.math.BigDecimal;

/**
 * 订单模块公共常量，集中管理订单相关的状态、金额、前缀等常量，避免在多个Service中重复定义
 *
 * @author Sim Team
 * @since 1.0.0
 */
public final class OrderConstants {

    private OrderConstants() {
        // 工具类禁止实例化
    }

    // ========== 默认运费 ==========
    public static final BigDecimal DEFAULT_SHIPPING_FEE = new BigDecimal("10.00");

    // ========== 订单状态 ==========
    public static final String ORDER_STATUS_CREATED = "CREATED";
    public static final String ORDER_STATUS_PAID = "PAID";
    public static final String ORDER_STATUS_SHIPPED = "SHIPPED";
    public static final String ORDER_STATUS_IN_TRANSIT = "IN_TRANSIT";
    public static final String ORDER_STATUS_DELIVERED = "DELIVERED";
    public static final String ORDER_STATUS_COMPLETED = "COMPLETED";
    public static final String ORDER_STATUS_CANCELLED = "CANCELLED";

    // ========== 订单号前缀 ==========
    public static final String ORDER_PREFIX_NORMAL = "SD";
    public static final String ORDER_PREFIX_FLASH_SALE = "FLAS";

    // ========== 随机码长度 ==========
    public static final int ORDER_NO_RANDOM_LENGTH = 4;

    // ========== 购物车限制 ==========
    public static final int MAX_CART_ITEM_QUANTITY = 99;

    // ========== 积分换算 ==========
    public static final int POINTS_PER_YUAN = 10;

    // ========== 支付相关 ==========
    public static final String DEFAULT_PAYMENT_METHOD = "WECHAT";
}
