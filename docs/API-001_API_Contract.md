# API-001 ShoppingDream API 契约文档

> **Version**: v1.0.0 | **Status**: Draft | **Date**: 2026-07-03
> **SSOT**: 本文为 ShoppingDream 所有 RESTful API 的唯一权威定义。

---

## 目录

- [01 通用规范](#01-通用规范)
- [02 认证模块](#02-认证模块)
- [03 用户模块](#03-用户模块)
- [04 商家模块](#04-商家模块)
- [05 店铺模块](#05-店铺模块)
- [06 商品模块](#06-商品模块)
- [07 购物车模块](#07-购物车模块)
- [08 订单模块](#08-订单模块)
- [09 支付模块](#09-支付模块)
- [10 发货模块](#10-发货模块)
- [11 物流模块](#11-物流模块)
- [12 评价模块](#12-评价模块)
- [13 营销模块](#13-营销模块)
- [14 系统管理模块](#14-系统管理模块)
- [15 文件上传模块](#15-文件上传模块)
- [16 消息通知模块](#16-消息通知模块)
- [17 优惠券模块](#17-优惠券模块)
- [18 积分模块](#18-积分模块)
- [19 秒杀模块](#19-秒杀模块)
- [20 专题活动模块](#20-专题活动模块)
- [21 排行榜模块](#21-排行榜模块)

---

## 01 通用规范

### 1.1 Base URL

```
开发环境: http://localhost:8080/api
生产环境: http://your-domain/api
```

### 1.2 统一响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": 1751539200000
}
```

### 1.3 状态码

| code | 含义 |
|---|---|
| 200 | 成功 |
| 400 | 参数错误 |
| 401 | 未认证 |
| 403 | 无权限 |
| 404 | 资源不存在 |
| 409 | 状态冲突（如订单状态不允许操作） |
| 500 | 系统内部错误 |

### 1.4 认证方式

```
Header: Authorization: Bearer <JWT_TOKEN>
```

### 1.5 分页参数

| 参数 | 类型 | 默认 | 说明 |
|---|---|---|---|
| page | int | 1 | 页码 |
| size | int | 20 | 每页条数 |
| sort | string | created_at,desc | 排序 |

### 1.6 分页响应

```json
{
  "list": [],
  "total": 100,
  "page": 1,
  "size": 20,
  "totalPages": 5
}
```

---

## 02 认证模块

### POST /api/common/register — 用户注册

**权限**: 公开

**Request**:
```json
{
  "username": "string (4-20字符, 字母数字下划线)",
  "password": "string (6-20字符)",
  "nickname": "string (可选)"
}
```

**Response**:
```json
{
  "userId": 1,
  "username": "testuser",
  "nickname": "测试用户",
  "avatar": "/avatar/1.jpg",
  "role": "USER",
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "expiresIn": 86400,
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### POST /api/common/login — 用户登录

**权限**: 公开

**Request**:
```json
{
  "username": "string",
  "password": "string"
}
```

**Response**:
```json
{
  "userId": 1,
  "username": "testuser",
  "nickname": "测试用户",
  "avatar": "/avatar/1.jpg",
  "role": "USER",
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "expiresIn": 86400,
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### POST /api/common/logout — 退出登录

**权限**: 已登录

**Response**: 无 data

### POST /api/common/refresh-token — 刷新 Token

**权限**: 公开

**Request**:
```json
{
  "refreshToken": "string"
}
```

**Response**:
```json
{
  "userId": 1,
  "username": "testuser",
  "nickname": "测试用户",
  "avatar": "/avatar/1.jpg",
  "role": "USER",
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "expiresIn": 86400,
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### POST /api/admin/login — 管理员登录

**权限**: 公开

**Request**:
```json
{
  "username": "string",
  "password": "string"
}
```

**Response**:
```json
{
  "userId": 1,
  "username": "admin",
  "nickname": "超级管理员",
  "avatar": "/avatar/admin.jpg",
  "role": "SUPER_ADMIN",
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "expiresIn": 86400,
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
  "adminName": "超级管理员"
}
```

### GET /api/common/userinfo — 获取当前用户信息

**权限**: 已登录

**Response**:
```json
{
  "userId": 1,
  "username": "testuser",
  "nickname": "测试用户",
  "avatar": "/avatar/1.jpg",
  "gender": 0,
  "phone": "138****8888",
  "email": "test@example.com",
  "role": "USER",
  "points": 120,
  "merchantId": null
}
```

---

## 03 用户模块

### 3.1 个人信息

#### PUT /api/user/profile — 更新个人信息

**权限**: 用户登录

**Request**:
```json
{
  "nickname": "string",
  "avatar": "string (URL)",
  "gender": 0,
  "phone": "string",
  "email": "string"
}
```

#### PUT /api/user/password — 修改密码

**Request**:
```json
{
  "oldPassword": "string",
  "newPassword": "string"
}
```

### 3.2 收货地址

#### GET /api/user/addresses — 获取地址列表

**Response**:
```json
{
  "list": [
    {
      "id": 1,
      "receiverName": "张三",
      "receiverPhone": "13800138000",
      "province": "广东省",
      "city": "深圳市",
      "district": "南山区",
      "detailAddress": "科技园xx号",
      "isDefault": 1
    }
  ]
}
```

#### POST /api/user/addresses — 新增地址

**Request**:
```json
{
  "receiverName": "string",
  "receiverPhone": "string",
  "province": "string",
  "city": "string",
  "district": "string",
  "detailAddress": "string",
  "isDefault": 0
}
```

#### PUT /api/user/addresses/{addressId} — 编辑地址

**Request**: 同新增

#### DELETE /api/user/addresses/{addressId} — 删除地址

#### PUT /api/user/addresses/{addressId}/default — 设为默认地址

### 3.3 收藏

#### GET /api/user/favorites — 收藏列表（分页）

**Query**: `page`, `size`

**Response**: PageResponse<ProductCardVO>

#### POST /api/user/favorites/{productId} — 收藏商品

#### DELETE /api/user/favorites/{productId} — 取消收藏

### 3.4 浏览历史

#### GET /api/user/history — 浏览历史（分页）

#### DELETE /api/user/history — 清空浏览历史

### 3.5 搜索历史

#### GET /api/user/search-history — 搜索历史列表

**Response**:
```json
{
  "list": [
    {"id": 1, "keyword": "蓝牙耳机", "searchedAt": "2026-07-03T10:00:00"}
  ]
}
```

#### DELETE /api/user/search-history — 清空搜索历史

### 3.6 商家申请

#### POST /api/user/merchant-application — 提交商家入驻申请

**Request**:
```json
{
  "merchantName": "string",
  "contactPhone": "string",
  "contactEmail": "string",
  "businessLicense": "string (URL)"
}
```

---

## 04 商家模块

### 4.1 商家端

#### GET /api/merchant/info — 获取商家信息

**Response**:
```json
{
  "merchantId": 1,
  "merchantName": "测试店铺",
  "contactPhone": "13800138000",
  "status": "ACTIVE",
  "shopId": 1,
  "shopName": "测试店铺"
}
```

#### PUT /api/merchant/info — 更新商家信息

### 4.2 管理端

#### GET /api/admin/merchants — 商家列表（分页）

**Query**: `page`, `size`, `status`, `keyword`

#### GET /api/admin/merchants/{merchantId} — 商家详情

#### GET /api/admin/merchants/pending — 待审核商家列表

#### PATCH /api/admin/merchants/{merchantId}/approve — 审核通过

#### PATCH /api/admin/merchants/{merchantId}/reject — 审核拒绝

**Request**: `{"reason": "string"}`

#### PATCH /api/admin/merchants/{merchantId}/disable — 禁用商家

#### PATCH /api/admin/merchants/{merchantId}/enable — 启用商家

---

## 05 店铺模块

### 5.1 用户端

#### GET /api/public/shops/{shopId} — 店铺详情

**Response**:
```json
{
  "shopId": 1,
  "shopName": "测试店铺",
  "shopLogo": "/logo/1.jpg",
  "description": "店铺简介",
  "productCount": 50,
  "createdAt": "2026-07-01T10:00:00"
}
```

#### GET /api/public/shops/{shopId}/products — 店铺商品列表（分页）

**Query**: `page`, `size`, `sort`

### 5.2 商家端

#### GET /api/merchant/shop — 获取店铺信息

#### PUT /api/merchant/shop — 编辑店铺信息

**Request**:
```json
{
  "shopName": "string",
  "shopLogo": "string",
  "description": "string"
}
```

---

## 06 商品模块

### 6.1 用户端

#### GET /api/public/products — 商品列表（分页）

**Query**: `page`, `size`, `categoryId`, `keyword`, `minPrice`, `maxPrice`, `sort`

**Response**: PageResponse<ProductCardVO>
```json
{
  "list": [
    {
      "productId": 1,
      "name": "商品名称",
      "subtitle": "副标题",
      "mainImage": "/product/1.jpg",
      "price": 99.00,
      "originalPrice": 129.00,
      "sales": 100,
      "shopId": 1,
      "shopName": "店铺名"
    }
  ],
  "total": 100,
  "page": 1,
  "size": 20,
  "totalPages": 5
}
```

#### GET /api/public/products/{productId} — 商品详情

**Response**:
```json
{
  "productId": 1,
  "name": "商品名称",
  "subtitle": "副标题",
  "description": "<p>富文本描述</p>",
  "mainImage": "/product/1.jpg",
  "images": ["/product/1.jpg", "/product/2.jpg"],
  "price": 99.00,
  "originalPrice": 129.00,
  "stock": 500,
  "sales": 100,
  "viewCount": 5000,
  "categoryId": 1,
  "categoryName": "服饰",
  "shopId": 1,
  "shopName": "测试店铺",
  "skus": [
    {"skuId": 1, "skuName": "红色 M", "price": 99.00, "stock": 100}
  ],
  "reviewSummary": {
    "avgRating": 4.5,
    "reviewCount": 50,
    "goodRate": 90
  }
}
```

#### GET /api/public/products/search — 商品搜索

**Query**: `keyword`, `page`, `size`, `categoryId`, `minPrice`, `maxPrice`, `sort`

#### GET /api/public/categories — 分类树

**Response**:
```json
[
  {
    "id": 1, "name": "服饰", "icon": "/icon/1.png",
    "children": [
      {"id": 7, "name": "男装", "icon": null, "children": []},
      {"id": 8, "name": "女装", "icon": null, "children": []}
    ]
  }
]
```

#### GET /api/public/products/recommend — 首页推荐商品

**Query**: `size` (默认 10)

#### GET /api/public/products/hot — 热门商品

**Query**: `size` (默认 10)

#### GET /api/public/products/search/suggest — 搜索联想

**Query**: `keyword`

**Response**:
```json
{
  "suggestions": ["蓝牙耳机", "蓝牙音箱", "蓝牙鼠标"]
}
```

#### GET /api/public/products/search/hot — 热门搜索词

**Response**:
```json
{
  "keywords": ["耳机", "手机壳", "数据线"]
}
```

#### GET /api/public/brands — 品牌列表

**Response**:
```json
[
  {"brandId": 1, "brandName": "SoundTech", "brandLogo": "/brand/1.jpg"}
]
```

#### GET /api/public/products/new — 新品列表

**Query**: `size` (默认 10)

### 6.2 商家端

#### GET /api/merchant/products — 商家商品列表（分页）

**Query**: `page`, `size`, `status`, `keyword`

#### GET /api/merchant/products/{productId} — 商品详情

#### POST /api/merchant/products — 创建商品

**Request**:
```json
{
  "name": "string",
  "subtitle": "string",
  "description": "string",
  "categoryId": 1,
  "mainImage": "string",
  "images": ["string"],
  "price": 99.00,
  "originalPrice": 129.00,
  "stock": 500,
  "skus": [
    {"skuName": "红色 M", "price": 99.00, "stock": 100}
  ],
  "publish": true
}
```

#### PUT /api/merchant/products/{productId} — 编辑商品

#### PATCH /api/merchant/products/{productId}/publish — 上架商品

#### PATCH /api/merchant/products/{productId}/offline — 下架商品

#### DELETE /api/merchant/products/{productId} — 删除商品

### 6.3 商家端数据

#### GET /api/merchant/dashboard — 商家数据概览

**Response**:
```json
{
  "todayOrders": 15,
  "todaySales": 2985.00,
  "totalProducts": 48,
  "publishedProducts": 42,
  "pendingShipments": 3,
  "totalReviews": 128,
  "avgReviewScore": 4.6
}
```

#### POST /api/merchant/shop/banners — 添加店铺 Banner

#### DELETE /api/merchant/shop/banners/{bannerId} — 删除店铺 Banner

### 6.4 管理端

#### GET /api/admin/products — 全平台商品列表（分页）

**Query**: `page`, `size`, `status`, `shopId`, `keyword`

#### GET /api/admin/products/{productId} — 商品详情

#### PATCH /api/admin/products/{productId}/force-offline — 强制下架

**Request**: `{"reason": "string"}`

#### GET /api/admin/categories — 分类列表

#### POST /api/admin/categories — 创建分类

#### PUT /api/admin/categories/{categoryId} — 编辑分类

#### DELETE /api/admin/categories/{categoryId} — 删除分类

#### GET /api/admin/brands — 品牌列表

#### POST /api/admin/brands — 创建品牌

**Request**:
```json
{
  "brandName": "string",
  "brandLogo": "string",
  "brandDescription": "string"
}
```

#### PUT /api/admin/brands/{brandId} — 编辑品牌

#### DELETE /api/admin/brands/{brandId} — 删除品牌

---

## 07 购物车模块

#### GET /api/user/cart — 获取购物车

**Response**:
```json
{
  "cartId": 1,
  "shopGroups": [
    {
      "shopId": 1,
      "shopName": "测试店铺",
      "items": [
        {
          "cartItemId": 1,
          "productId": 1,
          "productName": "商品名称",
          "productImage": "/product/1.jpg",
          "skuId": null,
          "skuName": null,
          "price": 99.00,
          "quantity": 2,
          "stock": 500,
          "selected": 1,
          "available": true
        }
      ]
    }
  ],
  "totalAmount": 198.00,
  "selectedAmount": 198.00
}
```

#### POST /api/user/cart — 添加商品到购物车

**Request**:
```json
{
  "productId": 1,
  "skuId": null,
  "quantity": 1
}
```

#### PUT /api/user/cart/items/{cartItemId} — 修改购物车项

**Request**:
```json
{
  "quantity": 3,
  "selected": 1
}
```

#### DELETE /api/user/cart/items/{cartItemId} — 移除购物车项

#### DELETE /api/user/cart — 清空购物车

#### PUT /api/user/cart/select-all — 全选/取消全选

**Request**: `{"selected": 1}`

---

## 08 订单模块

### 8.1 用户端

#### POST /api/user/orders — 创建订单

**Request**:
```json
{
  "addressId": 1,
  "remark": "string (可选)",
  "cartItemIds": [1, 2, 3]
}
```

**Response**:
```json
{
  "orderId": 1,
  "orderNo": "SD20260703120001ABCD",
  "totalAmount": 297.00,
  "shippingFee": 10.00,
  "discountAmount": 0.00,
  "payAmount": 307.00
}
```

#### GET /api/user/orders — 订单列表（分页）

**Query**: `page`, `size`, `status`

**Response**: PageResponse<OrderListItemVO>
```json
{
  "list": [
    {
      "orderId": 1,
      "orderNo": "SD20260703120001ABCD",
      "status": "PAID",
      "totalAmount": 297.00,
      "payAmount": 307.00,
      "shopId": 1,
      "shopName": "测试店铺",
      "items": [
        {"productId": 1, "productName": "商品", "productImage": "/p/1.jpg", "quantity": 2, "price": 99.00}
      ],
      "createdAt": "2026-07-03T12:00:00"
    }
  ],
  "total": 10, "page": 1, "size": 20, "totalPages": 1
}
```

#### GET /api/user/orders/{orderNo} — 订单详情

**Response**:
```json
{
  "orderId": 1,
  "orderNo": "SD20260703120001ABCD",
  "status": "SHIPPED",
  "totalAmount": 297.00,
  "shippingFee": 10.00,
  "discountAmount": 0.00,
  "payAmount": 307.00,
  "receiverName": "张三",
  "receiverPhone": "13800138000",
  "receiverAddress": "广东省深圳市南山区科技园",
  "remark": "",
  "items": [
    {
      "productId": 1, "productName": "商品",
      "productImage": "/p/1.jpg",
      "skuId": null, "skuName": null,
      "price": 99.00, "quantity": 2, "subtotal": 198.00
    }
  ],
  "payment": {
    "paymentNo": "PAYxxx", "paymentMethod": "MOCK_ALIPAY",
    "amount": 307.00, "status": "SUCCESS", "paidAt": "2026-07-03T12:05:00"
  },
  "logistics": {
    "trackingNo": "LGxxx", "logisticsCompany": "模拟快递",
    "status": "IN_TRANSIT"
  },
  "createdAt": "2026-07-03T12:00:00",
  "paidAt": "2026-07-03T12:05:00",
  "shippedAt": "2026-07-03T14:00:00"
}
```

#### PATCH /api/user/orders/{orderNo}/cancel — 取消订单

#### PATCH /api/user/orders/{orderNo}/confirm — 确认收货

### 8.2 商家端

#### GET /api/merchant/orders — 商家订单列表（分页）

**Query**: `page`, `size`, `status`, `keyword`

#### GET /api/merchant/orders/{orderNo} — 订单详情

### 8.3 管理端

#### GET /api/admin/orders — 全平台订单列表（分页）

**Query**: `page`, `size`, `status`, `shopId`, `keyword`

#### GET /api/admin/orders/{orderNo} — 订单详情

---

## 09 支付模块

### POST /api/user/payments/{orderNo} — 发起支付

**Request**:
```json
{
  "paymentMethod": "MOCK_ALIPAY"
}
```

**Response**:
```json
{
  "paymentNo": "PAY20260703120500001",
  "orderNo": "SD20260703120001ABCD",
  "amount": 307.00,
  "paymentMethod": "MOCK_ALIPAY",
  "status": "SUCCESS",
  "paidAt": "2026-07-03T12:05:00"
}
```

> V1 Mock 支付直接返回 SUCCESS，无真实支付流程。

### GET /api/user/payments/{orderNo} — 查询支付状态

---

## 10 发货模块

### 10.1 商家端

#### GET /api/merchant/orders/shipping — 待发货订单列表

#### POST /api/merchant/shipments — 创建发货单

**Request**:
```json
{
  "orderNo": "SD20260703120001ABCD",
  "logisticsCompany": "模拟快递"
}
```

**Response**:
```json
{
  "shipmentId": 1,
  "shipmentNo": "SHP20260703140000001",
  "orderNo": "SD20260703120001ABCD",
  "status": "SHIPPED",
  "trackingNo": "LG20260703140000001"
}
```

### 10.2 通用

#### GET /api/common/shipments/{orderNo} — 查看发货信息

---

## 11 物流模块

#### GET /api/common/logistics/{orderNo} — 查看物流信息

**Response**:
```json
{
  "logisticsId": 1,
  "trackingNo": "LG20260703140000001",
  "logisticsCompany": "模拟快递",
  "status": "IN_TRANSIT",
  "tracks": [
    {"status": "CREATED", "description": "物流单已创建", "location": "深圳", "occurredAt": "2026-07-03T14:00:00"},
    {"status": "PICKED_UP", "description": "快递员已揽收", "location": "深圳", "occurredAt": "2026-07-03T14:30:00"},
    {"status": "SORTING", "description": "到达分拣中心", "location": "深圳转运中心", "occurredAt": "2026-07-03T15:00:00"},
    {"status": "IN_TRANSIT", "description": "运输中", "location": "深圳→上海", "occurredAt": "2026-07-03T16:00:00"}
  ]
}
```

---

## 12 评价模块

### 12.1 用户端

#### POST /api/user/reviews — 提交评价

**Request**:
```json
{
  "orderNo": "SD20260703120001ABCD",
  "items": [
    {
      "productId": 1,
      "rating": 5,
      "content": "商品质量很好",
      "images": ["/review/1.jpg", "/review/2.jpg"]
    }
  ]
}
```

#### GET /api/user/reviews — 我的评价列表（分页）

#### DELETE /api/user/reviews/{reviewId} — 删除评价

### 12.2 公开

#### GET /api/public/products/{productId}/reviews — 商品评价列表（分页）

**Query**: `page`, `size`, `rating` (按评分筛选)

**Response**: PageResponse<ReviewVO>
```json
{
  "list": [
    {
      "reviewId": 1,
      "userName": "测试**",
      "rating": 5,
      "content": "很好",
      "images": ["/review/1.jpg"],
      "merchantReply": "谢谢好评",
      "merchantRepliedAt": "2026-07-03T16:00:00",
      "createdAt": "2026-07-03T15:00:00"
    }
  ],
  "total": 50, "page": 1, "size": 10, "totalPages": 5
}
```

### 12.3 商家端

#### GET /api/merchant/reviews — 店铺评价列表（分页）

**Query**: `page`, `size`, `rating`, `hasImage`, `replied`

#### POST /api/merchant/reviews/{reviewId}/reply — 回复评价

**Request**: `{"content": "string"}`

### 12.4 管理端

#### GET /api/admin/reviews — 全平台评价列表（分页）

#### PATCH /api/admin/reviews/{reviewId}/hide — 隐藏评价

#### PATCH /api/admin/reviews/{reviewId}/show — 显示评价

#### DELETE /api/admin/reviews/{reviewId} — 删除评价

---

## 13 营销模块

### 13.1 签到

#### POST /api/user/sign-in — 每日签到

**Response**:
```json
{
  "signed": true,
  "consecutiveDays": 7,
  "pointsEarned": 6,
  "totalPoints": 126
}
```

#### GET /api/user/sign-in/today — 查询今日签到状态

**Response**:
```json
{
  "signedToday": true,
  "consecutiveDays": 7,
  "totalPoints": 126
}
```

#### GET /api/user/sign-in/records — 签到记录（分页）

### 13.2 Banner

#### GET /api/public/banners — 首页 Banner 列表

**Response**:
```json
[
  {"id": 1, "title": "欢迎", "imageUrl": "/banner/1.jpg", "linkUrl": "/"}
]
```

#### POST /api/admin/banners — 创建 Banner

#### PUT /api/admin/banners/{bannerId} — 编辑 Banner

#### DELETE /api/admin/banners/{bannerId} — 删除 Banner

---

## 14 系统管理模块

### 14.1 用户管理

#### GET /api/admin/users — 用户列表（分页）

**Query**: `page`, `size`, `status`, `keyword`

#### GET /api/admin/users/{userId} — 用户详情

#### PATCH /api/admin/users/{userId}/disable — 禁用用户

#### PATCH /api/admin/users/{userId}/enable — 启用用户

### 14.2 数据看板

#### GET /api/admin/dashboard — 仪表盘数据

**Response**:
```json
{
  "userCount": 1000,
  "merchantCount": 50,
  "productCount": 500,
  "orderCount": 2000,
  "todayOrderCount": 50,
  "todayNewUsers": 10,
  "totalGmv": 150000.00,
  "todayGmv": 3000.00
}
```

### 14.3 系统管理

#### GET /api/admin/menus — 菜单树

#### POST /api/admin/menus — 创建菜单

#### PUT /api/admin/menus/{menuId} — 编辑菜单

#### DELETE /api/admin/menus/{menuId} — 删除菜单

#### GET /api/admin/dicts — 字典类型列表

#### POST /api/admin/dicts — 创建字典类型

#### GET /api/admin/dicts/{dictTypeId}/items — 字典项列表

#### POST /api/admin/dicts/{dictTypeId}/items — 创建字典项

#### GET /api/admin/logs — 操作日志列表（分页）

**Query**: `page`, `size`, `module`, `operatorId`, `startDate`, `endDate`

### 14.7 角色管理

#### GET /api/admin/roles — 角色列表

#### POST /api/admin/roles — 创建角色

#### PUT /api/admin/roles/{roleId} — 编辑角色

#### DELETE /api/admin/roles/{roleId} — 删除角色

#### GET /api/admin/roles/{roleId}/permissions — 角色权限列表

#### PUT /api/admin/roles/{roleId}/permissions — 分配角色权限

**Request**:
```json
{
  "permissionIds": [1, 2, 3]
}
```

#### GET /api/admin/roles/{roleId}/menus — 角色菜单列表

#### PUT /api/admin/roles/{roleId}/menus — 分配角色菜单

**Request**:
```json
{
  "menuIds": [1, 2, 3]
}
```

### 14.8 权限管理

#### GET /api/admin/permissions — 权限列表

#### POST /api/admin/permissions — 创建权限

#### PUT /api/admin/permissions/{permissionId} — 编辑权限

#### DELETE /api/admin/permissions/{permissionId} — 删除权限

### 14.9 消息通知

#### GET /api/notifications — 消息列表

**Query**: `type`, `isRead`, `pageNum`, `pageSize`

**Response**:
```json
{
  "list": [
    {
      "id": 1,
      "notificationType": "ORDER",
      "title": "订单创建",
      "content": "您的订单 SD20260702120001 已创建",
      "isRead": 0,
      "referenceId": 8001,
      "referenceType": "ORDER",
      "createdAt": "2026-07-02T12:00:01"
    }
  ]
}
```

#### GET /api/notifications/unread-count — 未读消息数量

**Response**:
```json
{
  "unreadCount": 5
}
```

#### PUT /api/notifications/{notificationId}/read — 标记已读

#### PUT /api/notifications/read-all — 全部标记已读

#### DELETE /api/notifications/{notificationId} — 删除消息

---

## 15 文件上传模块

### POST /api/common/upload — 文件上传

**Content-Type**: multipart/form-data

**Request**:
```
file: <binary>
type: avatar | product | review | banner | license
```

**Response**:
```json
{
  "url": "/uploads/avatar/20260703/uuid.jpg",
  "originalName": "photo.jpg",
  "size": 102400,
  "contentType": "image/jpeg"
}
```

**限制**:
- 允许格式: jpg, jpeg, png, gif, webp
- 最大大小: 5MB
- 存储路径: `/uploads/{type}/{yyyyMMdd}/{uuid}.{ext}`

---

## 17 优惠券模块

### 17.1 用户端

#### GET /api/user/coupons/available — 可领取优惠券列表

**权限**: 公开/登录

**Query**: `page`, `size`

**Response**:
```json
{
  "list": [
    {
      "couponId": 1,
      "couponCode": "SAVE10",
      "couponName": "新用户立减10元",
      "couponType": "FIXED_AMOUNT",
      "discountValue": 10.00,
      "minSpend": 50.00,
      "totalQuantity": 1000,
      "claimedQuantity": 320,
      "validStartTime": "2026-07-01T00:00:00",
      "validEndTime": "2026-07-31T23:59:59",
      "applicableScope": "ALL"
    }
  ],
  "total": 10,
  "page": 1,
  "size": 20,
  "totalPages": 1
}
```

#### POST /api/user/coupons/{couponId}/claim — 领取优惠券

**权限**: 用户登录

**Request**:
```json
{}
```

**Response**:
```json
{
  "userCouponId": 1,
  "couponId": 1,
  "couponCode": "SAVE10",
  "couponName": "新用户立减10元",
  "couponType": "FIXED_AMOUNT",
  "discountValue": 10.00,
  "minSpend": 50.00,
  "validStartTime": "2026-07-01T00:00:00",
  "validEndTime": "2026-07-31T23:59:59",
  "status": "UNUSED",
  "claimedAt": "2026-07-07T10:00:00"
}
```

#### GET /api/user/coupons/my — 我的优惠券列表

**权限**: 用户登录

**Query**: `page`, `size`, `status` (UNUSED/USED/EXPIRED)

**Response**:
```json
{
  "list": [
    {
      "userCouponId": 1,
      "couponId": 1,
      "couponCode": "SAVE10",
      "couponName": "新用户立减10元",
      "couponType": "FIXED_AMOUNT",
      "discountValue": 10.00,
      "minSpend": 50.00,
      "validStartTime": "2026-07-01T00:00:00",
      "validEndTime": "2026-07-31T23:59:59",
      "status": "UNUSED",
      "claimedAt": "2026-07-07T10:00:00",
      "usedAt": null
    }
  ],
  "total": 5,
  "page": 1,
  "size": 20,
  "totalPages": 1
}
```

#### GET /api/user/coupons/{couponId} — 优惠券详情

**权限**: 用户登录

**Response**:
```json
{
  "couponId": 1,
  "couponCode": "SAVE10",
  "couponName": "新用户立减10元",
  "couponType": "FIXED_AMOUNT",
  "discountValue": 10.00,
  "minSpend": 50.00,
  "totalQuantity": 1000,
  "claimedQuantity": 320,
  "usedQuantity": 150,
  "validStartTime": "2026-07-01T00:00:00",
  "validEndTime": "2026-07-31T23:59:59",
  "applicableScope": "ALL",
  "applicableIds": [],
  "status": "ACTIVE"
}
```

### 17.2 管理端

#### GET /api/admin/coupons — 优惠券列表

**权限**: 管理员

**Query**: `page`, `size`, `status`, `keyword`

**Response**:
```json
{
  "list": [
    {
      "couponId": 1,
      "couponCode": "SAVE10",
      "couponName": "新用户立减10元",
      "couponType": "FIXED_AMOUNT",
      "discountValue": 10.00,
      "minSpend": 50.00,
      "totalQuantity": 1000,
      "claimedQuantity": 320,
      "usedQuantity": 150,
      "validStartTime": "2026-07-01T00:00:00",
      "validEndTime": "2026-07-31T23:59:59",
      "applicableScope": "ALL",
      "status": "ACTIVE"
    }
  ],
  "total": 10,
  "page": 1,
  "size": 20,
  "totalPages": 1
}
```

#### POST /api/admin/coupons — 创建优惠券

**权限**: 管理员

**Request**:
```json
{
  "couponCode": "SAVE10",
  "couponName": "新用户立减10元",
  "couponType": "FIXED_AMOUNT",
  "discountValue": 10.00,
  "minSpend": 50.00,
  "totalQuantity": 1000,
  "validStartTime": "2026-07-01T00:00:00",
  "validEndTime": "2026-07-31T23:59:59",
  "applicableScope": "ALL",
  "applicableIds": []
}
```

**Response**:
```json
{
  "couponId": 1,
  "couponCode": "SAVE10",
  "couponName": "新用户立减10元",
  "status": "ACTIVE"
}
```

#### PUT /api/admin/coupons/{couponId} — 编辑优惠券

**权限**: 管理员

**Request**:
```json
{
  "couponName": "新用户立减10元",
  "discountValue": 10.00,
  "minSpend": 50.00,
  "totalQuantity": 1000,
  "validStartTime": "2026-07-01T00:00:00",
  "validEndTime": "2026-07-31T23:59:59",
  "applicableScope": "ALL",
  "applicableIds": []
}
```

**Response**: 无 data

#### DELETE /api/admin/coupons/{couponId} — 删除优惠券

**权限**: 管理员

**Request**:
```json
{}
```

**Response**: 无 data

#### PATCH /api/admin/coupons/{couponId}/enable — 启用优惠券

**权限**: 管理员

**Request**:
```json
{}
```

**Response**:
```json
{
  "couponId": 1,
  "status": "ACTIVE"
}
```

#### PATCH /api/admin/coupons/{couponId}/disable — 禁用优惠券

**权限**: 管理员

**Request**:
```json
{}
```

**Response**:
```json
{
  "couponId": 1,
  "status": "INACTIVE"
}
```

#### GET /api/admin/coupons/{couponId}/stats — 领取和使用统计

**权限**: 管理员

**Response**:
```json
{
  "couponId": 1,
  "couponName": "新用户立减10元",
  "totalQuantity": 1000,
  "claimedQuantity": 320,
  "usedQuantity": 150,
  "claimRate": 32.0,
  "useRate": 46.9,
  "totalDiscountAmount": 1500.00
}
```

---

## 18 积分模块

### 18.1 用户端

#### GET /api/user/points — 积分余额和总积分

**权限**: 用户登录

**Response**:
```json
{
  "currentPoints": 126,
  "totalEarned": 500,
  "totalSpent": 374
}
```

#### GET /api/user/points/records — 积分明细

**权限**: 用户登录

**Query**: `page`, `size`, `type` (EARN/SPEND)

**Response**:
```json
{
  "list": [
    {
      "recordId": 1,
      "userId": 1,
      "points": 10,
      "type": "EARN",
      "source": "SIGN_IN",
      "description": "每日签到奖励",
      "relatedId": null,
      "createdAt": "2026-07-07T10:00:00"
    },
    {
      "recordId": 2,
      "userId": 1,
      "points": -100,
      "type": "SPEND",
      "source": "EXCHANGE",
      "description": "积分兑换商品",
      "relatedId": 5,
      "createdAt": "2026-07-06T15:00:00"
    }
  ],
  "total": 50,
  "page": 1,
  "size": 20,
  "totalPages": 3
}
```

#### GET /api/user/points/products — 积分兑换商品列表

**权限**: 公开/登录

**Query**: `page`, `size`

**Response**:
```json
{
  "list": [
    {
      "productId": 1,
      "productName": "定制帆布袋",
      "description": "环保定制帆布袋",
      "imageUrl": "/points/1.jpg",
      "pointsRequired": 200,
      "stock": 50,
      "status": "ACTIVE"
    }
  ],
  "total": 10,
  "page": 1,
  "size": 20,
  "totalPages": 1
}
```

#### POST /api/user/points/products/{productId}/exchange — 积分兑换

**权限**: 用户登录

**Request**:
```json
{
  "quantity": 1
}
```

**Response**:
```json
{
  "exchangeId": 1,
  "productId": 1,
  "productName": "定制帆布袋",
  "pointsUsed": 200,
  "quantity": 1,
  "status": "SUCCESS",
  "exchangedAt": "2026-07-07T10:00:00"
}
```

### 18.2 管理端

#### GET /api/admin/points/records — 全平台积分明细

**权限**: 管理员

**Query**: `page`, `size`, `userId`, `type`, `source`

**Response**:
```json
{
  "list": [
    {
      "recordId": 1,
      "userId": 1,
      "nickname": "测试用户",
      "points": 10,
      "type": "EARN",
      "source": "SIGN_IN",
      "description": "每日签到奖励",
      "relatedId": null,
      "createdAt": "2026-07-07T10:00:00"
    }
  ],
  "total": 1000,
  "page": 1,
  "size": 20,
  "totalPages": 50
}
```

#### GET /api/admin/points/products — 积分商品列表

**权限**: 管理员

**Query**: `page`, `size`, `status`, `keyword`

**Response**:
```json
{
  "list": [
    {
      "productId": 1,
      "productName": "定制帆布袋",
      "description": "环保定制帆布袋",
      "imageUrl": "/points/1.jpg",
      "pointsRequired": 200,
      "stock": 50,
      "status": "ACTIVE"
    }
  ],
  "total": 10,
  "page": 1,
  "size": 20,
  "totalPages": 1
}
```

#### POST /api/admin/points/products — 创建积分商品

**权限**: 管理员

**Request**:
```json
{
  "productName": "定制帆布袋",
  "description": "环保定制帆布袋",
  "imageUrl": "/points/1.jpg",
  "pointsRequired": 200,
  "stock": 50
}
```

**Response**:
```json
{
  "productId": 1,
  "productName": "定制帆布袋",
  "status": "ACTIVE"
}
```

#### PUT /api/admin/points/products/{productId} — 编辑积分商品

**权限**: 管理员

**Request**:
```json
{
  "productName": "定制帆布袋",
  "description": "环保定制帆布袋",
  "imageUrl": "/points/1.jpg",
  "pointsRequired": 200,
  "stock": 50
}
```

**Response**: 无 data

#### DELETE /api/admin/points/products/{productId} — 删除积分商品

**权限**: 管理员

**Request**:
```json
{}
```

**Response**: 无 data

---

## 19 秒杀模块

### 19.1 用户端

#### GET /api/public/flash-sales — 进行中的秒杀活动列表

**权限**: 公开

**Query**: `page`, `size`

**Response**:
```json
{
  "list": [
    {
      "saleId": 1,
      "productId": 1,
      "productName": "无线蓝牙耳机",
      "productImage": "/product/1.jpg",
      "originalPrice": 199.00,
      "flashPrice": 99.00,
      "stock": 100,
      "soldCount": 45,
      "startTime": "2026-07-07T10:00:00",
      "endTime": "2026-07-07T12:00:00",
      "limitPerUser": 2,
      "status": "ACTIVE"
    }
  ],
  "total": 5,
  "page": 1,
  "size": 20,
  "totalPages": 1
}
```

#### GET /api/public/flash-sales/{saleId} — 秒杀详情

**权限**: 公开

**Response**:
```json
{
  "saleId": 1,
  "productId": 1,
  "productName": "无线蓝牙耳机",
  "productImage": "/product/1.jpg",
  "originalPrice": 199.00,
  "flashPrice": 99.00,
  "stock": 100,
  "soldCount": 45,
  "startTime": "2026-07-07T10:00:00",
  "endTime": "2026-07-07T12:00:00",
  "limitPerUser": 2,
  "status": "ACTIVE"
}
```

#### POST /api/user/flash-sales/{saleId}/order — 参与秒杀下单

**权限**: 用户登录

**Request**:
```json
{
  "addressId": 1,
  "quantity": 1
}
```

**Response**:
```json
{
  "orderId": 1,
  "orderNo": "SD20260707100001FLAS",
  "totalAmount": 99.00,
  "shippingFee": 0.00,
  "discountAmount": 0.00,
  "payAmount": 99.00,
  "saleId": 1,
  "flashPrice": 99.00
}
```

### 19.2 管理端

#### GET /api/admin/flash-sales — 秒杀活动列表

**权限**: 管理员

**Query**: `page`, `size`, `status`, `keyword`

**Response**:
```json
{
  "list": [
    {
      "saleId": 1,
      "productId": 1,
      "productName": "无线蓝牙耳机",
      "originalPrice": 199.00,
      "flashPrice": 99.00,
      "stock": 100,
      "soldCount": 45,
      "startTime": "2026-07-07T10:00:00",
      "endTime": "2026-07-07T12:00:00",
      "limitPerUser": 2,
      "status": "ACTIVE"
    }
  ],
  "total": 10,
  "page": 1,
  "size": 20,
  "totalPages": 1
}
```

#### POST /api/admin/flash-sales — 创建秒杀

**权限**: 管理员

**Request**:
```json
{
  "productId": 1,
  "flashPrice": 99.00,
  "stock": 100,
  "startTime": "2026-07-07T10:00:00",
  "endTime": "2026-07-07T12:00:00",
  "limitPerUser": 2
}
```

**Response**:
```json
{
  "saleId": 1,
  "productId": 1,
  "flashPrice": 99.00,
  "status": "ACTIVE"
}
```

#### PUT /api/admin/flash-sales/{saleId} — 编辑秒杀

**权限**: 管理员

**Request**:
```json
{
  "flashPrice": 89.00,
  "stock": 150,
  "startTime": "2026-07-07T10:00:00",
  "endTime": "2026-07-07T14:00:00",
  "limitPerUser": 2
}
```

**Response**: 无 data

#### DELETE /api/admin/flash-sales/{saleId} — 删除秒杀

**权限**: 管理员

**Request**:
```json
{}
```

**Response**: 无 data

#### PATCH /api/admin/flash-sales/{saleId}/enable — 启用秒杀

**权限**: 管理员

**Request**:
```json
{}
```

**Response**:
```json
{
  "saleId": 1,
  "status": "ACTIVE"
}
```

#### PATCH /api/admin/flash-sales/{saleId}/disable — 禁用秒杀

**权限**: 管理员

**Request**:
```json
{}
```

**Response**:
```json
{
  "saleId": 1,
  "status": "INACTIVE"
}
```

---

## 20 专题活动模块

### 20.1 用户端

#### GET /api/public/activities — 活动列表

**权限**: 公开

**Query**: `page`, `size`, `status`

**Response**:
```json
{
  "list": [
    {
      "activityId": 1,
      "title": "夏日清凉节",
      "subtitle": "全场冷饮低至5折",
      "coverImage": "/activity/1.jpg",
      "bannerImage": "/activity/banner1.jpg",
      "description": "夏日清凉节活动描述",
      "startTime": "2026-07-01T00:00:00",
      "endTime": "2026-07-15T23:59:59",
      "sortOrder": 1,
      "status": "ACTIVE"
    }
  ],
  "total": 5,
  "page": 1,
  "size": 20,
  "totalPages": 1
}
```

#### GET /api/public/activities/{activityId} — 活动详情

**权限**: 公开

**Response**:
```json
{
  "activityId": 1,
  "title": "夏日清凉节",
  "subtitle": "全场冷饮低至5折",
  "coverImage": "/activity/1.jpg",
  "bannerImage": "/activity/banner1.jpg",
  "description": "夏日清凉节活动描述",
  "startTime": "2026-07-01T00:00:00",
  "endTime": "2026-07-15T23:59:59",
  "sortOrder": 1,
  "status": "ACTIVE",
  "products": [
    {
      "productId": 1,
      "name": "冰镇果汁",
      "mainImage": "/product/1.jpg",
      "price": 9.90,
      "originalPrice": 19.90
    }
  ]
}
```

### 20.2 管理端

#### GET /api/admin/activities — 活动列表

**权限**: 管理员

**Query**: `page`, `size`, `status`, `keyword`

**Response**:
```json
{
  "list": [
    {
      "activityId": 1,
      "title": "夏日清凉节",
      "subtitle": "全场冷饮低至5折",
      "coverImage": "/activity/1.jpg",
      "bannerImage": "/activity/banner1.jpg",
      "startTime": "2026-07-01T00:00:00",
      "endTime": "2026-07-15T23:59:59",
      "sortOrder": 1,
      "status": "ACTIVE"
    }
  ],
  "total": 10,
  "page": 1,
  "size": 20,
  "totalPages": 1
}
```

#### POST /api/admin/activities — 创建活动

**权限**: 管理员

**Request**:
```json
{
  "title": "夏日清凉节",
  "subtitle": "全场冷饮低至5折",
  "coverImage": "/activity/1.jpg",
  "bannerImage": "/activity/banner1.jpg",
  "description": "夏日清凉节活动描述",
  "startTime": "2026-07-01T00:00:00",
  "endTime": "2026-07-15T23:59:59",
  "sortOrder": 1
}
```

**Response**:
```json
{
  "activityId": 1,
  "title": "夏日清凉节",
  "status": "ACTIVE"
}
```

#### PUT /api/admin/activities/{activityId} — 编辑活动

**权限**: 管理员

**Request**:
```json
{
  "title": "夏日清凉节",
  "subtitle": "全场冷饮低至5折",
  "coverImage": "/activity/1.jpg",
  "bannerImage": "/activity/banner1.jpg",
  "description": "夏日清凉节活动描述",
  "startTime": "2026-07-01T00:00:00",
  "endTime": "2026-07-15T23:59:59",
  "sortOrder": 1
}
```

**Response**: 无 data

#### DELETE /api/admin/activities/{activityId} — 删除活动

**权限**: 管理员

**Request**:
```json
{}
```

**Response**: 无 data

#### POST /api/admin/activities/{activityId}/products — 添加活动商品

**权限**: 管理员

**Request**:
```json
{
  "productIds": [1, 2, 3]
}
```

**Response**:
```json
{
  "activityId": 1,
  "addedCount": 3
}
```

#### DELETE /api/admin/activities/{activityId}/products/{productId} — 移除活动商品

**权限**: 管理员

**Request**:
```json
{}
```

**Response**: 无 data

---

## 21 排行榜模块

### 21.1 用户端

#### GET /api/public/rankings/consumption — 消费排行榜

**权限**: 公开

**Query**: `period` (WEEK/MONTH/ALL)

**Response**:
```json
{
  "period": "MONTH",
  "list": [
    {
      "rank": 1,
      "userId": 1,
      "nickname": "张三",
      "avatar": "/avatar/1.jpg",
      "value": 5000.00,
      "unit": "元"
    },
    {
      "rank": 2,
      "userId": 2,
      "nickname": "李四",
      "avatar": "/avatar/2.jpg",
      "value": 3200.00,
      "unit": "元"
    }
  ]
}
```

#### GET /api/public/rankings/sign-in — 签到排行榜

**权限**: 公开

**Query**: `period` (WEEK/MONTH/ALL)

**Response**:
```json
{
  "period": "MONTH",
  "list": [
    {
      "rank": 1,
      "userId": 1,
      "nickname": "张三",
      "avatar": "/avatar/1.jpg",
      "value": 30,
      "unit": "天"
    },
    {
      "rank": 2,
      "userId": 2,
      "nickname": "李四",
      "avatar": "/avatar/2.jpg",
      "value": 28,
      "unit": "天"
    }
  ]
}
```

---

## 附录：API 清单汇总

| # | Method | URL | 权限 | 说明 |
|---|---|---|---|---|
| 1 | POST | /api/common/register | 公开 | 用户注册 |
| 2 | POST | /api/common/login | 公开 | 用户登录 |
| 3 | POST | /api/common/logout | 登录 | 退出登录 |
| 4 | POST | /api/common/refresh-token | 公开 | 刷新Token |
| 5 | GET | /api/common/userinfo | 登录 | 当前用户信息 |
| 6 | PUT | /api/user/profile | 用户 | 更新个人信息 |
| 7 | PUT | /api/user/password | 用户 | 修改密码 |
| 8 | GET | /api/user/addresses | 用户 | 地址列表 |
| 9 | POST | /api/user/addresses | 用户 | 新增地址 |
| 10 | PUT | /api/user/addresses/{addressId} | 用户 | 编辑地址 |
| 11 | DELETE | /api/user/addresses/{addressId} | 用户 | 删除地址 |
| 12 | PUT | /api/user/addresses/{addressId}/default | 用户 | 设默认地址 |
| 13 | GET | /api/user/favorites | 用户 | 收藏列表 |
| 14 | POST | /api/user/favorites/{productId} | 用户 | 收藏商品 |
| 15 | DELETE | /api/user/favorites/{productId} | 用户 | 取消收藏 |
| 16 | GET | /api/user/history | 用户 | 浏览历史 |
| 17 | DELETE | /api/user/history | 用户 | 清空历史 |
| 18 | GET | /api/user/search-history | 用户 | 搜索历史 |
| 19 | DELETE | /api/user/search-history | 用户 | 清空搜索历史 |
| 20 | POST | /api/user/merchant-application | 用户 | 商家申请 |
| 21 | GET | /api/merchant/info | 商家 | 商家信息 |
| 22 | PUT | /api/merchant/info | 商家 | 更新商家信息 |
| 23 | GET | /api/merchant/shop | 商家 | 店铺信息 |
| 24 | PUT | /api/merchant/shop | 商家 | 编辑店铺 |
| 25 | GET | /api/merchant/products | 商家 | 商品列表 |
| 26 | GET | /api/merchant/products/{productId} | 商家 | 商品详情 |
| 27 | POST | /api/merchant/products | 商家 | 创建商品 |
| 28 | PUT | /api/merchant/products/{productId} | 商家 | 编辑商品 |
| 29 | PATCH | /api/merchant/products/{productId}/publish | 商家 | 上架 |
| 30 | PATCH | /api/merchant/products/{productId}/offline | 商家 | 下架 |
| 31 | DELETE | /api/merchant/products/{productId} | 商家 | 删除商品 |
| 32 | GET | /api/merchant/orders | 商家 | 订单列表 |
| 33 | GET | /api/merchant/orders/{orderNo} | 商家 | 订单详情 |
| 34 | GET | /api/merchant/orders/shipping | 商家 | 待发货列表 |
| 35 | POST | /api/merchant/shipments | 商家 | 创建发货 |
| 36 | GET | /api/merchant/reviews | 商家 | 评价列表 |
| 37 | POST | /api/merchant/reviews/{reviewId}/reply | 商家 | 回复评价 |
| 38 | GET | /api/merchant/dashboard | 商家 | 商家数据概览 |
| 39 | POST | /api/merchant/shop/banners | 商家 | 添加店铺Banner |
| 40 | DELETE | /api/merchant/shop/banners/{bannerId} | 商家 | 删除店铺Banner |
| 41 | GET | /api/public/products | 公开 | 商品列表 |
| 42 | GET | /api/public/products/{productId} | 公开 | 商品详情 |
| 43 | GET | /api/public/products/search | 公开 | 商品搜索 |
| 44 | GET | /api/public/categories | 公开 | 分类树 |
| 45 | GET | /api/public/products/recommend | 公开 | 推荐商品 |
| 46 | GET | /api/public/products/hot | 公开 | 热门商品 |
| 47 | GET | /api/public/products/search/suggest | 公开 | 搜索联想 |
| 48 | GET | /api/public/products/search/hot | 公开 | 热门搜索词 |
| 49 | GET | /api/public/brands | 公开 | 品牌列表 |
| 50 | GET | /api/public/products/new | 公开 | 新品列表 |
| 51 | GET | /api/public/shops/{shopId} | 公开 | 店铺详情 |
| 52 | GET | /api/public/shops/{shopId}/products | 公开 | 店铺商品 |
| 53 | GET | /api/public/products/{productId}/reviews | 公开 | 商品评价 |
| 54 | GET | /api/public/banners | 公开 | Banner列表 |
| 55 | GET | /api/user/cart | 用户 | 购物车 |
| 56 | POST | /api/user/cart | 用户 | 加购物车 |
| 57 | PUT | /api/user/cart/items/{cartItemId} | 用户 | 修改购物车项 |
| 58 | DELETE | /api/user/cart/items/{cartItemId} | 用户 | 移除购物车项 |
| 59 | DELETE | /api/user/cart | 用户 | 清空购物车 |
| 60 | PUT | /api/user/cart/select-all | 用户 | 全选/取消 |
| 61 | POST | /api/user/orders | 用户 | 创建订单 |
| 62 | GET | /api/user/orders | 用户 | 订单列表 |
| 63 | GET | /api/user/orders/{orderNo} | 用户 | 订单详情 |
| 64 | PATCH | /api/user/orders/{orderNo}/cancel | 用户 | 取消订单 |
| 65 | PATCH | /api/user/orders/{orderNo}/confirm | 用户 | 确认收货 |
| 66 | POST | /api/user/payments/{orderNo} | 用户 | 发起支付 |
| 67 | GET | /api/user/payments/{orderNo} | 用户 | 查询支付 |
| 68 | POST | /api/user/reviews | 用户 | 提交评价 |
| 69 | GET | /api/user/reviews | 用户 | 我的评价 |
| 70 | DELETE | /api/user/reviews/{reviewId} | 用户 | 删除评价 |
| 71 | POST | /api/user/sign-in | 用户 | 每日签到 |
| 72 | GET | /api/user/sign-in/today | 用户 | 签到状态 |
| 73 | GET | /api/user/sign-in/records | 用户 | 签到记录 |
| 74 | GET | /api/common/logistics/{orderNo} | 登录 | 物流信息 |
| 75 | GET | /api/common/shipments/{orderNo} | 登录 | 发货信息 |
| 76 | POST | /api/common/upload | 登录 | 文件上传 |
| 77 | GET | /api/admin/users | 管理员 | 用户列表 |
| 78 | GET | /api/admin/users/{userId} | 管理员 | 用户详情 |
| 79 | PATCH | /api/admin/users/{userId}/disable | 管理员 | 禁用用户 |
| 80 | PATCH | /api/admin/users/{userId}/enable | 管理员 | 启用用户 |
| 81 | GET | /api/admin/merchants | 管理员 | 商家列表 |
| 82 | GET | /api/admin/merchants/{merchantId} | 管理员 | 商家详情 |
| 83 | GET | /api/admin/merchants/pending | 管理员 | 待审核商家 |
| 84 | PATCH | /api/admin/merchants/{merchantId}/approve | 管理员 | 审核通过 |
| 85 | PATCH | /api/admin/merchants/{merchantId}/reject | 管理员 | 审核拒绝 |
| 86 | PATCH | /api/admin/merchants/{merchantId}/disable | 管理员 | 禁用商家 |
| 87 | PATCH | /api/admin/merchants/{merchantId}/enable | 管理员 | 启用商家 |
| 88 | GET | /api/admin/products | 管理员 | 全平台商品 |
| 89 | GET | /api/admin/products/{productId} | 管理员 | 商品详情 |
| 90 | PATCH | /api/admin/products/{productId}/force-offline | 管理员 | 强制下架 |
| 91 | GET | /api/admin/categories | 管理员 | 分类列表 |
| 92 | POST | /api/admin/categories | 管理员 | 创建分类 |
| 93 | PUT | /api/admin/categories/{categoryId} | 管理员 | 编辑分类 |
| 94 | DELETE | /api/admin/categories/{categoryId} | 管理员 | 删除分类 |
| 95 | GET | /api/admin/brands | 管理员 | 品牌列表 |
| 96 | POST | /api/admin/brands | 管理员 | 创建品牌 |
| 97 | PUT | /api/admin/brands/{brandId} | 管理员 | 编辑品牌 |
| 98 | DELETE | /api/admin/brands/{brandId} | 管理员 | 删除品牌 |
| 99 | GET | /api/admin/orders | 管理员 | 全平台订单 |
| 100 | GET | /api/admin/orders/{orderNo} | 管理员 | 订单详情 |
| 101 | GET | /api/admin/reviews | 管理员 | 全平台评价 |
| 102 | PATCH | /api/admin/reviews/{reviewId}/hide | 管理员 | 隐藏评价 |
| 103 | PATCH | /api/admin/reviews/{reviewId}/show | 管理员 | 显示评价 |
| 104 | DELETE | /api/admin/reviews/{reviewId} | 管理员 | 删除评价 |
| 105 | POST | /api/admin/banners | 管理员 | 创建Banner |
| 106 | PUT | /api/admin/banners/{bannerId} | 管理员 | 编辑Banner |
| 107 | DELETE | /api/admin/banners/{bannerId} | 管理员 | 删除Banner |
| 108 | GET | /api/admin/dashboard | 管理员 | 仪表盘 |
| 109 | GET | /api/admin/menus | 管理员 | 菜单树 |
| 110 | POST | /api/admin/menus | 管理员 | 创建菜单 |
| 111 | PUT | /api/admin/menus/{menuId} | 管理员 | 编辑菜单 |
| 112 | DELETE | /api/admin/menus/{menuId} | 管理员 | 删除菜单 |
| 113 | GET | /api/admin/dicts | 管理员 | 字典列表 |
| 114 | POST | /api/admin/dicts | 管理员 | 创建字典 |
| 115 | GET | /api/admin/dicts/{dictTypeId}/items | 管理员 | 字典项 |
| 116 | POST | /api/admin/dicts/{dictTypeId}/items | 管理员 | 创建字典项 |
| 117 | GET | /api/admin/logs | 管理员 | 操作日志 |
| 118 | GET | /api/admin/roles | 管理员 | 角色列表 |
| 119 | POST | /api/admin/roles | 管理员 | 创建角色 |
| 120 | PUT | /api/admin/roles/{roleId} | 管理员 | 编辑角色 |
| 121 | DELETE | /api/admin/roles/{roleId} | 管理员 | 删除角色 |
| 122 | GET | /api/admin/roles/{roleId}/permissions | 管理员 | 角色权限 |
| 123 | PUT | /api/admin/roles/{roleId}/permissions | 管理员 | 分配权限 |
| 124 | GET | /api/admin/roles/{roleId}/menus | 管理员 | 角色菜单 |
| 125 | PUT | /api/admin/roles/{roleId}/menus | 管理员 | 分配菜单 |
| 126 | GET | /api/admin/permissions | 管理员 | 权限列表 |
| 127 | POST | /api/admin/permissions | 管理员 | 创建权限 |
| 128 | PUT | /api/admin/permissions/{permissionId} | 管理员 | 编辑权限 |
| 129 | DELETE | /api/admin/permissions/{permissionId} | 管理员 | 删除权限 |
| 130 | GET | /api/notifications | 登录 | 消息列表 |
| 131 | GET | /api/notifications/unread-count | 登录 | 未读消息数 |
| 132 | PUT | /api/notifications/{notificationId}/read | 登录 | 标记已读 |
| 133 | PUT | /api/notifications/read-all | 登录 | 全部已读 |
| 134 | DELETE | /api/notifications/{notificationId} | 登录 | 删除消息 |
| 135 | GET | /api/user/coupons/available | 公开 | 可领取优惠券列表 |
| 136 | POST | /api/user/coupons/{couponId}/claim | 用户 | 领取优惠券 |
| 137 | GET | /api/user/coupons/my | 用户 | 我的优惠券列表 |
| 138 | GET | /api/user/coupons/{couponId} | 用户 | 优惠券详情 |
| 139 | GET | /api/admin/coupons | 管理员 | 优惠券列表 |
| 140 | POST | /api/admin/coupons | 管理员 | 创建优惠券 |
| 141 | PUT | /api/admin/coupons/{couponId} | 管理员 | 编辑优惠券 |
| 142 | DELETE | /api/admin/coupons/{couponId} | 管理员 | 删除优惠券 |
| 143 | PATCH | /api/admin/coupons/{couponId}/enable | 管理员 | 启用优惠券 |
| 144 | PATCH | /api/admin/coupons/{couponId}/disable | 管理员 | 禁用优惠券 |
| 145 | GET | /api/admin/coupons/{couponId}/stats | 管理员 | 优惠券统计 |
| 146 | GET | /api/user/points | 用户 | 积分余额 |
| 147 | GET | /api/user/points/records | 用户 | 积分明细 |
| 148 | GET | /api/user/points/products | 公开 | 积分商品列表 |
| 149 | POST | /api/user/points/products/{productId}/exchange | 用户 | 积分兑换 |
| 150 | GET | /api/admin/points/records | 管理员 | 全平台积分明细 |
| 151 | GET | /api/admin/points/products | 管理员 | 积分商品列表 |
| 152 | POST | /api/admin/points/products | 管理员 | 创建积分商品 |
| 153 | PUT | /api/admin/points/products/{productId} | 管理员 | 编辑积分商品 |
| 154 | DELETE | /api/admin/points/products/{productId} | 管理员 | 删除积分商品 |
| 155 | GET | /api/public/flash-sales | 公开 | 秒杀活动列表 |
| 156 | GET | /api/public/flash-sales/{saleId} | 公开 | 秒杀详情 |
| 157 | POST | /api/user/flash-sales/{saleId}/order | 用户 | 秒杀下单 |
| 158 | GET | /api/admin/flash-sales | 管理员 | 秒杀活动列表 |
| 159 | POST | /api/admin/flash-sales | 管理员 | 创建秒杀 |
| 160 | PUT | /api/admin/flash-sales/{saleId} | 管理员 | 编辑秒杀 |
| 161 | DELETE | /api/admin/flash-sales/{saleId} | 管理员 | 删除秒杀 |
| 162 | PATCH | /api/admin/flash-sales/{saleId}/enable | 管理员 | 启用秒杀 |
| 163 | PATCH | /api/admin/flash-sales/{saleId}/disable | 管理员 | 禁用秒杀 |
| 164 | GET | /api/public/activities | 公开 | 活动列表 |
| 165 | GET | /api/public/activities/{activityId} | 公开 | 活动详情 |
| 166 | GET | /api/admin/activities | 管理员 | 活动列表 |
| 167 | POST | /api/admin/activities | 管理员 | 创建活动 |
| 168 | PUT | /api/admin/activities/{activityId} | 管理员 | 编辑活动 |
| 169 | DELETE | /api/admin/activities/{activityId} | 管理员 | 删除活动 |
| 170 | POST | /api/admin/activities/{activityId}/products | 管理员 | 添加活动商品 |
| 171 | DELETE | /api/admin/activities/{activityId}/products/{productId} | 管理员 | 移除活动商品 |
| 172 | GET | /api/public/rankings/consumption | 公开 | 消费排行榜 |
| 173 | GET | /api/public/rankings/sign-in | 公开 | 签到排行榜 |

**总计 173 个 API 接口。**
