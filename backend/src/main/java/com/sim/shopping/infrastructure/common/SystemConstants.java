package com.sim.shopping.infrastructure.common;

/**
 * 系统级公共常量，集中管理角色、状态、类型等高频硬编码魔法值
 * 避免在多个Service/Controller中重复定义相同的字符串/数字字面量
 *
 * @author Sim Team
 * @since 1.0.0
 */
public final class SystemConstants {

    private SystemConstants() {
        // 工具类禁止实例化
    }

    // ========== 用户角色 ==========
    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_SUPER_ADMIN = "SUPER_ADMIN";
    public static final String ROLE_MERCHANT = "MERCHANT";

    // ========== 通用状态 ==========
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_INACTIVE = "INACTIVE";
    public static final String STATUS_DISABLED = "DISABLED";
    public static final String STATUS_ENDED = "ENDED";

    // ========== 积分记录类型 ==========
    public static final String POINTS_TYPE_EARN = "EARN";
    public static final String POINTS_TYPE_SPEND = "SPEND";

    // ========== 积分来源 ==========
    public static final String POINTS_SOURCE_ORDER_REWARD = "ORDER_REWARD";
    public static final String POINTS_SOURCE_EXCHANGE = "EXCHANGE";

    // ========== 商品状态 ==========
    public static final String PRODUCT_STATUS_PUBLISHED = "PUBLISHED";
    public static final String PRODUCT_STATUS_DRAFT = "DRAFT";
    public static final String PRODUCT_STATUS_OFFLINE = "OFFLINE";

    // ========== 通知类型 ==========
    public static final String NOTIFICATION_TYPE_ORDER = "ORDER";
    public static final String NOTIFICATION_TYPE_MERCHANT = "MERCHANT";
    public static final String NOTIFICATION_TYPE_SYSTEM = "SYSTEM";

    // ========== 优惠券状态 ==========
    public static final String COUPON_STATUS_ACTIVE = "ACTIVE";
    public static final String COUPON_STATUS_USED = "USED";
    public static final String COUPON_STATUS_EXPIRED = "EXPIRED";

    // ========== 支付相关 ==========
    public static final String PAYMENT_METHOD_ALIPAY = "ALIPAY";
    public static final String PAYMENT_METHOD_WECHAT = "WECHAT";
    public static final String PAYMENT_STATUS_PENDING = "PENDING";
    public static final String PAYMENT_STATUS_SUCCESS = "SUCCESS";

    // ========== 物流状态 ==========
    public static final String SHIPMENT_STATUS_PENDING = "PENDING";
    public static final String SHIPMENT_STATUS_SHIPPED = "SHIPPED";
    public static final String SHIPMENT_STATUS_DELIVERED = "DELIVERED";

    // ========== 评价状态 ==========
    public static final String REVIEW_STATUS_VISIBLE = "VISIBLE";
    public static final String REVIEW_STATUS_HIDDEN = "HIDDEN";

    // ========== 店铺状态 ==========
    public static final String SHOP_STATUS_ACTIVE = "ACTIVE";
    public static final String SHOP_STATUS_CLOSED = "CLOSED";

    // ========== 定时任务状态 ==========
    public static final String SCHEDULE_JOB_STATUS_ACTIVE = "ACTIVE";
    public static final String SCHEDULE_JOB_STATUS_PAUSED = "PAUSED";
    public static final String SCHEDULE_JOB_STATUS_RUNNING = "RUNNING";
    public static final String SCHEDULE_JOB_STATUS_ERROR = "ERROR";

    // ========== 活动状态 ==========
    public static final String ACTIVITY_STATUS_ACTIVE = "ACTIVE";
    public static final String ACTIVITY_STATUS_INACTIVE = "INACTIVE";

    // ========== 商家审核状态 ==========
    public static final String MERCHANT_STATUS_PENDING = "PENDING";
    public static final String MERCHANT_STATUS_APPROVED = "APPROVED";
    public static final String MERCHANT_STATUS_REJECTED = "REJECTED";

    // ========== 退款状态 ==========
    public static final String REFUND_STATUS_PENDING = "PENDING";
    public static final String REFUND_STATUS_COMPLETED = "COMPLETED";

    // ========== 结算类型与状态 ==========
    public static final String SETTLEMENT_TYPE_ORDER = "ORDER";
    public static final String SETTLEMENT_STATUS_SETTLED = "SETTLED";

    // ========== 退款类型 ==========
    public static final String REFUND_TYPE_REFUND_ONLY = "REFUND_ONLY";
    public static final String REFUND_TYPE_RETURN_REFUND = "RETURN_REFUND";

    // ========== 通知目标类型 ==========
    public static final String NOTIFICATION_TARGET_USER = "USER";
    public static final String NOTIFICATION_TARGET_MERCHANT = "MERCHANT";

    // ========== 通知主题 ==========
    public static final String NOTIFICATION_TOPIC_ORDER = "ORDER";
    public static final String NOTIFICATION_TOPIC_SHIPMENT = "SHIPMENT";
    public static final String NOTIFICATION_TOPIC_LOGISTICS = "LOGISTICS";

    // ========== 通知关联类型 ==========
    public static final String NOTIFICATION_RELATED_TYPE_SHOP = "SHOP";
    public static final String NOTIFICATION_RELATED_TYPE_SHIPMENT = "SHIPMENT";
    public static final String NOTIFICATION_RELATED_TYPE_ORDER = "ORDER";

    // ========== 用户优惠券状态 ==========
    public static final String USER_COUPON_STATUS_CLAIMED = "CLAIMED";
    public static final String USER_COUPON_STATUS_USED = "USED";
    public static final String USER_COUPON_STATUS_EXPIRED = "EXPIRED";

    // ========== 日志与限制 ==========
    public static final int LOG_TRUNCATE_LENGTH = 500;

    // ========== 登录日志状态 ==========
    public static final int LOGIN_STATUS_SUCCESS = 1;
    public static final int LOGIN_STATUS_FAILURE = 0;

    // ========== 登录类型 ==========
    public static final String LOGIN_TYPE_LOGIN = "LOGIN";
    public static final String LOGIN_TYPE_LOGOUT = "LOGOUT";

    // ========== 性别 ==========
    public static final int GENDER_UNKNOWN = 0;
    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 2;
}
