# DB-001 ShoppingDream 数据库设计文档

> **Version**: v1.0.0 | **Status**: Draft | **Date**: 2026-07-03
> **SSOT**: 本文为 ShoppingDream 数据库结构的唯一权威来源。

---

## 目录

- [01 设计规范](#01-设计规范)
- [02 ER 关系总览](#02-er-关系总览)
- [03 表结构详解](#03-表结构详解)
- [04 索引设计](#04-索引设计)
- [05 初始化数据](#05-初始化数据)

---

## 01 设计规范

### 1.1 通用字段

所有业务表包含以下通用字段：

| 字段名 | 类型 | 说明 |
|---|---|---|
| id | BIGINT UNSIGNED AUTO_INCREMENT | 主键 |
| created_at | DATETIME DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 更新时间 |
| created_by | BIGINT | 创建人 ID |
| updated_by | BIGINT | 更新人 ID |
| deleted | TINYINT DEFAULT 0 | 软删除标记 (0=正常, 1=已删除) |

### 1.2 命名规范

| 规则 | 示例 |
|---|---|
| 业务表 `t_` 前缀 | `t_user`, `t_order` |
| 关联表 `t_rel_` 前缀 | `t_rel_product_sku` |
| 系统表 `t_sys_` 前缀 | `t_sys_menu`, `t_sys_dict` |
| 字段 snake_case | `order_no`, `user_id` |
| 枚举状态 VARCHAR | `status VARCHAR(20) DEFAULT 'CREATED'` |
| 金额 DECIMAL(10,2) | `price DECIMAL(10,2)` |
| 布尔 TINYINT | `is_default TINYINT DEFAULT 0` |

---

## 02 ER 关系总览

```
t_user ──1:1── t_user_address
t_user ──1:1── t_merchant ──1:1── t_shop
t_shop ──1:N── t_product ──1:N── t_product_image
t_product ──1:N── t_product_sku
t_product ──N:1── t_category (多级分类)
t_product ──N:1── t_brand (品牌)
t_user ──1:1── t_shopping_cart ──1:N── t_cart_item
t_user ──1:N── t_order ──1:N── t_order_item
t_order ──1:1── t_payment
t_order ──1:1── t_shipment ──1:1── t_logistics_record ──1:N── t_logistics_track
t_order ──1:N── t_review ──1:N── t_review_image
t_user ──1:N── t_sign_in_record
t_user ──N:M── t_product (收藏 → t_favorite)
t_user ──N:M── t_product (浏览历史 → t_browse_history)
t_user ──1:N── t_search_history (搜索历史)
t_sys_role ──N:M── t_sys_permission (→ t_sys_role_permission)
t_sys_role ──N:M── t_sys_menu (→ t_sys_role_menu)
t_sys_notification → t_user (站内消息)
```

---

## 03 表结构详解

### 3.1 用户领域 (User Domain)

#### t_user — 用户表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| username | VARCHAR(50) | NO | | 用户名（唯一） |
| password | VARCHAR(100) | NO | | 密码（BCrypt 加密） |
| nickname | VARCHAR(50) | YES | | 昵称 |
| avatar | VARCHAR(255) | YES | | 头像 URL |
| gender | TINYINT | YES | 0 | 性别 (0=未知, 1=男, 2=女) |
| phone | VARCHAR(20) | YES | | 手机号 |
| email | VARCHAR(100) | YES | | 邮箱 |
| role | VARCHAR(20) | NO | 'USER' | 角色 (USER/MERCHANT/ADMIN) |
| status | VARCHAR(20) | NO | 'ACTIVE' | 状态 (ACTIVE/DISABLED) |
| points | INT | NO | 0 | 积分 |
| + 通用字段 | | | | |

**索引**：
- UNIQUE `uk_username` (`username`)
- INDEX `idx_phone` (`phone`)
- INDEX `idx_status` (`status`)

#### t_user_address — 用户收货地址表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| user_id | BIGINT UNSIGNED | NO | | 用户 ID |
| receiver_name | VARCHAR(50) | NO | | 收货人姓名 |
| receiver_phone | VARCHAR(20) | NO | | 收货人电话 |
| province | VARCHAR(50) | NO | | 省 |
| city | VARCHAR(50) | NO | | 市 |
| district | VARCHAR(50) | NO | | 区 |
| detail_address | VARCHAR(255) | NO | | 详细地址 |
| is_default | TINYINT | NO | 0 | 是否默认 (0=否, 1=是) |
| + 通用字段 | | | | |

**索引**：
- INDEX `idx_user_id` (`user_id`)

---

### 3.2 商家领域 (Merchant Domain)

#### t_merchant — 商家表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| user_id | BIGINT UNSIGNED | NO | | 关联用户 ID |
| merchant_name | VARCHAR(100) | NO | | 商家名称 |
| contact_phone | VARCHAR(20) | NO | | 联系电话 |
| contact_email | VARCHAR(100) | YES | | 联系邮箱 |
| business_license | VARCHAR(255) | YES | | 营业执照 URL |
| status | VARCHAR(20) | NO | 'PENDING' | 状态 (PENDING/APPROVED/ACTIVE/DISABLED/REJECTED) |
| approved_at | DATETIME | YES | | 审核通过时间 |
| approved_by | BIGINT | YES | | 审核人 ID |
| + 通用字段 | | | | |

**索引**：
- UNIQUE `uk_user_id` (`user_id`)
- INDEX `idx_status` (`status`)

---

### 3.3 店铺领域 (Shop Domain)

#### t_shop — 店铺表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| merchant_id | BIGINT UNSIGNED | NO | | 商家 ID |
| shop_name | VARCHAR(100) | NO | | 店铺名称 |
| shop_logo | VARCHAR(255) | YES | | 店铺 Logo URL |
| description | TEXT | YES | | 店铺描述 |
| status | VARCHAR(20) | NO | 'ACTIVE' | 状态 (ACTIVE/DISABLED) |
| + 通用字段 | | | | |

**索引**：
- UNIQUE `uk_merchant_id` (`merchant_id`)
- INDEX `idx_status` (`status`)

---

### 3.4 商品领域 (Product Domain)

#### t_category — 商品分类表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| parent_id | BIGINT UNSIGNED | NO | 0 | 父分类 ID (0=根分类) |
| name | VARCHAR(50) | NO | | 分类名称 |
| icon | VARCHAR(255) | YES | | 分类图标 URL |
| sort_order | INT | NO | 0 | 排序值 |
| status | VARCHAR(20) | NO | 'ACTIVE' | 状态 (ACTIVE/DISABLED) |
| level | TINYINT | NO | 1 | 层级 (1=一级, 2=二级) |
| + 通用字段 | | | | |

**索引**：
- INDEX `idx_parent_id` (`parent_id`)
- INDEX `idx_level` (`level`)

#### t_brand — 品牌表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| brand_name | VARCHAR(100) | NO | | 品牌名称 |
| brand_logo | VARCHAR(500) | YES | | 品牌 Logo URL |
| brand_description | TEXT | YES | | 品牌描述 |
| status | VARCHAR(20) | NO | 'ACTIVE' | 状态 (ACTIVE/DISABLED) |
| + 通用字段 | | | | |

**索引**：
- UNIQUE `uk_brand_name` (`brand_name`)
- INDEX `idx_status` (`status`)

#### t_product — 商品表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| shop_id | BIGINT UNSIGNED | NO | | 店铺 ID |
| category_id | BIGINT UNSIGNED | NO | | 分类 ID |
| brand_id | BIGINT UNSIGNED | YES | | 品牌 ID |
| name | VARCHAR(200) | NO | | 商品名称 |
| subtitle | VARCHAR(255) | YES | | 副标题 |
| description | TEXT | YES | | 商品描述（富文本） |
| main_image | VARCHAR(255) | YES | | 主图 URL |
| price | DECIMAL(10,2) | NO | | 售价 |
| original_price | DECIMAL(10,2) | YES | | 原价 |
| stock | INT | NO | 0 | 库存 |
| sales | INT | NO | 0 | 销量 |
| view_count | INT | NO | 0 | 浏览量 |
| favorite_count | INT | NO | 0 | 收藏数 |
| review_count | INT | NO | 0 | 评价数 |
| avg_review_score | DECIMAL(2,1) | NO | 0.0 | 平均评分 |
| is_recommended | TINYINT | NO | 0 | 是否推荐 |
| is_new | TINYINT | NO | 0 | 是否新品 |
| publish_time | DATETIME | YES | | 发布时间 |
| status | VARCHAR(20) | NO | 'DRAFT' | 状态 (DRAFT/PUBLISHED/OFFLINE/DELETED) |
| + 通用字段 | | | | |

**索引**：
- INDEX `idx_shop_id` (`shop_id`)
- INDEX `idx_category_id` (`category_id`)
- INDEX `idx_brand_id` (`brand_id`)
- INDEX `idx_status` (`status`)
- INDEX `idx_is_recommended` (`is_recommended`)
- INDEX `idx_sales` (`sales`)
- INDEX `idx_created_at` (`created_at`)

#### t_product_image — 商品图片表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| product_id | BIGINT UNSIGNED | NO | | 商品 ID |
| image_url | VARCHAR(500) | NO | | 图片 URL |
| image_type | TINYINT | NO | 1 | 类型 (1=主图, 2=详情图) |
| sort_order | INT | NO | 0 | 排序值 |
| + 通用字段 | | | | |

**索引**：
- INDEX `idx_product_id` (`product_id`)

#### t_product_sku — 商品规格表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| product_id | BIGINT UNSIGNED | NO | | 商品 ID |
| sku_name | VARCHAR(200) | NO | | SKU 名称（如"红色 XL"） |
| sku_code | VARCHAR(100) | YES | | SKU 编码 |
| price | DECIMAL(10,2) | NO | | 规格价格 |
| stock | INT | NO | 0 | 规格库存 |
| attributes | JSON | YES | | SKU 属性 JSON，如 {"颜色":"红","尺寸":"L"} |
| image_url | VARCHAR(500) | YES | | SKU 图片 URL |
| + 通用字段 | | | | |

**索引**：
- INDEX `idx_product_id` (`product_id`)
- UNIQUE `uk_sku_code` (`sku_code`)

#### t_favorite — 收藏表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| user_id | BIGINT UNSIGNED | NO | | 用户 ID |
| product_id | BIGINT UNSIGNED | NO | | 商品 ID |
| + 通用字段 | | | | |

**索引**：
- UNIQUE `uk_user_product` (`user_id`, `product_id`)
- INDEX `idx_product_id` (`product_id`)

#### t_browse_history — 浏览历史表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| user_id | BIGINT UNSIGNED | NO | | 用户 ID |
| product_id | BIGINT UNSIGNED | NO | | 商品 ID |
| viewed_at | DATETIME | NO | CURRENT_TIMESTAMP | 浏览时间 |
| + 通用字段 | | | | |

**索引**：
- INDEX `idx_user_id_viewed_at` (`user_id`, `viewed_at`)

#### t_search_history — 搜索历史表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| user_id | BIGINT UNSIGNED | NO | | 用户 ID |
| keyword | VARCHAR(100) | NO | | 搜索关键词 |
| searched_at | DATETIME | NO | CURRENT_TIMESTAMP | 搜索时间 |
| + 通用字段 | | | | |

**索引**：
- INDEX `idx_user_id_searched_at` (`user_id`, `searched_at`)

---

### 3.5 购物车领域 (ShoppingCart Domain)

#### t_shopping_cart — 购物车表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| user_id | BIGINT UNSIGNED | NO | | 用户 ID |
| shop_id | BIGINT UNSIGNED | YES | | 店铺 ID（按店铺分组） |
| + 通用字段 | | | | |

**索引**：
- UNIQUE `uk_user_id` (`user_id`)

#### t_cart_item — 购物车项表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| cart_id | BIGINT UNSIGNED | NO | | 购物车 ID |
| product_id | BIGINT UNSIGNED | NO | | 商品 ID |
| shop_id | BIGINT UNSIGNED | NO | | 店铺 ID（用于按店铺分组） |
| quantity | INT | NO | 1 | 数量 |
| sku_id | BIGINT UNSIGNED | YES | | 规格 ID |
| selected | TINYINT | NO | 1 | 是否选中 (0=未选, 1=已选) |
| + 通用字段 | | | | |

**索引**：
- UNIQUE `uk_cart_product_sku` (`cart_id`, `product_id`, `sku_id`)
- INDEX `idx_cart_id` (`cart_id`)

---

### 3.6 订单领域 (Order Domain)

#### t_order — 订单表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| order_no | VARCHAR(32) | NO | | 订单号 (SD+yyyyMMddHHmmss+4位随机) |
| user_id | BIGINT UNSIGNED | NO | | 买家 ID |
| shop_id | BIGINT UNSIGNED | NO | | 店铺 ID |
| total_amount | DECIMAL(10,2) | NO | | 商品总金额 |
| shipping_fee | DECIMAL(10,2) | NO | 0.00 | 运费 |
| discount_amount | DECIMAL(10,2) | NO | 0.00 | 优惠金额 |
| pay_amount | DECIMAL(10,2) | NO | | 实付金额 |
| status | VARCHAR(20) | NO | 'CREATED' | 状态 (CREATED/PAID/SHIPPED/IN_TRANSIT/DELIVERED/COMPLETED/CANCELLED) |
| receiver_name | VARCHAR(50) | NO | | 收货人 |
| receiver_phone | VARCHAR(20) | NO | | 收货电话 |
| receiver_address | VARCHAR(500) | NO | | 收货地址（快照） |
| remark | VARCHAR(255) | YES | | 订单备注 |
| paid_at | DATETIME | YES | | 支付时间 |
| shipped_at | DATETIME | YES | | 发货时间 |
| delivered_at | DATETIME | YES | | 签收时间 |
| completed_at | DATETIME | YES | | 完成时间 |
| cancelled_at | DATETIME | YES | | 取消时间 |
| auto_confirm | TINYINT | NO | 0 | 是否自动确认 (0=否, 1=是) |
| + 通用字段 | | | | |

**索引**：
- UNIQUE `uk_order_no` (`order_no`)
- INDEX `idx_user_id_status` (`user_id`, `status`)
- INDEX `idx_shop_id_status` (`shop_id`, `status`)
- INDEX `idx_status` (`status`)
- INDEX `idx_created_at` (`created_at`)

#### t_order_item — 订单项表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| order_id | BIGINT UNSIGNED | NO | | 订单 ID |
| product_id | BIGINT UNSIGNED | NO | | 商品 ID |
| product_name | VARCHAR(200) | NO | | 商品名称（快照） |
| product_image | VARCHAR(255) | YES | | 商品图片（快照） |
| sku_id | BIGINT UNSIGNED | YES | | 规格 ID |
| sku_name | VARCHAR(100) | YES | | 规格名称（快照） |
| price | DECIMAL(10,2) | NO | | 单价（快照） |
| quantity | INT | NO | | 数量 |
| subtotal | DECIMAL(10,2) | NO | | 小计 |
| + 通用字段 | | | | |

**索引**：
- INDEX `idx_order_id` (`order_id`)

---

### 3.7 支付领域 (Payment Domain)

#### t_payment — 支付记录表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| order_id | BIGINT UNSIGNED | NO | | 订单 ID |
| order_no | VARCHAR(32) | NO | | 订单号 |
| payment_no | VARCHAR(64) | NO | | 支付流水号 |
| payment_method | VARCHAR(20) | NO | | 支付方式 (MOCK_ALIPAY/MOCK_WECHAT) |
| amount | DECIMAL(10,2) | NO | | 支付金额 |
| status | VARCHAR(20) | NO | 'PENDING' | 状态 (PENDING/SUCCESS/FAILED/CLOSED) |
| paid_at | DATETIME | YES | | 支付成功时间 |
| + 通用字段 | | | | |

**索引**：
- UNIQUE `uk_order_id` (`order_id`)
- INDEX `idx_payment_no` (`payment_no`)
- INDEX `idx_status` (`status`)

---

### 3.8 发货领域 (Shipment Domain)

#### t_shipment — 发货单表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| order_id | BIGINT UNSIGNED | NO | | 订单 ID |
| order_no | VARCHAR(32) | NO | | 订单号 |
| shop_id | BIGINT UNSIGNED | NO | | 店铺 ID |
| shipment_no | VARCHAR(64) | NO | | 发货单号 |
| logistics_company | VARCHAR(50) | YES | | 物流公司 |
| status | VARCHAR(20) | NO | 'PENDING' | 状态 (PENDING/PACKED/SHIPPED) |
| shipped_at | DATETIME | YES | | 发货时间 |
| + 通用字段 | | | | |

**索引**：
- UNIQUE `uk_order_id` (`order_id`)
- INDEX `idx_shop_id` (`shop_id`)

---

### 3.9 物流领域 (Logistics Domain)

#### t_logistics_record — 物流记录表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| shipment_id | BIGINT UNSIGNED | NO | | 发货单 ID |
| order_id | BIGINT UNSIGNED | NO | | 订单 ID |
| order_no | VARCHAR(32) | NO | | 订单号 |
| tracking_no | VARCHAR(64) | NO | | 物流单号 |
| logistics_company | VARCHAR(50) | YES | | 物流公司 |
| status | VARCHAR(20) | NO | 'CREATED' | 状态 (CREATED/PICKED_UP/SORTING/IN_TRANSIT/OUT_FOR_DELIVERY/DELIVERED) |
| delivered_at | DATETIME | YES | | 签收时间 |
| + 通用字段 | | | | |

**索引**：
- UNIQUE `uk_shipment_id` (`shipment_id`)
- INDEX `idx_order_id` (`order_id`)
- INDEX `idx_tracking_no` (`tracking_no`)
- INDEX `idx_status` (`status`)

#### t_logistics_track — 物流轨迹表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| logistics_id | BIGINT UNSIGNED | NO | | 物流记录 ID |
| status | VARCHAR(20) | NO | | 轨迹状态 |
| description | VARCHAR(255) | YES | | 轨迹描述 |
| location | VARCHAR(100) | YES | | 所在地 |
| occurred_at | DATETIME | NO | | 发生时间 |
| + 通用字段 | | | | |

**索引**：
- INDEX `idx_logistics_id` (`logistics_id`)

---

### 3.10 评价领域 (Review Domain)

#### t_review — 评价表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| order_id | BIGINT UNSIGNED | NO | | 订单 ID |
| order_no | VARCHAR(32) | NO | | 订单号 |
| product_id | BIGINT UNSIGNED | NO | | 商品 ID |
| user_id | BIGINT UNSIGNED | NO | | 评价人 ID |
| shop_id | BIGINT UNSIGNED | NO | | 店铺 ID |
| rating | TINYINT | NO | 5 | 评分 (1-5) |
| content | TEXT | YES | | 评价内容 |
| status | VARCHAR(20) | NO | 'VISIBLE' | 状态 (CREATED/VISIBLE/HIDDEN/DELETED) |
| merchant_reply | TEXT | YES | | 商家回复 |
| merchant_replied_at | DATETIME | YES | | 商家回复时间 |
| + 通用字段 | | | | |

**索引**：
- INDEX `idx_product_id_status` (`product_id`, `status`)
- INDEX `idx_user_id` (`user_id`)
- INDEX `idx_shop_id` (`shop_id`)
- INDEX `idx_order_id` (`order_id`)

#### t_review_image — 评价图片表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| review_id | BIGINT UNSIGNED | NO | | 评价 ID |
| image_url | VARCHAR(255) | NO | | 图片 URL |
| sort_order | INT | NO | 0 | 排序 |
| + 通用字段 | | | | |

**索引**：
- INDEX `idx_review_id` (`review_id`)

---

### 3.11 营销领域 (Marketing Domain)

#### t_sign_in_record — 签到记录表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| user_id | BIGINT UNSIGNED | NO | | 用户 ID |
| sign_date | DATE | NO | | 签到日期 |
| consecutive_days | INT | NO | 1 | 连续签到天数 |
| points_earned | INT | NO | 1 | 获得积分 |
| + 通用字段 | | | | |

**索引**：
- UNIQUE `uk_user_date` (`user_id`, `sign_date`)
- INDEX `idx_sign_date` (`sign_date`)

#### t_banner — 首页 Banner 表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| title | VARCHAR(100) | NO | | 标题 |
| image_url | VARCHAR(255) | NO | | 图片 URL |
| link_url | VARCHAR(255) | YES | | 跳转链接 |
| sort_order | INT | NO | 0 | 排序 |
| status | VARCHAR(20) | NO | 'ACTIVE' | 状态 (ACTIVE/INACTIVE) |
| start_time | DATETIME | YES | | 展示开始时间 |
| end_time | DATETIME | YES | | 展示结束时间 |
| + 通用字段 | | | | |

**索引**：
- INDEX `idx_status_sort` (`status`, `sort_order`)

---

### 3.12 系统领域 (System Domain)

#### t_sys_admin — 管理员表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| user_id | BIGINT UNSIGNED | NO | | 关联用户 ID |
| admin_name | VARCHAR(50) | NO | | 管理员名称 |
| role | VARCHAR(20) | NO | 'ADMIN' | 角色 (ADMIN/SUPER_ADMIN) |
| + 通用字段 | | | | |

**索引**：
- UNIQUE `uk_user_id` (`user_id`)

#### t_sys_role — 角色表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| role_code | VARCHAR(50) | NO | | 角色编码 (SUPER_ADMIN/ADMIN/MERCHANT/USER) |
| role_name | VARCHAR(50) | NO | | 角色名称 |
| description | VARCHAR(255) | YES | | 描述 |
| status | VARCHAR(20) | NO | 'ACTIVE' | 状态 (ACTIVE/DISABLED) |
| + 通用字段 | | | | |

**索引**：
- UNIQUE `uk_role_code` (`role_code`)

#### t_sys_permission — 权限表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| permission_code | VARCHAR(100) | NO | | 权限编码 |
| permission_name | VARCHAR(100) | NO | | 权限名称 |
| permission_type | TINYINT | NO | | 类型 (1=菜单, 2=按钮, 3=接口) |
| module | VARCHAR(50) | NO | | 所属模块 |
| description | VARCHAR(255) | YES | | 描述 |
| + 通用字段 | | | | |

**索引**：
- UNIQUE `uk_permission_code` (`permission_code`)

#### t_sys_role_permission — 角色权限关联表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| role_id | BIGINT UNSIGNED | NO | | 角色 ID |
| permission_id | BIGINT UNSIGNED | NO | | 权限 ID |
| + 通用字段 | | | | |

**索引**：
- UNIQUE `uk_role_permission` (`role_id`, `permission_id`)

#### t_sys_role_menu — 角色菜单关联表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| role_id | BIGINT UNSIGNED | NO | | 角色 ID |
| menu_id | BIGINT UNSIGNED | NO | | 菜单 ID |
| + 通用字段 | | | | |

**索引**：
- UNIQUE `uk_role_menu` (`role_id`, `menu_id`)

#### t_sys_config — 系统配置表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| config_key | VARCHAR(100) | NO | | 配置键 |
| config_value | TEXT | NO | | 配置值 |
| config_name | VARCHAR(100) | NO | | 配置名称 |
| config_type | VARCHAR(50) | NO | | 配置类型 (string/number/boolean/json) |
| module | VARCHAR(50) | NO | | 所属模块 |
| description | VARCHAR(255) | YES | | 描述 |
| is_system | TINYINT | NO | 0 | 是否系统内置 (0=否, 1=是) |
| + 通用字段 | | | | |

**索引**：
- UNIQUE `uk_config_key` (`config_key`)

#### t_sys_menu — 菜单表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| parent_id | BIGINT UNSIGNED | NO | 0 | 父菜单 ID |
| name | VARCHAR(50) | NO | | 菜单名称 |
| path | VARCHAR(200) | YES | | 路由路径 |
| component | VARCHAR(200) | YES | | 组件路径 |
| icon | VARCHAR(50) | YES | | 图标 |
| sort_order | INT | NO | 0 | 排序 |
| type | VARCHAR(20) | NO | 'MENU' | 类型 (MENU/BUTTON) |
| permission | VARCHAR(100) | YES | | 权限标识 |
| visible | TINYINT | NO | 1 | 是否可见 |
| + 通用字段 | | | | |

**索引**：
- INDEX `idx_parent_id` (`parent_id`)

#### t_sys_dict_type — 字典类型表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| dict_name | VARCHAR(50) | NO | | 字典名称 |
| dict_code | VARCHAR(50) | NO | | 字典编码 |
| status | VARCHAR(20) | NO | 'ACTIVE' | 状态 |
| remark | VARCHAR(255) | YES | | 备注 |
| + 通用字段 | | | | |

**索引**：
- UNIQUE `uk_dict_code` (`dict_code`)

#### t_sys_dict_item — 字典项表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| dict_type_id | BIGINT UNSIGNED | NO | | 字典类型 ID |
| label | VARCHAR(100) | NO | | 标签 |
| value | VARCHAR(100) | NO | | 值 |
| sort_order | INT | NO | 0 | 排序 |
| status | VARCHAR(20) | NO | 'ACTIVE' | 状态 |
| + 通用字段 | | | | |

**索引**：
- INDEX `idx_dict_type_id` (`dict_type_id`)

#### t_sys_operation_log — 操作日志表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| operator_id | BIGINT UNSIGNED | NO | | 操作人 ID |
| operator_name | VARCHAR(50) | NO | | 操作人名称 |
| operator_type | VARCHAR(20) | YES | | 操作人类型 (USER/MERCHANT/ADMIN) |
| module | VARCHAR(50) | NO | | 操作模块 |
| operation_type | VARCHAR(50) | NO | | 操作类型 |
| method | VARCHAR(200) | YES | | 请求方法 |
| request_url | VARCHAR(500) | YES | | 请求 URL |
| request_params | TEXT | YES | | 请求参数 |
| response_result | TEXT | YES | | 响应结果 |
| ip | VARCHAR(50) | YES | | IP 地址 |
| cost_time | BIGINT | YES | | 耗时(ms) |
| + 通用字段 | | | | |

**索引**：
- INDEX `idx_operator_id` (`operator_id`)
- INDEX `idx_module` (`module`)
- INDEX `idx_created_at` (`created_at`)

#### t_sys_login_log — 登录日志表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| user_id | BIGINT UNSIGNED | YES | | 用户 ID |
| user_type | VARCHAR(20) | NO | | 用户类型 (USER/MERCHANT/ADMIN) |
| username | VARCHAR(50) | NO | | 用户名 |
| login_type | VARCHAR(20) | NO | | 类型 (LOGIN/LOGOUT/REGISTER) |
| status | TINYINT | NO | 1 | 状态 (0=失败, 1=成功) |
| ip | VARCHAR(50) | YES | | IP 地址 |
| user_agent | VARCHAR(500) | YES | | User-Agent |
| error_msg | VARCHAR(255) | YES | | 错误信息 |
| + 通用字段 | | | | |

**索引**：
- INDEX `idx_user_id_type` (`user_id`, `user_type`)
- INDEX `idx_created_at` (`created_at`)

#### t_sys_notification — 站内消息表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| user_id | BIGINT UNSIGNED | NO | | 接收用户 ID |
| user_type | VARCHAR(20) | NO | | 用户类型 (USER/MERCHANT/ADMIN) |
| notification_type | VARCHAR(30) | NO | | 类型 (ORDER/PAYMENT/SHIPMENT/LOGISTICS/REVIEW/SYSTEM) |
| title | VARCHAR(100) | NO | | 标题 |
| content | TEXT | NO | | 内容 |
| reference_id | BIGINT | YES | | 关联 ID |
| reference_type | VARCHAR(30) | YES | | 关联类型 |
| is_read | TINYINT | NO | 0 | 是否已读 (0=未读, 1=已读) |
| read_at | DATETIME | YES | | 阅读时间 |
| + 通用字段 | | | | |

**索引**：
- INDEX `idx_user_type` (`user_id`, `user_type`)
- INDEX `idx_is_read` (`is_read`)
- INDEX `idx_created_at` (`created_at`)

#### t_sys_file — 文件表

| 字段 | 类型 | 空 | 默认 | 说明 |
|---|---|---|---|---|
| id | BIGINT UNSIGNED | NO | AUTO | 主键 |
| original_name | VARCHAR(255) | NO | | 原始文件名 |
| file_name | VARCHAR(255) | NO | | 存储文件名 |
| file_path | VARCHAR(500) | NO | | 文件路径 |
| file_url | VARCHAR(500) | NO | | 访问 URL |
| file_type | VARCHAR(50) | NO | | MIME 类型 |
| file_size | BIGINT | NO | | 文件大小(字节) |
| file_category | VARCHAR(20) | NO | | 分类 (PRODUCT/AVATAR/REVIEW/SHOP/SYSTEM) |
| uploader_id | BIGINT | YES | | 上传者 ID |
| uploader_type | VARCHAR(20) | YES | | 上传者类型 |
| + 通用字段 | | | | |

**索引**：
- INDEX `idx_uploader` (`uploader_id`, `uploader_type`)
- INDEX `idx_file_category` (`file_category`)

---

## 04 索引设计原则

1. **查询驱动建索引** — 根据 PRD 的查询场景建索引，不为不存在的查询建索引
2. **联合索引前缀匹配** — 高频过滤字段放前面（如 `user_id` + `status`）
3. **软删除不参与索引** — `deleted` 字段值分布极端，不建索引
4. **避免冗余索引** — 单列索引和联合索引重叠时，保留联合索引

---

## 05 初始化数据

### 5.1 默认管理员

```sql
-- 密码: admin123 (BCrypt 加密)
INSERT INTO t_user (username, password, nickname, role, status)
VALUES ('admin', '$2a$10$xxx', '超级管理员', 'ADMIN', 'ACTIVE');
```

### 5.2 默认角色

```sql
INSERT INTO t_sys_role (role_code, role_name, description, status) VALUES
('SUPER_ADMIN', '超级管理员', '拥有所有权限', 'ACTIVE'),
('ADMIN', '平台管理员', '拥有管理后台权限', 'ACTIVE'),
('MERCHANT', '商家', '拥有商家端权限', 'ACTIVE'),
('USER', '普通用户', '拥有用户端权限', 'ACTIVE');
```

### 5.3 默认商品分类

```sql
INSERT INTO t_category (parent_id, name, sort_order, level, status) VALUES
(0, '服装', 1, 1, 'ACTIVE'),
(0, '数码', 2, 1, 'ACTIVE'),
(0, '美妆', 3, 1, 'ACTIVE'),
(0, '食品', 4, 1, 'ACTIVE'),
(0, '家居', 5, 1, 'ACTIVE'),
(0, '运动', 6, 1, 'ACTIVE'),
(0, '图书', 7, 1, 'ACTIVE'),
(0, '母婴', 8, 1, 'ACTIVE');
-- 二级分类在 M2 阶段插入
```

### 5.4 默认 Banner

```sql
INSERT INTO t_banner (title, image_url, link_url, sort_order, status) VALUES
('欢迎来到 ShoppingDream', '/banner/welcome.jpg', '/', 1, 'ACTIVE'),
('新品上市', '/banner/new.jpg', '/category/1', 2, 'ACTIVE');
```

### 5.5 默认字典

```sql
INSERT INTO t_sys_dict_type (dict_name, dict_code, status) VALUES
('订单状态', 'ORDER_STATUS', 'ACTIVE'),
('支付方式', 'PAYMENT_METHOD', 'ACTIVE'),
('商品状态', 'PRODUCT_STATUS', 'ACTIVE'),
('用户角色', 'USER_ROLE', 'ACTIVE');
```

### 5.6 默认系统配置

```sql
INSERT INTO t_sys_config (config_key, config_value, config_name, config_type, module, is_system) VALUES
('site.name', 'ShoppingDream', '站点名称', 'string', 'system', 1),
('mock.payment.delay.min', '1000', '模拟支付最小延迟(ms)', 'number', 'mock', 1),
('mock.payment.delay.max', '3000', '模拟支付最大延迟(ms)', 'number', 'mock', 1),
('mock.logistics.interval', '3600', '物流节点推进间隔(秒)', 'number', 'mock', 1),
('recommend.home.size', '10', '首页推荐数量', 'number', 'ai', 1),
('search.hot.size', '10', '热门搜索数量', 'number', 'ai', 1),
('file.upload.max.image', '5242880', '图片上传最大值(字节)', 'number', 'file', 1);
```

---

## 附录：表清单

| # | 表名 | 所属领域 | 说明 |
|---|---|---|---|
| 1 | t_user | User | 用户 |
| 2 | t_user_address | User | 收货地址 |
| 3 | t_merchant | Merchant | 商家 |
| 4 | t_shop | Shop | 店铺 |
| 5 | t_category | Product | 商品分类 |
| 6 | t_brand | Product | 品牌 |
| 7 | t_product | Product | 商品 |
| 8 | t_product_image | Product | 商品图片 |
| 9 | t_product_sku | Product | 商品规格 |
| 10 | t_favorite | Product | 收藏 |
| 11 | t_browse_history | Product | 浏览历史 |
| 12 | t_search_history | Product | 搜索历史 |
| 13 | t_shopping_cart | Cart | 购物车 |
| 14 | t_cart_item | Cart | 购物车项 |
| 15 | t_order | Order | 订单 |
| 16 | t_order_item | Order | 订单项 |
| 17 | t_payment | Payment | 支付记录 |
| 18 | t_shipment | Shipment | 发货单 |
| 19 | t_logistics_record | Logistics | 物流记录 |
| 20 | t_logistics_track | Logistics | 物流轨迹 |
| 21 | t_review | Review | 评价 |
| 22 | t_review_image | Review | 评价图片 |
| 23 | t_sign_in_record | Marketing | 签到记录 |
| 24 | t_banner | Marketing | 首页 Banner |
| 25 | t_sys_admin | System | 管理员 |
| 26 | t_sys_role | System | 角色 |
| 27 | t_sys_permission | System | 权限 |
| 28 | t_sys_role_permission | System | 角色权限关联 |
| 29 | t_sys_role_menu | System | 角色菜单关联 |
| 30 | t_sys_menu | System | 菜单 |
| 31 | t_sys_dict_type | System | 字典类型 |
| 32 | t_sys_dict_item | System | 字典项 |
| 33 | t_sys_config | System | 系统配置 |
| 34 | t_sys_operation_log | System | 操作日志 |
| 35 | t_sys_login_log | System | 登录日志 |
| 36 | t_sys_notification | System | 站内消息 |
| 37 | t_sys_file | System | 文件 |

**总计 37 张表，覆盖 PRD 全部 12 个领域。**
