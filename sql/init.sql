-- ============================================================
-- ShoppingDream 数据库初始化脚本
-- DB-001 v1.0.0 | MySQL 8.0+ / 9.x
-- 生成日期: 2026-07-03
-- ============================================================

CREATE DATABASE IF NOT EXISTS sim_shopping DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE sim_shopping;

-- ============================================================
-- 1. 用户领域 (User Domain)
-- ============================================================

DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码（BCrypt）',
  `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
  `gender` TINYINT DEFAULT 0 COMMENT '性别(0=未知,1=男,2=女)',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `role` VARCHAR(20) NOT NULL DEFAULT 'USER' COMMENT '角色(USER/MERCHANT/ADMIN)',
  `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE/DISABLED)',
  `points` INT NOT NULL DEFAULT 0 COMMENT '积分',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `created_by` BIGINT DEFAULT NULL COMMENT '创建人',
  `updated_by` BIGINT DEFAULT NULL COMMENT '更新人',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '软删除(0=正常,1=已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_phone` (`phone`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

DROP TABLE IF EXISTS `t_user_address`;
CREATE TABLE `t_user_address` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `receiver_name` VARCHAR(50) NOT NULL COMMENT '收货人姓名',
  `receiver_phone` VARCHAR(20) NOT NULL COMMENT '收货人电话',
  `province` VARCHAR(50) NOT NULL COMMENT '省',
  `city` VARCHAR(50) NOT NULL COMMENT '市',
  `district` VARCHAR(50) NOT NULL COMMENT '区',
  `detail_address` VARCHAR(255) NOT NULL COMMENT '详细地址',
  `is_default` TINYINT NOT NULL DEFAULT 0 COMMENT '是否默认(0=否,1=是)',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户收货地址表';

-- ============================================================
-- 2. 商家领域 (Merchant Domain)
-- ============================================================

DROP TABLE IF EXISTS `t_merchant`;
CREATE TABLE `t_merchant` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '关联用户ID',
  `merchant_name` VARCHAR(100) NOT NULL COMMENT '商家名称',
  `contact_phone` VARCHAR(20) NOT NULL COMMENT '联系电话',
  `contact_email` VARCHAR(100) DEFAULT NULL COMMENT '联系邮箱',
  `business_license` VARCHAR(255) DEFAULT NULL COMMENT '营业执照URL',
  `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态(PENDING/APPROVED/ACTIVE/DISABLED/REJECTED)',
  `approved_at` DATETIME DEFAULT NULL COMMENT '审核通过时间',
  `approved_by` BIGINT DEFAULT NULL COMMENT '审核人ID',
  `reject_reason` VARCHAR(500) DEFAULT NULL COMMENT '拒绝原因',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商家表';

-- ============================================================
-- 3. 店铺领域 (Shop Domain)
-- ============================================================

DROP TABLE IF EXISTS `t_shop`;
CREATE TABLE `t_shop` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `merchant_id` BIGINT UNSIGNED NOT NULL COMMENT '商家ID',
  `shop_name` VARCHAR(100) NOT NULL COMMENT '店铺名称',
  `shop_logo` VARCHAR(255) DEFAULT NULL COMMENT '店铺Logo URL',
  `description` TEXT COMMENT '店铺描述',
  `balance` DECIMAL(12,2) NOT NULL DEFAULT 0.00 COMMENT '可用余额',
  `total_income` DECIMAL(12,2) NOT NULL DEFAULT 0.00 COMMENT '累计总收入',
  `total_settled` DECIMAL(12,2) NOT NULL DEFAULT 0.00 COMMENT '已结算金额',
  `frozen_amount` DECIMAL(12,2) NOT NULL DEFAULT 0.00 COMMENT '冻结金额（待结算）',
  `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE/DISABLED)',
  `sender_name` VARCHAR(50) DEFAULT NULL COMMENT '发货人姓名',
  `sender_phone` VARCHAR(20) DEFAULT NULL COMMENT '发货人电话',
  `sender_province` VARCHAR(30) DEFAULT NULL COMMENT '发货省份',
  `sender_city` VARCHAR(30) DEFAULT NULL COMMENT '发货城市',
  `sender_district` VARCHAR(30) DEFAULT NULL COMMENT '发货区县',
  `sender_address` VARCHAR(200) DEFAULT NULL COMMENT '发货详细地址',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_merchant_id` (`merchant_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='店铺表';

-- ============================================================
-- 增量迁移：为已有 t_shop 表添加发货地址字段（如果不存在）
-- ============================================================
-- ALTER TABLE t_shop ADD COLUMN sender_name VARCHAR(50) DEFAULT NULL COMMENT '发货人姓名';
-- ALTER TABLE t_shop ADD COLUMN sender_phone VARCHAR(20) DEFAULT NULL COMMENT '发货人电话';
-- ALTER TABLE t_shop ADD COLUMN sender_province VARCHAR(30) DEFAULT NULL COMMENT '发货省份';
-- ALTER TABLE t_shop ADD COLUMN sender_city VARCHAR(30) DEFAULT NULL COMMENT '发货城市';
-- ALTER TABLE t_shop ADD COLUMN sender_district VARCHAR(30) DEFAULT NULL COMMENT '发货区县';
-- ALTER TABLE t_shop ADD COLUMN sender_address VARCHAR(200) DEFAULT NULL COMMENT '发货详细地址';

-- ============================================================
-- 增量迁移：为已有 t_logistics_record 表添加地址字段（如果不存在）
-- ============================================================
-- ALTER TABLE t_logistics_record ADD COLUMN sender_address VARCHAR(500) DEFAULT NULL COMMENT '发货地址（快照）';
-- ALTER TABLE t_logistics_record ADD COLUMN receiver_address VARCHAR(500) DEFAULT NULL COMMENT '收货地址（快照）';
-- ALTER TABLE t_logistics_record ADD COLUMN sender_city VARCHAR(50) DEFAULT NULL COMMENT '发货城市';

-- ============================================================
-- 4. 商品领域 (Product Domain)
-- ============================================================

DROP TABLE IF EXISTS `t_category`;
CREATE TABLE `t_category` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `parent_id` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '父分类ID(0=根)',
  `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
  `icon` VARCHAR(255) DEFAULT NULL COMMENT '分类图标URL',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序值',
  `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE/DISABLED)',
  `level` TINYINT NOT NULL DEFAULT 1 COMMENT '层级(1=一级,2=二级)',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_level` (`level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品分类表';

DROP TABLE IF EXISTS `t_brand`;
CREATE TABLE `t_brand` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `brand_name` VARCHAR(100) NOT NULL COMMENT '品牌名称',
  `brand_logo` VARCHAR(500) DEFAULT NULL COMMENT '品牌Logo URL',
  `brand_description` TEXT COMMENT '品牌描述',
  `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE/DISABLED)',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_brand_name` (`brand_name`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='品牌表';

DROP TABLE IF EXISTS `t_product`;
CREATE TABLE `t_product` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `shop_id` BIGINT UNSIGNED NOT NULL COMMENT '店铺ID',
  `category_id` BIGINT UNSIGNED NOT NULL COMMENT '分类ID',
  `brand_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '品牌ID',
  `name` VARCHAR(200) NOT NULL COMMENT '商品名称',
  `subtitle` VARCHAR(255) DEFAULT NULL COMMENT '副标题',
  `description` TEXT COMMENT '商品描述（富文本）',
  `main_image` VARCHAR(255) DEFAULT NULL COMMENT '主图URL',
  `price` DECIMAL(10,2) NOT NULL COMMENT '售价',
  `original_price` DECIMAL(10,2) DEFAULT NULL COMMENT '原价',
  `stock` INT NOT NULL DEFAULT 0 COMMENT '库存',
  `sales` INT NOT NULL DEFAULT 0 COMMENT '销量',
  `view_count` INT NOT NULL DEFAULT 0 COMMENT '浏览量',
  `favorite_count` INT NOT NULL DEFAULT 0 COMMENT '收藏数',
  `review_count` INT NOT NULL DEFAULT 0 COMMENT '评价数',
  `avg_review_score` DECIMAL(2,1) NOT NULL DEFAULT 0.0 COMMENT '平均评分',
  `is_recommended` TINYINT NOT NULL DEFAULT 0 COMMENT '是否推荐',
  `is_new` TINYINT NOT NULL DEFAULT 0 COMMENT '是否新品',
  `publish_time` DATETIME DEFAULT NULL COMMENT '发布时间',
  `status` VARCHAR(20) NOT NULL DEFAULT 'DRAFT' COMMENT '状态(DRAFT/PUBLISHED/OFFLINE/DELETED)',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_shop_id` (`shop_id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_brand_id` (`brand_id`),
  KEY `idx_status` (`status`),
  KEY `idx_is_recommended` (`is_recommended`),
  KEY `idx_sales` (`sales`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品表';

DROP TABLE IF EXISTS `t_product_image`;
CREATE TABLE `t_product_image` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `product_id` BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
  `image_url` VARCHAR(500) NOT NULL COMMENT '图片URL',
  `image_type` TINYINT NOT NULL DEFAULT 1 COMMENT '类型(1=主图,2=详情图)',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序值',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品图片表';

DROP TABLE IF EXISTS `t_product_sku`;
CREATE TABLE `t_product_sku` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `product_id` BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
  `sku_name` VARCHAR(200) NOT NULL COMMENT 'SKU名称',
  `sku_code` VARCHAR(100) DEFAULT NULL COMMENT 'SKU编码',
  `price` DECIMAL(10,2) NOT NULL COMMENT '规格价格',
  `stock` INT NOT NULL DEFAULT 0 COMMENT '规格库存',
  `attributes` JSON DEFAULT NULL COMMENT 'SKU属性JSON',
  `image_url` VARCHAR(500) DEFAULT NULL COMMENT 'SKU图片URL',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`),
  UNIQUE KEY `uk_sku_code` (`sku_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品规格表';

DROP TABLE IF EXISTS `t_favorite`;
CREATE TABLE `t_favorite` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `product_id` BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_product` (`user_id`, `product_id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收藏表';

DROP TABLE IF EXISTS `t_browse_history`;
CREATE TABLE `t_browse_history` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `product_id` BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
  `viewed_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '浏览时间',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_user_id_viewed_at` (`user_id`, `viewed_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='浏览历史表';

DROP TABLE IF EXISTS `t_search_history`;
CREATE TABLE `t_search_history` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `keyword` VARCHAR(100) NOT NULL COMMENT '搜索关键词',
  `searched_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '搜索时间',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_user_id_searched_at` (`user_id`, `searched_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='搜索历史表';

-- ============================================================
-- 5. 购物车领域 (ShoppingCart Domain)
-- ============================================================

DROP TABLE IF EXISTS `t_shopping_cart`;
CREATE TABLE `t_shopping_cart` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `shop_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '店铺ID（按店铺分组）',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='购物车表';

DROP TABLE IF EXISTS `t_cart_item`;
CREATE TABLE `t_cart_item` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `cart_id` BIGINT UNSIGNED NOT NULL COMMENT '购物车ID',
  `product_id` BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
  `shop_id` BIGINT UNSIGNED NOT NULL COMMENT '店铺ID（按店铺分组）',
  `quantity` INT NOT NULL DEFAULT 1 COMMENT '数量',
  `sku_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '规格ID',
  `selected` TINYINT NOT NULL DEFAULT 1 COMMENT '是否选中(0=未选,1=已选)',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_cart_product_sku` (`cart_id`, `product_id`, `sku_id`),
  KEY `idx_cart_id` (`cart_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='购物车项表';

-- ============================================================
-- 6. 订单领域 (Order Domain)
-- ============================================================

DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `order_no` VARCHAR(32) NOT NULL COMMENT '订单号',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '买家ID',
  `shop_id` BIGINT UNSIGNED NOT NULL COMMENT '店铺ID',
  `total_amount` DECIMAL(10,2) NOT NULL COMMENT '商品总金额',
  `shipping_fee` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '运费',
  `discount_amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '优惠金额',
  `pay_amount` DECIMAL(10,2) NOT NULL COMMENT '实付金额',
  `status` VARCHAR(20) NOT NULL DEFAULT 'CREATED' COMMENT '状态(CREATED/PAID/SHIPPED/IN_TRANSIT/DELIVERED/COMPLETED/CANCELLED)',
  `receiver_name` VARCHAR(50) NOT NULL COMMENT '收货人',
  `receiver_phone` VARCHAR(20) NOT NULL COMMENT '收货电话',
  `receiver_address` VARCHAR(500) NOT NULL COMMENT '收货地址（快照）',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '订单备注',
  `paid_at` DATETIME DEFAULT NULL COMMENT '支付时间',
  `shipped_at` DATETIME DEFAULT NULL COMMENT '发货时间',
  `delivered_at` DATETIME DEFAULT NULL COMMENT '签收时间',
  `completed_at` DATETIME DEFAULT NULL COMMENT '完成时间',
  `cancelled_at` DATETIME DEFAULT NULL COMMENT '取消时间',
  `auto_confirm` TINYINT NOT NULL DEFAULT 0 COMMENT '是否自动确认(0=否,1=是)',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id_status` (`user_id`, `status`),
  KEY `idx_shop_id_status` (`shop_id`, `status`),
  KEY `idx_status` (`status`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

DROP TABLE IF EXISTS `t_order_item`;
CREATE TABLE `t_order_item` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `order_id` BIGINT UNSIGNED NOT NULL COMMENT '订单ID',
  `product_id` BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
  `product_name` VARCHAR(200) NOT NULL COMMENT '商品名称（快照）',
  `product_image` VARCHAR(255) DEFAULT NULL COMMENT '商品图片（快照）',
  `sku_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '规格ID',
  `sku_name` VARCHAR(100) DEFAULT NULL COMMENT '规格名称（快照）',
  `price` DECIMAL(10,2) NOT NULL COMMENT '单价（快照）',
  `quantity` INT NOT NULL COMMENT '数量',
  `subtotal` DECIMAL(10,2) NOT NULL COMMENT '小计',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单项表';

-- ============================================================
-- 7. 支付领域 (Payment Domain)
-- ============================================================

DROP TABLE IF EXISTS `t_payment`;
CREATE TABLE `t_payment` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `order_id` BIGINT UNSIGNED NOT NULL COMMENT '订单ID',
  `order_no` VARCHAR(32) NOT NULL COMMENT '订单号',
  `payment_no` VARCHAR(64) NOT NULL COMMENT '支付流水号',
  `payment_method` VARCHAR(20) NOT NULL COMMENT '支付方式(MOCK_ALIPAY/MOCK_WECHAT)',
  `amount` DECIMAL(10,2) NOT NULL COMMENT '支付金额',
  `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态(PENDING/SUCCESS/FAILED/CLOSED)',
  `paid_at` DATETIME DEFAULT NULL COMMENT '支付成功时间',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_id` (`order_id`),
  KEY `idx_payment_no` (`payment_no`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='支付记录表';

-- ============================================================
-- 8. 发货领域 (Shipment Domain)
-- ============================================================

DROP TABLE IF EXISTS `t_shipment`;
CREATE TABLE `t_shipment` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `order_id` BIGINT UNSIGNED NOT NULL COMMENT '订单ID',
  `order_no` VARCHAR(32) NOT NULL COMMENT '订单号',
  `shop_id` BIGINT UNSIGNED NOT NULL COMMENT '店铺ID',
  `shipment_no` VARCHAR(64) NOT NULL COMMENT '发货单号',
  `logistics_company` VARCHAR(50) DEFAULT NULL COMMENT '物流公司',
  `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态(PENDING/PACKED/SHIPPED)',
  `shipped_at` DATETIME DEFAULT NULL COMMENT '发货时间',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_id` (`order_id`),
  KEY `idx_shop_id` (`shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='发货单表';

-- ============================================================
-- 9. 物流领域 (Logistics Domain)
-- ============================================================

DROP TABLE IF EXISTS `t_logistics_record`;
CREATE TABLE `t_logistics_record` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `shipment_id` BIGINT UNSIGNED NOT NULL COMMENT '发货单ID',
  `order_id` BIGINT UNSIGNED NOT NULL COMMENT '订单ID',
  `order_no` VARCHAR(32) NOT NULL COMMENT '订单号',
  `tracking_no` VARCHAR(64) NOT NULL COMMENT '物流单号',
  `logistics_company` VARCHAR(50) DEFAULT NULL COMMENT '物流公司',
  `status` VARCHAR(20) NOT NULL DEFAULT 'CREATED' COMMENT '状态(CREATED/PICKED_UP/SORTING/IN_TRANSIT/OUT_FOR_DELIVERY/DELIVERED)',
  `delivered_at` DATETIME DEFAULT NULL COMMENT '签收时间',
  `sender_address` VARCHAR(500) DEFAULT NULL COMMENT '发货地址（快照）',
  `receiver_address` VARCHAR(500) DEFAULT NULL COMMENT '收货地址（快照）',
  `sender_city` VARCHAR(50) DEFAULT NULL COMMENT '发货城市',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_shipment_id` (`shipment_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_tracking_no` (`tracking_no`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='物流记录表';

DROP TABLE IF EXISTS `t_logistics_track`;
CREATE TABLE `t_logistics_track` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `logistics_id` BIGINT UNSIGNED NOT NULL COMMENT '物流记录ID',
  `status` VARCHAR(20) NOT NULL COMMENT '轨迹状态',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '轨迹描述',
  `location` VARCHAR(100) DEFAULT NULL COMMENT '所在地',
  `occurred_at` DATETIME NOT NULL COMMENT '发生时间',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_logistics_id` (`logistics_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='物流轨迹表';

-- ============================================================
-- 10. 评价领域 (Review Domain)
-- ============================================================

DROP TABLE IF EXISTS `t_review`;
CREATE TABLE `t_review` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `order_id` BIGINT UNSIGNED NOT NULL COMMENT '订单ID',
  `order_no` VARCHAR(32) NOT NULL COMMENT '订单号',
  `product_id` BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '评价人ID',
  `shop_id` BIGINT UNSIGNED NOT NULL COMMENT '店铺ID',
  `rating` TINYINT NOT NULL DEFAULT 5 COMMENT '评分(1-5)',
  `content` TEXT COMMENT '评价内容',
  `status` VARCHAR(20) NOT NULL DEFAULT 'VISIBLE' COMMENT '状态(CREATED/VISIBLE/HIDDEN/DELETED)',
  `merchant_reply` TEXT COMMENT '商家回复',
  `merchant_replied_at` DATETIME DEFAULT NULL COMMENT '商家回复时间',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_product_id_status` (`product_id`, `status`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_shop_id` (`shop_id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评价表';

DROP TABLE IF EXISTS `t_review_image`;
CREATE TABLE `t_review_image` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `review_id` BIGINT UNSIGNED NOT NULL COMMENT '评价ID',
  `image_url` VARCHAR(255) NOT NULL COMMENT '图片URL',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_review_id` (`review_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评价图片表';

-- ============================================================
-- 11. 营销领域 (Marketing Domain)
-- ============================================================

DROP TABLE IF EXISTS `t_sign_in_record`;
CREATE TABLE `t_sign_in_record` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `sign_date` DATE NOT NULL COMMENT '签到日期',
  `consecutive_days` INT NOT NULL DEFAULT 1 COMMENT '连续签到天数',
  `points_earned` INT NOT NULL DEFAULT 1 COMMENT '获得积分',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_date` (`user_id`, `sign_date`),
  KEY `idx_sign_date` (`sign_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='签到记录表';

DROP TABLE IF EXISTS `t_banner`;
CREATE TABLE `t_banner` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(100) NOT NULL COMMENT '标题',
  `image_url` VARCHAR(255) NOT NULL COMMENT '图片URL',
  `link_url` VARCHAR(255) DEFAULT NULL COMMENT '跳转链接',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序',
  `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE/INACTIVE)',
  `start_time` DATETIME DEFAULT NULL COMMENT '展示开始时间',
  `end_time` DATETIME DEFAULT NULL COMMENT '展示结束时间',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_status_sort` (`status`, `sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='首页Banner表';

-- ============================================================
-- 12. 系统领域 (System Domain)
-- ============================================================

DROP TABLE IF EXISTS `t_sys_admin`;
CREATE TABLE `t_sys_admin` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '关联用户ID',
  `admin_name` VARCHAR(50) NOT NULL COMMENT '管理员名称',
  `role` VARCHAR(20) NOT NULL DEFAULT 'ADMIN' COMMENT '角色(ADMIN/SUPER_ADMIN)',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员表';

DROP TABLE IF EXISTS `t_sys_role`;
CREATE TABLE `t_sys_role` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `role_code` VARCHAR(50) NOT NULL COMMENT '角色编码',
  `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '描述',
  `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE/DISABLED)',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

DROP TABLE IF EXISTS `t_sys_permission`;
CREATE TABLE `t_sys_permission` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `permission_code` VARCHAR(100) NOT NULL COMMENT '权限编码',
  `permission_name` VARCHAR(100) NOT NULL COMMENT '权限名称',
  `permission_type` TINYINT NOT NULL COMMENT '类型(1=菜单,2=按钮,3=接口)',
  `module` VARCHAR(50) NOT NULL COMMENT '所属模块',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '描述',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_permission_code` (`permission_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';

DROP TABLE IF EXISTS `t_sys_role_permission`;
CREATE TABLE `t_sys_role_permission` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `role_id` BIGINT UNSIGNED NOT NULL COMMENT '角色ID',
  `permission_id` BIGINT UNSIGNED NOT NULL COMMENT '权限ID',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';

DROP TABLE IF EXISTS `t_sys_role_menu`;
CREATE TABLE `t_sys_role_menu` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `role_id` BIGINT UNSIGNED NOT NULL COMMENT '角色ID',
  `menu_id` BIGINT UNSIGNED NOT NULL COMMENT '菜单ID',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_menu` (`role_id`, `menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单关联表';

DROP TABLE IF EXISTS `t_sys_menu`;
CREATE TABLE `t_sys_menu` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `parent_id` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '父菜单ID',
  `name` VARCHAR(50) NOT NULL COMMENT '菜单名称',
  `path` VARCHAR(200) DEFAULT NULL COMMENT '路由路径',
  `component` VARCHAR(200) DEFAULT NULL COMMENT '组件路径',
  `icon` VARCHAR(50) DEFAULT NULL COMMENT '图标',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序',
  `type` VARCHAR(20) NOT NULL DEFAULT 'MENU' COMMENT '类型(MENU/BUTTON)',
  `permission` VARCHAR(100) DEFAULT NULL COMMENT '权限标识',
  `visible` TINYINT NOT NULL DEFAULT 1 COMMENT '是否可见',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单表';

DROP TABLE IF EXISTS `t_sys_dict_type`;
CREATE TABLE `t_sys_dict_type` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `dict_name` VARCHAR(50) NOT NULL COMMENT '字典名称',
  `dict_code` VARCHAR(50) NOT NULL COMMENT '字典编码',
  `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dict_code` (`dict_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='字典类型表';

DROP TABLE IF EXISTS `t_sys_dict_item`;
CREATE TABLE `t_sys_dict_item` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `dict_type_id` BIGINT UNSIGNED NOT NULL COMMENT '字典类型ID',
  `label` VARCHAR(100) NOT NULL COMMENT '标签',
  `value` VARCHAR(100) NOT NULL COMMENT '值',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序',
  `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_dict_type_id` (`dict_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='字典项表';

DROP TABLE IF EXISTS `t_sys_config`;
CREATE TABLE `t_sys_config` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `config_key` VARCHAR(100) NOT NULL COMMENT '配置键',
  `config_value` TEXT NOT NULL COMMENT '配置值',
  `config_name` VARCHAR(100) NOT NULL COMMENT '配置名称',
  `config_type` VARCHAR(50) NOT NULL COMMENT '配置类型(string/number/boolean/json)',
  `module` VARCHAR(50) NOT NULL COMMENT '所属模块',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '描述',
  `is_system` TINYINT NOT NULL DEFAULT 0 COMMENT '是否系统内置(0=否,1=是)',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

DROP TABLE IF EXISTS `t_sys_operation_log`;
CREATE TABLE `t_sys_operation_log` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `operator_id` BIGINT UNSIGNED NOT NULL COMMENT '操作人ID',
  `operator_name` VARCHAR(50) NOT NULL COMMENT '操作人名称',
  `operator_type` VARCHAR(20) DEFAULT NULL COMMENT '操作人类型(USER/MERCHANT/ADMIN)',
  `module` VARCHAR(50) NOT NULL COMMENT '操作模块',
  `operation_type` VARCHAR(50) NOT NULL COMMENT '操作类型',
  `method` VARCHAR(200) DEFAULT NULL COMMENT '请求方法',
  `request_url` VARCHAR(500) DEFAULT NULL COMMENT '请求URL',
  `request_params` TEXT COMMENT '请求参数',
  `response_result` TEXT COMMENT '响应结果',
  `ip` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
  `cost_time` BIGINT DEFAULT NULL COMMENT '耗时(ms)',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_operator_id` (`operator_id`),
  KEY `idx_module` (`module`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

DROP TABLE IF EXISTS `t_sys_login_log`;
CREATE TABLE `t_sys_login_log` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '用户ID',
  `user_type` VARCHAR(20) NOT NULL COMMENT '用户类型(USER/MERCHANT/ADMIN)',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `login_type` VARCHAR(20) NOT NULL COMMENT '类型(LOGIN/LOGOUT/REGISTER)',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态(0=失败,1=成功)',
  `ip` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
  `user_agent` VARCHAR(500) DEFAULT NULL COMMENT 'User-Agent',
  `error_msg` VARCHAR(255) DEFAULT NULL COMMENT '错误信息',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_user_id_type` (`user_id`, `user_type`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='登录日志表';

DROP TABLE IF EXISTS `t_sys_notification`;
CREATE TABLE `t_sys_notification` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '接收用户ID',
  `user_type` VARCHAR(20) NOT NULL COMMENT '用户类型(USER/MERCHANT/ADMIN)',
  `notification_type` VARCHAR(30) NOT NULL COMMENT '类型(ORDER/PAYMENT/SHIPMENT/LOGISTICS/REVIEW/SYSTEM)',
  `title` VARCHAR(100) NOT NULL COMMENT '标题',
  `content` TEXT NOT NULL COMMENT '内容',
  `reference_id` BIGINT DEFAULT NULL COMMENT '关联ID',
  `reference_type` VARCHAR(30) DEFAULT NULL COMMENT '关联类型',
  `is_read` TINYINT NOT NULL DEFAULT 0 COMMENT '是否已读(0=未读,1=已读)',
  `read_at` DATETIME DEFAULT NULL COMMENT '阅读时间',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_user_type` (`user_id`, `user_type`),
  KEY `idx_is_read` (`is_read`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='站内消息表';

DROP TABLE IF EXISTS `t_sys_file`;
CREATE TABLE `t_sys_file` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `original_name` VARCHAR(255) NOT NULL COMMENT '原始文件名',
  `file_name` VARCHAR(255) NOT NULL COMMENT '存储文件名',
  `file_path` VARCHAR(500) NOT NULL COMMENT '文件路径',
  `file_url` VARCHAR(500) NOT NULL COMMENT '访问URL',
  `file_type` VARCHAR(50) NOT NULL COMMENT 'MIME类型',
  `file_size` BIGINT NOT NULL COMMENT '文件大小(字节)',
  `file_category` VARCHAR(20) NOT NULL COMMENT '分类(PRODUCT/AVATAR/REVIEW/SHOP/SYSTEM)',
  `uploader_id` BIGINT DEFAULT NULL COMMENT '上传者ID',
  `uploader_type` VARCHAR(20) DEFAULT NULL COMMENT '上传者类型',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_uploader` (`uploader_id`, `uploader_type`),
  KEY `idx_file_category` (`file_category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件表';

DROP TABLE IF EXISTS `sys_cache`;
CREATE TABLE `sys_cache` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `cache_key` VARCHAR(255) NOT NULL COMMENT '缓存键',
  `cache_value` LONGTEXT COMMENT '缓存值(JSON)',
  `expired_at` DATETIME NOT NULL COMMENT '过期时间',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_cache_key` (`cache_key`),
  KEY `idx_expired_at` (`expired_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据缓存表（Redis不可用时作为降级缓存）';

DROP TABLE IF EXISTS `t_activity`;
CREATE TABLE `t_activity` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `activity_name` VARCHAR(100) NOT NULL COMMENT '活动名称',
  `banner_image` VARCHAR(255) DEFAULT NULL COMMENT 'Banner图片URL',
  `description` TEXT COMMENT '活动描述',
  `start_time` DATETIME DEFAULT NULL COMMENT '开始时间',
  `end_time` DATETIME DEFAULT NULL COMMENT '结束时间',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序值',
  `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE/INACTIVE)',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_start_end_time` (`start_time`, `end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='活动表';

DROP TABLE IF EXISTS `t_points_product`;
CREATE TABLE `t_points_product` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `product_name` VARCHAR(200) NOT NULL COMMENT '商品名称',
  `description` TEXT COMMENT '商品描述',
  `image_url` VARCHAR(500) DEFAULT NULL COMMENT '商品图片URL',
  `points_required` INT NOT NULL DEFAULT 0 COMMENT '所需积分',
  `stock` INT NOT NULL DEFAULT 0 COMMENT '库存',
  `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE/INACTIVE)',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='积分商品表';

DROP TABLE IF EXISTS `t_coupon`;
CREATE TABLE `t_coupon` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `coupon_code` VARCHAR(50) DEFAULT NULL COMMENT '优惠券编码',
  `coupon_name` VARCHAR(100) NOT NULL COMMENT '优惠券名称',
  `coupon_type` VARCHAR(20) NOT NULL COMMENT '类型(FIXED_AMOUNT/PERCENTAGE)',
  `discount_value` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '优惠金额/折扣比例',
  `min_spend` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '最低消费金额',
  `total_quantity` INT NOT NULL DEFAULT 0 COMMENT '总发行量',
  `claimed_quantity` INT NOT NULL DEFAULT 0 COMMENT '已领取数量',
  `used_quantity` INT NOT NULL DEFAULT 0 COMMENT '已使用数量',
  `valid_start_time` DATETIME DEFAULT NULL COMMENT '有效期开始',
  `valid_end_time` DATETIME DEFAULT NULL COMMENT '有效期结束',
  `applicable_scope` VARCHAR(20) DEFAULT 'ALL' COMMENT '适用范围(ALL/SHOP/CATEGORY/PRODUCT)',
  `applicable_ids` VARCHAR(500) DEFAULT NULL COMMENT '适用对象ID列表',
  `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE/INACTIVE/EXPIRED)',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_coupon_code` (`coupon_code`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='优惠券表';

DROP TABLE IF EXISTS `t_flash_sale`;
CREATE TABLE `t_flash_sale` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `product_id` BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
  `original_price` DECIMAL(10,2) NOT NULL COMMENT '原价',
  `flash_price` DECIMAL(10,2) NOT NULL COMMENT '秒杀价',
  `stock` INT NOT NULL DEFAULT 0 COMMENT '秒杀库存',
  `sold_count` INT NOT NULL DEFAULT 0 COMMENT '已售数量',
  `start_time` DATETIME NOT NULL COMMENT '开始时间',
  `end_time` DATETIME NOT NULL COMMENT '结束时间',
  `limit_per_user` INT NOT NULL DEFAULT 1 COMMENT '每人限购',
  `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE/INACTIVE/ENDED)',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_status` (`status`),
  KEY `idx_start_end_time` (`start_time`, `end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='秒杀活动表';

DROP TABLE IF EXISTS `t_order_refund`;
CREATE TABLE `t_order_refund` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` BIGINT UNSIGNED NOT NULL COMMENT '订单ID',
  `order_no` VARCHAR(32) NOT NULL COMMENT '订单号',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `refund_type` VARCHAR(20) NOT NULL COMMENT '退款类型(REFUND/RETURN_REFUND)',
  `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态(PENDING/APPROVED/REJECTED/COMPLETED)',
  `reason` VARCHAR(255) DEFAULT NULL COMMENT '退款原因',
  `amount` DECIMAL(10,2) NOT NULL COMMENT '退款金额',
  `shop_response` TEXT COMMENT '商家回复',
  `handled_at` DATETIME DEFAULT NULL COMMENT '处理时间',
  `completed_at` DATETIME DEFAULT NULL COMMENT '完成时间',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='退款表';

DROP TABLE IF EXISTS `t_user_coupon`;
CREATE TABLE `t_user_coupon` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `coupon_id` BIGINT UNSIGNED NOT NULL COMMENT '优惠券ID',
  `claimed_at` DATETIME DEFAULT NULL COMMENT '领取时间',
  `used_at` DATETIME DEFAULT NULL COMMENT '使用时间',
  `order_no` VARCHAR(32) DEFAULT NULL COMMENT '使用订单号',
  `status` VARCHAR(20) NOT NULL DEFAULT 'CLAIMED' COMMENT '状态(CLAIMED/USED/EXPIRED)',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_coupon_id` (`coupon_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户优惠券表';

DROP TABLE IF EXISTS `t_user_points`;
CREATE TABLE `t_user_points` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `total_points` INT NOT NULL DEFAULT 0 COMMENT '总积分',
  `available_points` INT NOT NULL DEFAULT 0 COMMENT '可用积分',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户积分账户表';

DROP TABLE IF EXISTS `t_points_record`;
CREATE TABLE `t_points_record` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `points` INT NOT NULL DEFAULT 0 COMMENT '变动积分',
  `type` VARCHAR(20) NOT NULL COMMENT '类型(EARN/SPEND/REFUND)',
  `source` VARCHAR(50) DEFAULT NULL COMMENT '来源',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '描述',
  `related_id` BIGINT DEFAULT NULL COMMENT '关联ID',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='积分变动记录表';

DROP TABLE IF EXISTS `t_settlement_record`;
CREATE TABLE `t_settlement_record` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `shop_id` BIGINT UNSIGNED NOT NULL COMMENT '店铺ID',
  `order_id` BIGINT UNSIGNED NOT NULL COMMENT '订单ID',
  `order_no` VARCHAR(32) NOT NULL COMMENT '订单号',
  `amount` DECIMAL(10,2) NOT NULL COMMENT '结算金额',
  `type` VARCHAR(20) NOT NULL COMMENT '类型(ORDER_SETTLEMENT/REFUND_DEDUCTION)',
  `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态(PENDING/SETTLED/CANCELLED)',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '描述',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_shop_id` (`shop_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='结算记录表';

DROP TABLE IF EXISTS `t_activity_product`;
CREATE TABLE `t_activity_product` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `activity_id` BIGINT UNSIGNED NOT NULL COMMENT '活动ID',
  `product_id` BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序值',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` BIGINT DEFAULT NULL,
  `updated_by` BIGINT DEFAULT NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_activity_id` (`activity_id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='活动商品关联表';

-- ============================================================
-- 初始化数据
-- ============================================================

-- 默认管理员 (密码: admin123, BCrypt加密)
INSERT INTO `t_user` (`username`, `password`, `nickname`, `role`, `status`, `phone`, `email`, `points`) VALUES
('admin', '$2a$10$7MaASWMlYItDrcW.KLnGYOqklzVZ9mlIIJToy0EVzCLZ0jFkwegJ2', '超级管理员', 'ADMIN', 'ACTIVE', '13000000000', 'admin@sim.com', 0);

-- 测试商家用户 (密码各不相同)
INSERT INTO `t_user` (`username`, `password`, `nickname`, `role`, `status`, `phone`, `email`, `points`) VALUES
('merchant1', '$2a$10$CIAvymesYwLeArEOQ9fDWO.SEANZ91QLNjC7lF3JfyZw8PyMZPFgS', '模拟商城官方', 'MERCHANT', 'ACTIVE', '13800138001', 'merchant1@test.com', 0),
('merchant2', '$2a$10$6k6e80g3ndhEDPjYfPqC8OBVGYbahlh6IXSuXT9WmFtchoUkZQizi', '美妆女王', 'MERCHANT', 'ACTIVE', '13800138002', 'merchant2@test.com', 0),
('merchant3', '$2a$10$ChR2p6vItrBlvGUN64carOZNXaTCKsP7ks.Ra1zVXTao64Rk4iAsm', '潮流服饰', 'MERCHANT', 'ACTIVE', '13800138003', 'merchant3@test.com', 0),
('merchant4', '$2a$10$BJzOeTxeCZy8IePltNVG5.bfVinpFviow9Zkr9Hw5dt6SUgSli6OC', '美食天下', 'MERCHANT', 'ACTIVE', '13800138004', 'merchant4@test.com', 0),
('merchant5', '$2a$10$eiIDa9dRIWodYW7fo3KV5OyFXF2QpVrnErrBbTtzbmf9kiCCks5Ru', '居家好物', 'MERCHANT', 'ACTIVE', '13800138005', 'merchant5@test.com', 0),
('merchant6', '$2a$10$vCxO.jCsVNC8FLycmsxpq.lTY25vCWNgPl3MaDD.gJKjWN72de1aW', '运动达人', 'MERCHANT', 'ACTIVE', '13800138006', 'merchant6@test.com', 0),
('merchant7', '$2a$10$zP.bfYapZZCb7MJjG6Qr5OeYR/Jzs8151IbI6bJaZaCz6zbX2POhK', '书虫书屋', 'MERCHANT', 'ACTIVE', '13800138007', 'merchant7@test.com', 0),
('merchant8', '$2a$10$XG/dwyF3nyAjKF1.tXPb.Ojn9.9eQ2O3XN7QsrkfAt70j2ZiCTwwK', '宝贝乐园', 'MERCHANT', 'ACTIVE', '13800138008', 'merchant8@test.com', 0);

-- 测试普通用户 (密码各不相同)
INSERT INTO `t_user` (`username`, `password`, `nickname`, `role`, `status`, `phone`, `email`, `points`) VALUES
('user1', '$2a$10$4h91VahjMi/2.WJgf/oahOfZ7hFuHbiOdwofu9P/DeKLnImJ/QvmS', '测试用户', 'USER', 'ACTIVE', '13900139001', 'user1@test.com', 100),
('user2', '$2a$10$DTbmtBHH3NgOFo0.LMrWPevSejyO068I4oClVweVBidjjoEzIJbf2', '购物达人', 'USER', 'ACTIVE', '13900139002', 'user2@test.com', 200);

-- 默认角色
INSERT INTO `t_sys_role` (`role_code`, `role_name`, `description`, `status`) VALUES
('SUPER_ADMIN', '超级管理员', '拥有所有权限', 'ACTIVE'),
('ADMIN', '平台管理员', '拥有管理后台权限', 'ACTIVE'),
('MERCHANT', '商家', '拥有商家端权限', 'ACTIVE'),
('USER', '普通用户', '拥有用户端权限', 'ACTIVE');

-- 默认商品分类（一级）
INSERT INTO `t_category` (`parent_id`, `name`, `sort_order`, `level`, `status`) VALUES
(0, '服装', 1, 1, 'ACTIVE'),
(0, '数码', 2, 1, 'ACTIVE'),
(0, '美妆', 3, 1, 'ACTIVE'),
(0, '食品', 4, 1, 'ACTIVE'),
(0, '家居', 5, 1, 'ACTIVE'),
(0, '运动', 6, 1, 'ACTIVE'),
(0, '图书', 7, 1, 'ACTIVE'),
(0, '母婴', 8, 1, 'ACTIVE');

-- 二级子分类
INSERT INTO `t_category` (`parent_id`, `name`, `icon`, `sort_order`, `level`, `status`) VALUES
-- 数码电子
(2, '手机', 'Phone', 1, 2, 'ACTIVE'),
(2, '电脑', 'Computer', 2, 2, 'ACTIVE'),
(2, '耳机音响', 'Headset', 3, 2, 'ACTIVE'),
-- 美妆护肤
(3, '护肤', 'SkinCare', 1, 2, 'ACTIVE'),
(3, '彩妆', 'Makeup', 2, 2, 'ACTIVE'),
(3, '个护', 'PersonalCare', 3, 2, 'ACTIVE'),
-- 服装
(1, '男装', 'Tshirt', 1, 2, 'ACTIVE'),
(1, '女装', 'Dress', 2, 2, 'ACTIVE'),
(1, '鞋靴', 'Shoes', 3, 2, 'ACTIVE'),
-- 食品
(4, '零食', 'Snack', 1, 2, 'ACTIVE'),
(4, '饮料', 'Drink', 2, 2, 'ACTIVE'),
(4, '生鲜', 'Fresh', 3, 2, 'ACTIVE'),
-- 家居
(5, '家纺', 'Textile', 1, 2, 'ACTIVE'),
(5, '厨具', 'Kitchen', 2, 2, 'ACTIVE'),
(5, '收纳', 'Storage', 3, 2, 'ACTIVE'),
-- 运动
(6, '运动鞋', 'Sneaker', 1, 2, 'ACTIVE'),
(6, '运动服', 'Sportswear', 2, 2, 'ACTIVE'),
(6, '健身器材', 'Fitness', 3, 2, 'ACTIVE'),
-- 图书
(7, '文学', 'Book', 1, 2, 'ACTIVE'),
(7, '科技', 'Tech', 2, 2, 'ACTIVE'),
(7, '教育', 'Edu', 3, 2, 'ACTIVE'),
-- 母婴
(8, '奶粉', 'Milk', 1, 2, 'ACTIVE'),
(8, '纸尿裤', 'Diaper', 2, 2, 'ACTIVE'),
(8, '玩具', 'Toy', 3, 2, 'ACTIVE');

-- 默认 Banner
INSERT INTO `t_banner` (`title`, `image_url`, `link_url`, `sort_order`, `status`, `start_time`, `end_time`) VALUES
('欢迎来到模拟商城', '', '/', 1, 'ACTIVE', NOW(), DATE_ADD(NOW(), INTERVAL 365 DAY)),
('新品上市 限时优惠', '', '/category/2', 2, 'ACTIVE', NOW(), DATE_ADD(NOW(), INTERVAL 365 DAY));

-- 商家
INSERT INTO `t_merchant` (`user_id`, `merchant_name`, `contact_phone`, `contact_email`, `status`, `approved_at`, `approved_by`) VALUES
(2, '模拟商城旗舰店', '13800138001', 'merchant1@test.com', 'ACTIVE', NOW(), 1),
(3, '花漾美妆专营店', '13800138002', 'merchant2@test.com', 'ACTIVE', NOW(), 1),
(4, '潮流服饰旗舰店', '13800138003', 'merchant3@test.com', 'ACTIVE', NOW(), 1),
(5, '美食天下旗舰店', '13800138004', 'merchant4@test.com', 'ACTIVE', NOW(), 1),
(6, '居家好物旗舰店', '13800138005', 'merchant5@test.com', 'ACTIVE', NOW(), 1),
(7, '运动达人旗舰店', '13800138006', 'merchant6@test.com', 'ACTIVE', NOW(), 1),
(8, '书虫书屋', '13800138007', 'merchant7@test.com', 'ACTIVE', NOW(), 1),
(9, '宝贝乐园', '13800138008', 'merchant8@test.com', 'ACTIVE', NOW(), 1);

-- 店铺
INSERT INTO `t_shop` (`merchant_id`, `shop_name`, `shop_logo`, `description`, `status`) VALUES
(1, '模拟商城旗舰店', '', '模拟商城官方自营店铺，全品类商品，品质保障', 'ACTIVE'),
(2, '花漾美妆专营店', '', '正品美妆，呵护你的美丽', 'ACTIVE'),
(3, '潮流服饰旗舰店', '', '时尚穿搭，品质生活', 'ACTIVE'),
(4, '美食天下旗舰店', '', '精选美味，产地直供', 'ACTIVE'),
(5, '居家好物旗舰店', '', '打造温馨舒适的家', 'ACTIVE'),
(6, '运动达人旗舰店', '', '专业运动装备，释放你的活力', 'ACTIVE'),
(7, '书虫书屋', '', '百万图书，正版保障', 'ACTIVE'),
(8, '宝贝乐园', '', '用心呵护每一个宝贝', 'ACTIVE');

-- 品牌
INSERT INTO `t_brand` (`brand_name`, `brand_logo`, `brand_description`, `status`) VALUES
('Apple', '', '全球领先的科技品牌', 'ACTIVE'),
('华为', '', '中国科技领军品牌', 'ACTIVE'),
('雅诗兰黛', '', '高端护肤彩妆品牌', 'ACTIVE'),
('兰蔻', '', '法国奢华美妆品牌', 'ACTIVE'),
('小米', '', '让每个人都能享受科技的乐趣', 'ACTIVE'),
('SK-II', '', '日本高端护肤品牌', 'ACTIVE'),
('优衣库', '', '高品质基本款', 'ACTIVE'),
('Nike', '', '全球运动品牌', 'ACTIVE'),
('ZARA', '', '快时尚品牌', 'ACTIVE'),
('三只松鼠', '', '互联网零食品牌', 'ACTIVE'),
('农夫山泉', '', '天然饮用水品牌', 'ACTIVE'),
('无印良品', '', '简约生活方式品牌', 'ACTIVE'),
('宜家', '', '瑞典家居品牌', 'ACTIVE'),
('Adidas', '', '德国运动品牌', 'ACTIVE'),
('李宁', '', '中国运动品牌', 'ACTIVE'),
('人民文学出版社', '', '经典文学出版品牌', 'ACTIVE'),
('机械工业出版社', '', '专业科技图书出版', 'ACTIVE'),
('飞鹤', '', '中国奶粉品牌', 'ACTIVE'),
('乐高', '', '丹麦积木玩具品牌', 'ACTIVE');

-- 默认字典类型
INSERT INTO `t_sys_dict_type` (`dict_name`, `dict_code`, `status`) VALUES
('订单状态', 'ORDER_STATUS', 'ACTIVE'),
('支付方式', 'PAYMENT_METHOD', 'ACTIVE'),
('商品状态', 'PRODUCT_STATUS', 'ACTIVE'),
('用户角色', 'USER_ROLE', 'ACTIVE');

-- 默认系统配置
INSERT INTO `t_sys_config` (`config_key`, `config_value`, `config_name`, `config_type`, `module`, `is_system`) VALUES
('site.name', '模拟商城', '站点名称', 'string', 'system', 1),
('mock.payment.delay.min', '1000', '模拟支付最小延迟(ms)', 'number', 'mock', 1),
('mock.payment.delay.max', '3000', '模拟支付最大延迟(ms)', 'number', 'mock', 1),
('mock.logistics.interval', '3600', '物流节点推进间隔(秒)', 'number', 'mock', 1),
('recommend.home.size', '10', '首页推荐数量', 'number', 'ai', 1),
('search.hot.size', '10', '热门搜索数量', 'number', 'ai', 1),
('file.upload.max.image', '5242880', '图片上传最大值(字节)', 'number', 'file', 1);

-- 管理员账户
INSERT INTO `t_sys_admin` (`user_id`, `admin_name`, `role`) VALUES
(1, '超级管理员', 'SUPER_ADMIN');

-- 默认字典项
INSERT INTO `t_sys_dict_item` (`dict_type_id`, `label`, `value`, `sort_order`, `status`) VALUES
-- 订单状态字典
(1, '待支付', 'CREATED', 1, 'ACTIVE'),
(1, '已支付', 'PAID', 2, 'ACTIVE'),
(1, '已发货', 'SHIPPED', 3, 'ACTIVE'),
(1, '运输中', 'IN_TRANSIT', 4, 'ACTIVE'),
(1, '已签收', 'DELIVERED', 5, 'ACTIVE'),
(1, '已完成', 'COMPLETED', 6, 'ACTIVE'),
(1, '已取消', 'CANCELLED', 7, 'ACTIVE'),
-- 支付方式字典
(2, '模拟支付宝', 'MOCK_ALIPAY', 1, 'ACTIVE'),
(2, '模拟微信支付', 'MOCK_WECHAT', 2, 'ACTIVE'),
-- 商品状态字典
(3, '草稿', 'DRAFT', 1, 'ACTIVE'),
(3, '已发布', 'PUBLISHED', 2, 'ACTIVE'),
(3, '已下架', 'OFFLINE', 3, 'ACTIVE'),
(3, '已删除', 'DELETED', 4, 'ACTIVE'),
-- 用户角色字典
(4, '管理员', 'ADMIN', 1, 'ACTIVE'),
(4, '商家', 'MERCHANT', 2, 'ACTIVE'),
(4, '普通用户', 'USER', 3, 'ACTIVE');

-- ============================================================
