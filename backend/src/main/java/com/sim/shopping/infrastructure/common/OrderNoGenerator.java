package com.sim.shopping.infrastructure.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 订单号生成工具，提供线程安全的订单号生成方法
 *
 * @author Sim Team
 * @since 1.0.0
 */
public final class OrderNoGenerator {

    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private OrderNoGenerator() {
        // 工具类禁止实例化
    }

    /**
     * 生成普通订单号（格式：SD + 时间戳 + 4位随机码）
     * @return 订单号
     */
    public static String generateNormalOrderNo() {
        return generateOrderNo(OrderConstants.ORDER_PREFIX_NORMAL);
    }

    /**
     * 生成秒杀订单号（格式：FLAS + 时间戳 + 4位随机码）
     * @return 秒杀订单号
     */
    public static String generateFlashSaleOrderNo() {
        return generateOrderNo(OrderConstants.ORDER_PREFIX_FLASH_SALE);
    }

    /**
     * 生成自定义前缀订单号
     * @param prefix 前缀
     * @return 订单号
     */
    public static String generateOrderNo(String prefix) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        String random = randomAlphanumeric(OrderConstants.ORDER_NO_RANDOM_LENGTH);
        return prefix + timestamp + random;
    }

    /**
     * 生成指定长度的随机字母数字字符串
     * @param length 长度
     * @return 随机字符串
     */
    public static String randomAlphanumeric(int length) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
}
