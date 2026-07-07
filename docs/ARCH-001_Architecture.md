# ARCH-001 ShoppingDream 架构设计文档

> **Version**: v1.0.0 | **Status**: Draft | **Date**: 2026-07-03
> **SSOT**: 本文为 ShoppingDream 技术架构的唯一权威来源。

---

## 目录

- [01 架构总览](#01-架构总览)
- [02 后端架构](#02-后端架构)
- [03 前端架构](#03-前端架构)
- [04 公共规范](#04-公共规范)
- [05 安全架构](#05-安全架构)
- [06 部署架构](#06-部署架构)
- [07 技术决策](#07-技术决策)

---

## 01 架构总览

### 1.1 系统架构图

```
┌─────────────────────────────────────────────────────────────────────────┐
│                        ShoppingDream 系统架构                           │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐                 │
│  │  User Web    │  │ Merchant Web │  │  Admin Web   │                 │
│  │  (Vue 3)     │  │  (Vue 3)     │  │  (Vue 3)     │                 │
│  │  :5173       │  │  :5174       │  │  :5175       │                 │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘                 │
│         │                 │                 │                          │
│         └────────┬────────┴────────┬────────┘                          │
│                  │   HTTP/REST     │                                    │
│                  ▼                 ▼                                    │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │                    API Gateway (Nginx)                          │   │
│  │                    :80 → 反向代理                               │   │
│  └─────────────────────────────┬───────────────────────────────────┘   │
│                                │                                        │
│                                ▼                                        │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │                   Spring Boot Application                       │   │
│  │                   :8080                                         │   │
│  │  ┌─────────────────────────────────────────────────────────┐    │   │
│  │  │                  Interfaces Layer                        │    │   │
│  │  │  (REST Controllers, Exception Handler, Interceptors)     │    │   │
│  │  └────────────────────────┬────────────────────────────────┘    │   │
│  │  ┌────────────────────────▼────────────────────────────────┐    │   │
│  │  │                  Application Layer                       │    │   │
│  │  │  (Application Services, DTO Assemblers, Orchestration)   │    │   │
│  │  └────────────────────────┬────────────────────────────────┘    │   │
│  │  ┌────────────────────────▼────────────────────────────────┐    │   │
│  │  │                    Domain Layer                          │    │   │
│  │  │  (Aggregates, Entities, Value Objects, Domain Services,  │    │   │
│  │  │   Domain Events, Repositories Interfaces)                │    │   │
│  │  └────────────────────────┬────────────────────────────────┘    │   │
│  │  ┌────────────────────────▼────────────────────────────────┐    │   │
│  │  │               Infrastructure Layer                       │    │   │
│  │  │  (Repository Impls, MyBatis-Plus Mappers, Redis,         │    │   │
│  │  │   Mock Services, File Storage, Security)                 │    │   │
│  │  └─────────────────────────────────────────────────────────┘    │   │
│  └─────────────────────────────────────────────────────────────────┘   │
│         │                  │                  │                        │
│         ▼                  ▼                  ▼                        │
│  ┌────────────┐    ┌──────────────┐   ┌────────────┐                  │
│  │  MySQL 8   │    │   Redis 7    │   │ Local FS   │                  │
│  │  :3306     │    │   :6379      │   │ /uploads   │                  │
│  └────────────┘    └──────────────┘   └────────────┘                  │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

### 1.2 分层职责

| 层 | 职责 | 依赖方向 |
|---|---|---|
| Interfaces | 接收请求、参数校验、调用 Application Service、返回结果 | → Application |
| Application | 编排领域服务、事务管理、DTO 转换、领域事件发布 | → Domain |
| Domain | 核心业务逻辑、聚合根、实体、值对象、领域服务、领域事件 | 无外部依赖（纯 Java） |
| Infrastructure | 技术实现：数据库访问、缓存、消息队列、Mock 服务、文件存储 | → Domain（实现接口） |

### 1.3 领域划分

```
sim-shopping/
├── backend/                          # 后端 Spring Boot 项目
│   └── src/main/java/com/sim/shopping/
│       ├── interfaces/               # 接口层
│       │   ├── rest/                 # REST Controllers
│       │   │   ├── user/             # 用户端 API
│       │   │   ├── merchant/         # 商家端 API
│       │   │   └── admin/            # 管理后台 API
│       │   ├── dto/                  # 数据传输对象
│       │   │   ├── request/          # 请求 DTO
│       │   │   └── response/         # 响应 DTO
│       │   ├── assembler/            # DTO ↔ Domain 转换器
│       │   ├── advice/               # 全局异常处理
│       │   ├── filter/               # 过滤器
│       │   └── interceptor/          # 拦截器
│       ├── application/              # 应用层
│       │   ├── service/              # Application Service
│       │   │   ├── user/
│       │   │   ├── merchant/
│       │   │   ├── shop/
│       │   │   ├── product/
│       │   │   ├── cart/
│       │   │   ├── order/
│       │   │   ├── payment/
│       │   │   ├── shipment/
│       │   │   ├── logistics/
│       │   │   ├── review/
│       │   │   ├── marketing/
│       │   │   └── system/
│       │   ├── event/                # 领域事件处理器
│       │   └── scheduler/            # 定时任务
│       ├── domain/                   # 领域层
│       │   ├── user/                 # 用户领域
│       │   │   ├── model/            # 聚合根、实体、值对象
│       │   │   ├── service/          # 领域服务
│       │   │   ├── event/            # 领域事件
│       │   │   └── repository/       # Repository 接口
│       │   ├── merchant/             # 商家领域
│       │   ├── shop/                 # 店铺领域
│       │   ├── product/              # 商品领域
│       │   ├── cart/                 # 购物车领域
│       │   ├── order/                # 订单领域
│       │   ├── payment/              # 支付领域
│       │   ├── shipment/             # 发货领域
│       │   ├── logistics/            # 物流领域
│       │   ├── review/               # 评价领域
│       │   ├── marketing/            # 营销领域
│       │   ├── system/               # 系统领域
│       │   └── notification/         # 站内消息领域
│       ├── infrastructure/           # 基础设施层
│       │   ├── persistence/          # 持久化实现
│       │   │   ├── converter/        # DO ↔ Domain 转换
│       │   │   ├── entity/           # 数据库实体 (DO)
│       │   │   ├── mapper/           # MyBatis-Plus Mapper
│       │   │   └── repository/       # Repository 实现
│       │   ├── cache/                # Redis 缓存
│       │   ├── mock/                 # Mock 服务实现
│       │   │   ├── MockPaymentGateway.java
│       │   │   └── MockLogisticsGateway.java
│       │   ├── storage/              # 文件存储
│       │   ├── security/             # 安全实现
│       │   │   ├── JwtTokenProvider.java
│       │   │   ├── JwtAuthenticationFilter.java
│       │   │   └── UserDetailsServiceImpl.java
│       │   └── config/               # 配置类
│       └── SimShoppingApplication.java  # 启动类
├── frontend/
│   ├── user-web/                     # 用户端
│   ├── merchant-web/                 # 商家端
│   └── admin-web/                    # 管理后台
├── docker/                           # Docker 配置
│   ├── docker-compose.yml
│   ├── mysql/
│   ├── redis/
│   └── nginx/
├── docs/                             # 项目文档
└── PROGRESS.md                       # 进度追踪
```

---

## 02 后端架构

### 2.1 项目结构（Maven 多模块 vs 单模块）

**决策：单模块 + 包分层**

理由：
- V1 是单体应用，不需要微服务拆分
- DDD 分层通过包结构体现，而非 Maven 模块
- 减少模块间依赖管理的复杂度
- 12 个领域通过包隔离，领域内高内聚

### 2.2 领域包结构规范

每个领域遵循统一结构：

```
domain/{domain-name}/
├── model/
│   ├── {Aggregate}Root.java          # 聚合根
│   ├── {Entity}.java                 # 实体
│   ├── {ValueObject}.java            # 值对象
│   └── {Enum}.java                   # 枚举（状态机等）
├── service/
│   └── {Domain}Service.java          # 领域服务
├── event/
│   └── {EventName}.java              # 领域事件
└── repository/
    └── {Aggregate}Repository.java    # Repository 接口
```

### 2.3 各领域核心聚合

| 领域 | 聚合根 | 实体 | 值对象 | 状态机 |
|---|---|---|---|---|
| User | User | — | Address, UserProfile | ACTIVE / DISABLED |
| Merchant | Merchant | — | MerchantProfile | PENDING / APPROVED / ACTIVE / DISABLED |
| Shop | Shop | — | ShopInfo | ACTIVE / DISABLED |
| Product | Product | ProductImage, ProductSku, Brand | ProductStatus, Price, SkuAttributes | DRAFT / PUBLISHED / OFFLINE / DELETED |
| Cart | ShoppingCart | CartItem | — | — |
| Order | Order | OrderItem | OrderStatus, Address | CREATED / PAID / SHIPPED / IN_TRANSIT / DELIVERED / COMPLETED / CANCELLED |
| Payment | Payment | — | PaymentMethod | PENDING / SUCCESS / FAILED / CLOSED |
| Shipment | Shipment | — | — | PENDING / PACKED / SHIPPED |
| Logistics | LogisticsRecord | LogisticsTrack | — | CREATED / PICKED_UP / SORTING / IN_TRANSIT / OUT_FOR_DELIVERY / DELIVERED |
| Review | Review | ReviewImage | — | CREATED / VISIBLE / HIDDEN / DELETED |
| Marketing | SignInRecord | — | SignInResult | — |
| System | — | — | — | — (系统管理无聚合根，直接操作其他领域) |
| Notification | — | SysNotification | — | — (站内消息，附属于 System 领域) |

> **实现说明（与代码一致）**：V1 当前实现以 `application/*Service` 编排业务逻辑，`infrastructure/persistence/entity/*DO + mapper` 负责持久化；`domain/` 目前主要承载 **业务异常** 与 **领域事件**（如 `OrderCreatedEvent`）。完整的“聚合根/实体/值对象/Repository 接口”领域模型，会在后续重构中逐步落地，以降低一次性 DDD 落地成本。

### 2.4 Application Service 规范

```java
// 应用服务负责编排领域服务，不包含业务逻辑
@Service
@Transactional
public class OrderApplicationService {

    private final OrderDomainService orderDomainService;
    private final ProductRepository productRepository;
    private final ShoppingCartRepository cartRepository;

    public OrderCreateResponse createOrder(Long userId, OrderCreateRequest request) {
        // 1. 获取领域对象
        ShoppingCart cart = cartRepository.findByUserId(userId);
        List<Product> products = productRepository.findByIds(request.getProductIds());

        // 2. 调用领域服务（核心业务逻辑在这里）
        Order order = orderDomainService.createOrder(cart, products, request.getAddressId());

        // 3. 持久化（通过 Repository）
        orderRepository.save(order);

        // 4. 发布领域事件
        eventPublisher.publish(new OrderCreatedEvent(order));

        // 5. 返回 DTO
        return OrderAssembler.toResponse(order);
    }
}
```

### 2.5 DTO 规范

```
dto/
├── request/         # 入参 DTO
│   ├── user/
│   │   ├── UserRegisterRequest.java
│   │   └── UserLoginRequest.java
│   ├── order/
│   │   └── OrderCreateRequest.java
│   └── ...
├── response/        # 出参 DTO
│   ├── user/
│   │   ├── UserLoginResponse.java
│   │   └── UserInfoResponse.java
│   ├── order/
│   │   └── OrderDetailResponse.java
│   └── ...
└── common/          # 公共 DTO
    ├── PageResponse.java
    └── ApiResponse.java
```

### 2.6 统一响应格式

```java
// 统一 API 响应包装
public class ApiResponse<T> {
    private int code;        // 200=成功, 400=参数错误, 401=未认证, 403=无权限, 500=系统错误
    private String message;
    private T data;
    private long timestamp;

    public static <T> ApiResponse<T> success(T data) { ... }
    public static ApiResponse<Void> success() { ... }
    public static <T> ApiResponse<T> error(int code, String message) { ... }
}

// 分页响应
public class PageResponse<T> {
    private List<T> list;
    private long total;
    private int page;
    private int size;
    private int totalPages;
}
```

### 2.7 异常体系

```
RuntimeException
└── BusinessException (业务异常基类)
    ├── UserException
    │   ├── UserNotFoundException
    │   ├── UserAlreadyExistsException
    │   └── UserDisabledException
    ├── MerchantException
    │   ├── MerchantNotFoundException
    │   └── MerchantNotApprovedException
    ├── ProductException
    │   ├── ProductNotFoundException
    │   ├── ProductNotPublishedException
    │   └── InsufficientStockException
    ├── OrderException
    │   ├── OrderNotFoundException
    │   ├── OrderStatusException      # 状态不允许操作
    │   └── OrderOwnershipException   # 非本人订单
    ├── PaymentException
    │   ├── PaymentNotFoundException
    │   └── PaymentFailedException
    ├── CartException
    │   └── CartItemLimitException
    └── ReviewException
        ├── ReviewNotFoundException
        └── ReviewAlreadyExistsException
```

全局异常处理：

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusiness(BusinessException e) {
        return ResponseEntity.ok(ApiResponse.error(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
            .map(f -> f.getField() + ": " + f.getDefaultMessage())
            .collect(Collectors.joining(", "));
        return ResponseEntity.ok(ApiResponse.error(400, msg));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleSystem(Exception e) {
        log.error("系统异常", e);
        return ResponseEntity.ok(ApiResponse.error(500, "系统内部错误"));
    }
}
```

### 2.8 领域事件

| 事件 | 发布时机 | 消费者 | 动作 |
|---|---|---|---|
| OrderCreated | 创建订单后 | 库存服务 | 锁定库存 |
| PaymentSucceeded | 支付成功后 | 通知服务 | 发送支付成功通知 |
| ShipmentCreated | 发货创建后 | 物流服务 | 创建物流单 |
| LogisticsDelivered | 物流签收后 | 订单服务 | 更新订单状态为 DELIVERED |
| OrderCompleted | 订单完成后 | 评价服务 | 引导评价 |
| OrderCancelled | 订单取消后 | 库存服务 | 释放库存 |
| ReviewCreated | 评价创建后 | 商品服务 | 更新商品评分 |
| NotificationNeeded | 业务事件触发后 | 通知服务 | 发送站内消息 |

V1 使用 Spring ApplicationEvent 实现领域事件（进程内同步/异步），不引入消息队列。

---

## 03 前端架构

### 3.1 三端共用技术栈

| 技术 | 版本 | 用途 |
|---|---|---|
| Vue | 3.x (Composition API) | UI 框架 |
| TypeScript | 5.x | 类型安全 |
| Vite | 5.x | 构建工具 |
| Element Plus | 最新稳定 | UI 组件库 |
| Pinia | 2.x | 状态管理 |
| Vue Router | 4.x | 路由 |
| Axios | 1.x | HTTP 客户端 |
| ESLint + Prettier | 最新 | 代码规范 |

### 3.2 用户端结构 (user-web)

```
frontend/user-web/
├── public/
├── src/
│   ├── api/                    # API 请求
│   │   ├── modules/
│   │   │   ├── user.ts
│   │   │   ├── product.ts
│   │   │   ├── cart.ts
│   │   │   ├── order.ts
│   │   │   ├── payment.ts
│   │   │   ├── logistics.ts
│   │   │   ├── review.ts
│   │   │   ├── marketing.ts
│   │   │   └── shop.ts
│   │   ├── types/              # 接口类型定义
│   │   └── request.ts          # Axios 实例 + 拦截器
│   ├── assets/                 # 静态资源
│   ├── components/             # 通用组件
│   │   ├── common/             # 基础组件（Header, Footer, Pagination）
│   │   ├── product/            # 商品相关组件
│   │   └── order/              # 订单相关组件
│   ├── composables/            # 组合式函数
│   │   ├── useAuth.ts
│   │   ├── useCart.ts
│   │   └── usePagination.ts
│   ├── layouts/                # 布局组件
│   │   ├── DefaultLayout.vue
│   │   └── BlankLayout.vue
│   ├── router/                 # 路由
│   │   ├── index.ts
│   │   ├── routes.ts
│   │   └── guards.ts           # 路由守卫
│   ├── stores/                 # Pinia 状态
│   │   ├── user.ts
│   │   ├── cart.ts
│   │   └── app.ts
│   ├── views/                  # 页面组件
│   │   ├── home/
│   │   ├── category/
│   │   ├── search/
│   │   ├── product/
│   │   ├── shop/
│   │   ├── cart/
│   │   ├── checkout/
│   │   ├── payment/
│   │   ├── order/
│   │   ├── user/
│   │   └── auth/
│   ├── utils/                  # 工具函数
│   ├── App.vue
│   └── main.ts
├── index.html
├── vite.config.ts
├── tsconfig.json
└── package.json
```

### 3.3 商家端结构 (merchant-web)

```
frontend/merchant-web/
├── src/
│   ├── api/
│   │   ├── modules/
│   │   │   ├── merchant.ts
│   │   │   ├── shop.ts
│   │   │   ├── product.ts
│   │   │   ├── order.ts
│   │   │   ├── shipment.ts
│   │   │   ├── logistics.ts
│   │   │   ├── review.ts
│   │   │   └── statistics.ts
│   │   ├── types/
│   │   └── request.ts
│   ├── components/
│   │   ├── common/
│   │   └── product/
│   ├── layouts/
│   │   └── MerchantLayout.vue    # 左侧菜单 + 顶部导航
│   ├── router/
│   ├── stores/
│   │   ├── merchant.ts
│   │   └── app.ts
│   ├── views/
│   │   ├── dashboard/
│   │   ├── shop/
│   │   ├── product/
│   │   ├── order/
│   │   ├── logistics/
│   │   ├── review/
│   │   ├── marketing/
│   │   ├── data/
│   │   └── settings/
│   ├── utils/
│   ├── App.vue
│   └── main.ts
├── index.html
├── vite.config.ts
├── tsconfig.json
└── package.json
```

### 3.4 管理后台结构 (admin-web)

```
frontend/admin-web/
├── src/
│   ├── api/
│   │   ├── modules/
│   │   │   ├── system.ts         # 用户/商家/商品管理
│   │   │   ├── order.ts
│   │   │   ├── review.ts
│   │   │   ├── marketing.ts
│   │   │   ├── ai.ts
│   │   │   └── statistics.ts
│   │   ├── types/
│   │   └── request.ts
│   ├── components/
│   │   └── common/
│   ├── layouts/
│   │   └── AdminLayout.vue       # 左侧菜单 + 顶部导航 + 标签页
│   ├── router/
│   ├── stores/
│   │   ├── admin.ts
│   │   └── app.ts
│   ├── views/
│   │   ├── dashboard/
│   │   ├── user/
│   │   ├── merchant/
│   │   ├── shop/
│   │   ├── product/
│   │   ├── order/
│   │   ├── review/
│   │   ├── marketing/
│   │   ├── ai/
│   │   ├── system/
│   │   └── statistics/
│   ├── utils/
│   ├── App.vue
│   └── main.ts
├── index.html
├── vite.config.ts
├── tsconfig.json
└── package.json
```

### 3.5 前端公共规范

#### API 请求封装

```typescript
// request.ts — Axios 实例
const request = axios.create({
  baseURL: '/api',
  timeout: 15000,
})

// 请求拦截器：自动添加 Token
request.interceptors.request.use((config) => {
  const token = useUserStore().token
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

// 响应拦截器：统一处理 ApiResponse
request.interceptors.response.use((response) => {
  const { code, message, data } = response.data
  if (code === 200) return data
  if (code === 401) { /* 跳转登录 */ }
  ElMessage.error(message)
  return Promise.reject(new Error(message))
})
```

#### 路由守卫规范

```typescript
// guards.ts
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  
  if (to.meta.requiresAuth && !userStore.token) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
  } else if (to.meta.roles && !to.meta.roles.includes(userStore.role)) {
    next({ name: 'Forbidden' })
  } else {
    next()
  }
})
```

#### 状态管理规范

```typescript
// stores/user.ts
export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref<UserInfo | null>(null)
  const role = computed(() => userInfo.value?.role || 'guest')

  async function login(credentials: LoginRequest) { ... }
  async function fetchUserInfo() { ... }
  function logout() { ... }

  return { token, userInfo, role, login, fetchUserInfo, logout }
}, {
  persist: true,  // pinia-plugin-persistedstate
})
```

---

## 04 公共规范

### 4.1 命名规范

| 类型 | 规范 | 示例 |
|---|---|---|
| Java 类 | PascalCase | `OrderApplicationService` |
| Java 方法 | camelCase | `createOrder` |
| Java 变量 | camelCase | `orderNo` |
| Java 常量 | UPPER_SNAKE | `MAX_CART_ITEM_COUNT` |
| 数据库表 | snake_case | `t_order`, `t_order_item` |
| 数据库字段 | snake_case | `order_no`, `created_at` |
| REST URL | kebab-case | `/api/products`, `/api/orders/{orderNo}` |
| Vue 组件 | PascalCase | `ProductCard.vue` |
| TS 变量 | camelCase | `productList` |
| CSS 类 | kebab-case | `.product-card` |

### 4.2 数据库表命名

| 规则 | 示例 |
|---|---|
| 业务表前缀 `t_` | `t_user`, `t_product`, `t_order` |
| 关联表前缀 `t_rel_` | `t_rel_product_category` |
| 系统表前缀 `t_sys_` | `t_sys_menu`, `t_sys_dict` |
| 字段命名 | `created_at`, `updated_at`, `deleted_at`, `created_by`, `updated_by` |
| 软删除 | `deleted` (TINYINT, 0=未删除, 1=已删除) |
| 状态字段 | `status` (VARCHAR, 大写枚举值) |

### 4.3 REST API 规范

| 操作 | HTTP Method | URL | 示例 |
|---|---|---|---|
| 列表查询 | GET | `/api/{resource}` | `GET /api/products?page=1&size=20` |
| 详情查询 | GET | `/api/{resource}/{id}` | `GET /api/products/123` |
| 创建 | POST | `/api/{resource}` | `POST /api/orders` |
| 更新 | PUT | `/api/{resource}/{id}` | `PUT /api/users/1` |
| 删除 | DELETE | `/api/{resource}/{id}` | `DELETE /api/reviews/1` |
| 状态变更 | PATCH | `/api/{resource}/{id}/{action}` | `PATCH /api/products/1/publish` |
| 自定义动作 | POST | `/api/{resource}/{id}/{action}` | `POST /api/orders/SD001/pay` |

#### URL 分组前缀

| 前缀 | 说明 | 认证要求 |
|---|---|---|
| `/api/public/**` | 公开接口 | 无需认证 |
| `/api/user/**` | 用户端接口 | 用户登录 |
| `/api/merchant/**` | 商家端接口 | 商家登录 |
| `/api/admin/**` | 管理后台接口 | 管理员登录 |
| `/api/common/**` | 三端共用接口 | 登录（任意角色） |

#### 分页参数

| 参数 | 默认值 | 说明 |
|---|---|---|
| page | 1 | 页码，从 1 开始 |
| size | 20 | 每页条数，最大 100 |
| sort | created_at,desc | 排序字段,方向 |

### 4.4 枚举值规范

所有枚举值使用**大写下划线**格式，与 PRD 状态机定义一致：

```java
public enum OrderStatus {
    CREATED, PAID, SHIPPED, IN_TRANSIT, DELIVERED, COMPLETED, CANCELLED
}
```

### 4.5 日志规范

```java
// 使用 SLF4J
private static final Logger log = LoggerFactory.getLogger(OrderService.class);

// 日志级别
log.debug("查询订单列表, userId={}, page={}", userId, page);
log.info("订单创建成功, orderNo={}, userId={}", orderNo, userId);
log.warn("库存不足, productId={}, requested={}, available={}", productId, requested, available);
log.error("支付处理异常, orderNo={}", orderNo, e);
```

### 4.6 时间规范

- 数据库存储 `DATETIME`（MySQL 本地时间）
- Java 中使用 `LocalDateTime`
- API 返回 ISO 8601 格式：`2026-07-03T12:00:00`
- 时区：Asia/Shanghai

---

## 05 安全架构

### 5.1 认证方案

**JWT Token + Spring Security**

```
登录流程:
  Client → POST /api/common/login {username, password}
  Server → 验证密码 (BCrypt)
  Server → 生成 JWT (userId, role, 24h expiry)
  Server → 返回 {token, userInfo}

后续请求:
  Client → GET /api/user/orders, Header: Authorization: Bearer {token}
  Server → JwtAuthenticationFilter 解析 Token
  Server → SecurityContext 设置 Authentication
  Server → Controller 处理请求
```

### 5.2 RBAC 权限模型

```
角色层级:
  ROLE_SUPER_ADMIN > ROLE_ADMIN > ROLE_MERCHANT > ROLE_USER > ROLE_GUEST

RBAC 表结构:
  t_sys_role (角色表) ── t_sys_role_permission (角色权限关联) ── t_sys_permission (权限表)
  t_sys_role (角色表) ── t_sys_role_menu (角色菜单关联) ── t_sys_menu (菜单表)

权限规则:
  - SUPER_ADMIN 可访问所有接口
  - ADMIN 可访问 /api/admin/** 和 /api/common/**
  - MERCHANT 可访问 /api/merchant/** 和 /api/user/** 和 /api/common/**
  - USER 可访问 /api/user/** 和 /api/common/**
  - GUEST 可访问 /api/public/** 和 /api/common/login, /api/common/register

前端权限控制:
  - 路由守卫: meta.roles 控制页面访问
  - v-permission 指令: 按钮级权限控制
  - 菜单动态渲染: 根据角色权限渲染菜单
```

### 5.3 数据隔离

| 隔离维度 | 实现方式 |
|---|---|
| 用户数据隔离 | 查询条件自动加 `user_id = currentUser.id` |
| 商家数据隔离 | 查询条件自动加 `merchant_id = currentUser.merchantId` |
| 管理员数据隔离 | 无限制，可查看全平台数据 |

### 5.4 接口安全

| 安全措施 | 实现方式 |
|---|---|
| XSS 防护 | 输入过滤 + 输出转义 |
| CSRF 防护 | JWT 无状态，天然免疫 CSRF |
| SQL 注入 | MyBatis-Plus 参数化查询 |
| 接口限流 | Redis + 拦截器，V1 简单实现（IP 维度，60 次/分钟） |
| 文件上传 | 白名单后缀（jpg/png/gif/webp），大小限制 5MB |
| 密码存储 | BCrypt（strength=10） |

---

## 06 部署架构

### 6.1 Docker Compose 编排

```yaml
# docker-compose.yml
services:
  mysql:        # MySQL 8.0, port 3306
  redis:        # Redis 7.0, port 6379
  backend:      # Spring Boot, port 8080
  user-web:     # Nginx serving Vue build, port 80 → 5173
  merchant-web: # Nginx serving Vue build, port 81 → 5174
  admin-web:    # Nginx serving Vue build, port 82 → 5175
  nginx:        # 反向代理, port 80
```

### 6.2 环境配置

| 环境 | 配置文件 | 说明 |
|---|---|---|
| 开发 | `application-dev.yml` | 本地 MySQL/Redis，热重载 |
| 生产 | `application-prod.yml` | Docker MySQL/Redis，日志文件 |

### 6.3 端口规划

| 服务 | 开发端口 | 生产端口 |
|---|---|---|
| MySQL | 3306 | 3306 (内部) |
| Redis | 6379 | 6379 (内部) |
| Backend API | 8080 | 8080 (内部) |
| User Web | 5173 | 80 (Nginx) |
| Merchant Web | 5174 | 81 (Nginx) |
| Admin Web | 5175 | 82 (Nginx) |
| Nginx (总入口) | — | 80 |

---

## 07 技术决策

### ADR-001: DDD 领域分层

**决策**：后端采用 DDD 四层架构（Interfaces / Application / Domain / Infrastructure）

**理由**：
- PRD 明确定义 12 个领域，DDD 天然契合
- 领域层无外部依赖，业务逻辑可测试
- Repository 接口在领域层，实现在基础设施层，可替换
- 为 V2 微服务化预留拆分边界

**权衡**：
- 比传统 MVC 结构复杂，代码量更多
- 需要 Converter 在 DO/Entity/DTO 之间转换
- V1 单体应用下有过度设计风险，但长期收益 > 短期成本

### ADR-002: 领域事件使用 Spring ApplicationEvent

**决策**：V1 使用 Spring ApplicationEvent（进程内事件），不引入消息队列

**理由**：
- V1 单体应用，不需要跨进程事件
- Spring ApplicationEvent 支持同步和 @Async 异步
- 减少基础设施复杂度
- V2 拆分微服务时替换为 RabbitMQ/Kafka

### ADR-003: Mock 服务通过接口抽象实现

**决策**：支付和物流的 Mock 通过 Gateway 接口 + Mock 实现类

```java
// 领域层定义接口
public interface PaymentGateway {
    PaymentResult pay(PaymentRequest request);
}

// 基础设施层实现 Mock
@Component
public class MockPaymentGateway implements PaymentGateway {
    @Override
    public PaymentResult pay(PaymentRequest request) {
        return PaymentResult.success("mock-txn-" + System.currentTimeMillis());
    }
}
```

**理由**：
- V2 只需新增 `AlipayGateway` 实现，切换 @Primary 注解即可
- 领域层不感知实现细节
- 符合依赖倒置原则

### ADR-004: 前端三端独立仓库

**决策**：user-web / merchant-web / admin-web 三个独立 Vue 项目

**理由**：
- 三端权限/路由/组件差异大，合并增加复杂度
- 独立部署灵活
- Vite 多项目构建快，不需要 Monorepo 的依赖管理

**权衡**：
- 公共组件和工具函数需要复制（V1 量小可接受）
- V2 可提取 @sim/shared 包共享

### ADR-005: 单 Maven 模块 + 包分层

**决策**：后端不拆分 Maven 模块，通过包结构隔离领域

**理由**：
- V1 单体部署，多模块增加构建复杂度无实际收益
- 包结构足够隔离：`domain.order` 与 `domain.product` 无交叉
- V2 微服务拆分时，按领域包自然切分

### ADR-006: MyBatis-Plus 而非 JPA

**决策**：持久层使用 MyBatis-Plus

**理由**：
- PRD 指定技术栈
- MyBatis-Plus 提供代码生成、分页、逻辑删除开箱即用
- SQL 可控性优于 JPA
- 国内生态成熟

### ADR-007: JWT 无状态认证

**决策**：使用 JWT Token，不使用 Session

**理由**：
- 无状态，水平扩展无需 Session 共享
- 三端统一认证方案
- Token 24h 有效期 + 刷新机制
- 退出登录通过 Redis 黑名单实现（V1 可选）

---

## 附录：项目依赖清单

### 后端 Maven 依赖

| 依赖 | 版本 | 用途 |
|---|---|---|
| spring-boot-starter-web | 3.x | Web 框架 |
| spring-boot-starter-security | 3.x | 安全框架 |
| spring-boot-starter-validation | 3.x | 参数校验 |
| mybatis-plus-spring-boot3-starter | 3.5.x+ | ORM |
| mysql-connector-j | 8.x | MySQL 驱动 |
| spring-boot-starter-data-redis | 3.x | Redis 客户端 |
| jjwt-api + jjwt-impl + jjwt-jackson | 0.12.x | JWT |
| springdoc-openapi-starter-webmvc-ui | 2.x | API 文档 |
| hutool-all | 5.x | 工具库 |

### 前端 npm 依赖

| 依赖 | 版本 | 用途 |
|---|---|---|
| vue | 3.x | UI 框架 |
| typescript | 5.x | 类型系统 |
| vite | 5.x | 构建工具 |
| element-plus | 最新 | UI 组件 |
| pinia | 2.x | 状态管理 |
| vue-router | 4.x | 路由 |
| axios | 1.x | HTTP 客户端 |
| pinia-plugin-persistedstate | 最新 | 状态持久化 |
| @element-plus/icons-vue | 最新 | 图标 |
| dayjs | 最新 | 日期处理 |
