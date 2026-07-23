# 模拟商城（Sim-Shopping）

> **重要声明**：本系统是一个纯模拟娱乐的商城系统，不涉及任何真实商品、真实货币、真实物流。所有商品、交易、支付、物流等数据均为虚拟模拟，仅供学习与娱乐体验使用，不产生任何实际消费或交易行为。

---

## 项目概述

模拟商城是一个功能完整的模拟娱乐 B2C 电商平台，涵盖用户端购物、商家端管理、管理后台运营三大子系统。用户可免登录浏览商品、搜索、分类筛选，登录后可体验完整的模拟购物流程（加购、选品结算、下单、模拟支付、模拟物流追踪、评价）。商家可管理店铺商品和订单，管理员可进行全平台运营管理。系统还包含优惠券、积分商城、秒杀抢购、专题活动、排行榜等游戏化娱乐功能，所有数值均为模拟数据。

---

## 技术栈

### 后端

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 17 | JDK 17 LTS |
| Spring Boot | 3.2.5 | 核心框架 |
| Spring Security | 6.2.4 | 认证与授权（JWT） |
| MyBatis-Plus | 3.5.6 | ORM 框架 |
| MySQL | 8.x | 关系型数据库 |
| Redis | 7.x | 缓存（支持 Redis / 数据库双模式切换） |
| Jackson | 2.x | JSON 序列化（JSR310 时区 Asia/Shanghai） |
| Spring AOP | 6.1.6 | 日志与权限切面 |
| Hutool | 5.8.27 | 工具类库 |
| JJWT | 0.12.5 | JWT Token 生成与校验 |
| SpringDoc OpenAPI | 2.3.0 | API 文档（Swagger UI） |

### 前端

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue 3 | 3.4 | 渐进式框架 |
| TypeScript | 5.4 | 类型安全 |
| Element Plus | 2.7 | UI 组件库 |
| Vite | 5.2 | 构建工具 |
| Pinia | 2.1 | 状态管理 |
| Vue Router | 4.3 | 前端路由（History 模式） |
| Axios | 1.x | HTTP 客户端 |
| DOMPurify | 3.x | XSS 防护 |
| Sass | 1.75 | CSS 预处理器 |

### 部署

| 技术 | 说明 |
|------|------|
| Apache Tomcat | 10.1.x（WAR 部署） |
| Docker + Compose | 容器化部署（MySQL + Redis + Backend + 3 Frontend + Nginx） |
| Nginx | 反向代理与静态资源服务 |

---

## 项目结构

```
sim-shopping/
├── backend/                        # 后端服务（Spring Boot WAR）
│   ├── src/main/java/com/sim/shopping/
│   │   ├── application/            #   应用层（Service、事件监听、调度任务）
│   │   ├── domain/                 #   领域层（领域事件、异常）
│   │   ├── interfaces/             #   接口层（Controller + DTO + 参数校验）
│   │   │   ├── auth/               #     认证接口
│   │   │   ├── user/               #     用户端接口
│   │   │   ├── merchant/           #     商家端接口
│   │   │   ├── system/             #     系统管理接口
│   │   │   └── dto/                #     数据传输对象
│   │   └── infrastructure/          #   基础设施层
│   │       ├── persistence/         #     持久化（Mapper、Entity）
│   │       ├── security/           #     安全（JWT、SecurityConfig）
│   │       ├── cache/              #     缓存（Redis/Database 双实现）
│   │       └── config/             #     配置类
│   └── pom.xml
├── frontend/
│   ├── user-web/                   # 用户端（消费者）
│   ├── merchant-web/               # 商家端（店铺管理）
│   └── admin-web/                  # 管理后台（平台运营）
├── sql/
│   ├── init.sql                    # 数据库初始化脚本（建表 + 测试账号 + 初始数据）
│   ├── init_products_full.sql      # 288 个商品初始数据（24 分类 x 12）
│   └── init_other_data.sql         # 活动、秒杀、优惠券等营销数据
├── docker/
│   └── nginx/
│       ├── nginx.conf              # 完整版 Nginx 配置（三个前端路由）
│       └── nginx.minimal.conf      # 精简版 Nginx 配置（仅用户端）
├── docs/                           # 设计文档
├── docker-compose.yml              # Docker Compose 完整编排
├── docker-compose.local-db.yml     # Docker Compose（使用宿主机 MySQL）
├── docker-compose.minimal.yml      # Docker Compose 精简版（1GiB 内存）
└── README.md                       # 本文件
```

---

## 后端架构（DDD 四层架构）

```
┌─────────────────────────────────────┐
│  interfaces（接口层）                │  Controller + DTO + 参数校验
│                                     │  只注入 Service，不直接操作 Mapper
├─────────────────────────────────────┤
│  application（应用层）               │  Service + 事件监听 + 调度任务 + 事务编排
│                                     │  返回 DTO，不暴露 DO
├─────────────────────────────────────┤
│  domain（领域层）                    │  领域事件 + 值对象 + 异常
├─────────────────────────────────────┤
│  infrastructure（基础设施层）        │  Mapper + Entity + Redis + JWT + 配置
│                                     │  不依赖 interfaces 层，保持依赖方向正确
└─────────────────────────────────────┘
```

**架构规范：**
- Controller 只注入 Service，禁止直接注入 Mapper
- Service 返回 DTO，不直接暴露 DO 给 Controller
- Mapper 返回 DO 或内部查询结果类，不返回 interfaces 层 DTO
- DO 与 DTO 转换在 Service 层完成

---

## 功能模块

### 用户端（user-web）

| 模块 | 功能 |
|------|------|
| 首页 | Banner 轮播、推荐商品、热门商品、新品上市、商品分类导航 |
| 导航栏 | 顶部活动导航（限时秒杀、积分商城、领券中心、专题活动、排行榜）、购物车角标 |
| 商品浏览 | 商品列表、商品详情、SKU 选择、收藏商品、浏览历史 |
| 搜索 | 关键词搜索、搜索建议、热门搜索词、搜索历史、分类筛选、价格区间 |
| 分类 | 一级/二级分类树、分类商品列表 |
| 店铺 | 店铺详情、店铺商品列表 |
| 购物车 | 添加/删除/修改数量、多选/全选、选择结算 |
| 结算 | 按选中商品结算、地址选择、优惠券选择、运费计算 |
| 订单 | 创建订单、订单列表（多状态 Tab）、订单详情、取消订单、确认收货、物流追踪 |
| 支付 | 模拟支付（支持支付宝/微信/余额） |
| 评价 | 提交评价（支持图片上传）、查看评价、我的评价 |
| 地址管理 | 收货地址 CRUD（省市区级联选择器） |
| 签到 | 每日签到赚积分 |
| 优惠券 | 领券中心、我的优惠券（适用范围中文展示） |
| 积分商城 | 积分商品兑换、积分明细 |
| 秒杀 | 限时秒杀商品浏览、抢购（支持地址选择） |
| 活动 | 专题活动浏览、活动商品 |
| 排行榜 | 消费排行榜、签到排行榜 |
| 消息 | 系统通知、消息中心（未读角标） |
| 个人中心 | 用户信息、快捷功能入口、收货地址、收藏列表、积分、浏览历史 |
| 物流追踪 | 动态路线图（发货地→收货地）、卡车图标动画、6阶段进度可视化 |

### 商家端（merchant-web）

| 模块 | 功能 |
|------|------|
| 数据看板 | 店铺概览、销售数据 |
| 商品管理 | 发布商品、编辑商品、上架/下架/删除、SKU 管理 |
| 订单管理 | 订单列表、订单详情、发货 |
| 待发货 | 待发货订单专项列表 |
| 评价管理 | 查看商品评价、回复评价 |
| 结算管理 | 结算记录、收入明细 |
| 消息通知 | 新订单通知、系统消息（未读计数） |
| 店铺设置 | 店铺信息编辑、发货地址管理（省市区级联选择器） |
| 登录页 | 用户端/商家端快捷入口跳转 |

### 管理后台（admin-web）

| 模块 | 功能 |
|------|------|
| 数据看板 | 用户统计、订单统计、销售统计、商品统计、图表可视化 |
| 用户管理 | 用户列表、启用/禁用、分页、搜索 |
| 商家管理 | 商家入驻审核、商家列表、商家详情（调用详情接口获取完整信息） |
| 商品管理 | 全平台商品列表、审核、上下架 |
| 订单管理 | 全平台订单列表、订单详情 |
| 评价管理 | 全平台评价审核、删除 |
| Banner 管理 | 轮播图 CRUD、排序、上下线 |
| 优惠券管理 | 创建/编辑/删除优惠券、发放统计 |
| 积分商品管理 | 积分商品 CRUD、积分流水管理 |
| 秒杀活动管理 | 创建/编辑/删除秒杀活动 |
| 专题活动管理 | 创建/编辑/删除专题活动、活动商品关联 |
| 分类管理 | 分类树管理（排序字段 sortOrder、可见性 visible） |
| 品牌管理 | 品牌列表、添加/编辑/删除（DTO 规范化） |
| 角色管理 | 角色 CRUD、权限分配（PageResponse 分页适配） |
| 权限管理 | 权限列表、CRUD（字段与后端实体对齐） |
| 菜单管理 | 菜单树管理（去除 menuCode、sort→sortOrder、status→visible） |
| 字典管理 | 字典类型 CRUD、字典项 CRUD |
| 操作日志 | 操作日志查询（模块/操作类型下拉选择） |
| 登录日志 | 登录日志查询 |
| 登录页 | 用户端/商家端快捷入口跳转 |

---

## 数据库设计

- **38+ 张数据表**，覆盖用户、商家、商品、订单、支付、物流、评价、营销、系统配置等全领域
- 所有业务表均包含 `deleted` 软删除字段和 `created_at` / `updated_at` 审计字段
- 支持数据库降级缓存（`sys_cache` 表，当 Redis 不可用时自动切换）
- 关键表已添加索引优化（`t_order`、`t_product`、`t_cart` 等高频查询表）
- `t_shop` 表包含发货地址字段（sender_name/phone/province/city/district/address）
- `t_logistics_record` 表包含发货/收货地址字段（sender_address/receiver_address/sender_city）
- 初始化脚本：`sql/init.sql`（一次性执行，包含所有建表和基础数据）

---

## API 接口

- 公开接口（无需认证）：`/api/public/**` — 商品浏览、Banner、优惠券中心、秒杀、活动、排行榜
- 用户接口（需登录）：`/api/user/**` — 购物车、订单、支付、评价、收藏、签到、积分、优惠券
- 商家接口（需商家角色）：`/api/merchant/**` — 商品管理、订单发货、评价回复、结算
- 管理接口（需管理员角色）：`/api/admin/**` — 全平台运营管理
- 统一响应格式：`{ "code": 200, "message": "...", "data": {...}, "timestamp": "..." }`
- JWT Token 认证，角色权限控制（USER / MERCHANT / ADMIN / SUPER_ADMIN）
- Token 过期返回 403，前端自动清除 token 并跳转登录页
- Swagger UI 文档：`http://localhost:8080/swagger-ui.html`

---

## 初始数据

### 商品数据（288 个商品）

24 个分类 x 每分类 12 个商品，全部归属模拟商城旗舰店（shop_id=1）。

### 店铺数据（8 个店铺）

| 店铺 | 商家账号 | 密码 |
|------|---------|------|
| 模拟商城旗舰店 | merchant1 | merchant123 |
| 花漾美妆专营店 | merchant2 | merchant123 |
| 潮流服饰旗舰店 | merchant3 | merchant123 |
| 美食天下旗舰店 | merchant4 | merchant123 |
| 居家好物旗舰店 | merchant5 | merchant123 |
| 运动达人旗舰店 | merchant6 | merchant123 |
| 书虫书屋 | merchant7 | merchant123 |
| 宝贝乐园 | merchant8 | merchant123 |

### 测试账号

| 角色 | 用户名 | 密码 | 昵称 |
|------|--------|------|------|
| 管理员 | admin | admin123 | 超级管理员 |
| 普通用户 | user1 | user123 | 测试用户 |
| 普通用户 | user2 | user123 | 购物达人 |

---

## 快速开始

### 环境要求

- JDK 17+
- MySQL 8.0+
- Redis 7.0+（可选，支持数据库降级缓存）
- Node.js 18+
- Maven 3.8+

### 1. 数据库初始化

```bash
# 执行初始化脚本（建表 + 测试账号 + 分类 + Banner + 店铺等基础数据）
mysql -uroot -p sim_shopping < sql/init.sql

# 导入商品初始数据（可选，让页面有商品展示）
mysql -uroot -p sim_shopping < sql/init_products_full.sql

# 导入活动/秒杀/优惠券等营销数据（可选）
mysql -uroot -p sim_shopping < sql/init_other_data.sql
```

### 2. 启动后端

**Windows 本地开发：**
```bash
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

**打包部署（生产）：**
```bash
cd backend
mvn package -DskipTests
# 产物：target/sim-shopping.war
```

后端服务默认运行在 `http://localhost:8080`

### 3. 启动前端（开发模式）

```bash
# 用户端 — http://localhost:5173
cd frontend/user-web
npm install && npm run dev

# 商家端 — http://localhost:5174
cd frontend/merchant-web
npm install && npm run dev

# 管理后台 — http://localhost:5175
cd frontend/admin-web
npm install && npm run dev
```

### 4. 构建前端（生产部署）

```bash
cd frontend/user-web && npm run build
cd frontend/merchant-web && npm run build
cd frontend/admin-web && npm run build
# 各自产物：dist/ 目录（含 WEB-INF/web.xml，支持 Tomcat 部署）
```

---

## 部署方式

### 方式一：Tomcat 部署（当前生产方式）

```
/usr/bear/apache-tomcat-10.1.57/webapps/
├── ROOT.war              # 后端 WAR 包（改名为 ROOT.war）
├── user-web/             # 用户前端 dist 内容
│   ├── index.html
│   ├── assets/
│   └── WEB-INF/web.xml   # Vue Router history 模式 fallback
├── merchant-web/         # 商家前端 dist 内容
│   └── WEB-INF/web.xml
└── admin-web/            # 管理后台 dist 内容
    └── WEB-INF/web.xml
```

访问地址：
- 用户端：`http://ip:8080/user-web/`
- 商家端：`http://ip:8080/merchant-web/`
- 管理后台：`http://ip:8080/admin-web/`

### 方式二：Docker Compose 部署

```bash
# 完整部署（MySQL + Redis + Backend + 3 Frontend + Nginx）
docker-compose up -d

# 精简部署（1GiB 内存，仅用户端）
docker-compose -f docker-compose.minimal.yml up -d

# 使用宿主机 MySQL（开发环境）
docker-compose -f docker-compose.local-db.yml up -d
```

Nginx 统一入口 `http://localhost:80`，通过路径区分三个前端。

---

## 配置说明

### 多环境配置

| 文件 | 场景 | 说明 |
|------|------|------|
| `application.yml` | 公共配置 | JWT、缓存类型、文件上传、Jackson 时区等 |
| `application-local.yml` | 本地开发 | 数据库 `shop/shop`，缓存降级为 database，SQL 日志开启 |
| `application-prod.yml` | 生产部署 | 数据库通过环境变量配置，Redis 缓存，SQL 日志关闭 |

### 敏感配置（环境变量）

| 变量 | 说明 | 生产环境 |
|------|------|---------|
| `JWT_SECRET` | JWT 签名密钥 | 建议设置，有默认值 |
| `DB_HOST` | 数据库主机 | 默认 127.0.0.1 |
| `DB_PORT` | 数据库端口 | 默认 3306 |
| `DB_USERNAME` | 数据库用户名 | 默认 root |
| `DB_PASSWORD` | 数据库密码 | 默认 root123 |
| `REDIS_HOST` | Redis 主机 | 默认 127.0.0.1 |
| `REDIS_PASSWORD` | Redis 密码 | 默认空 |

### 缓存模式切换

通过 `app.cache.type` 配置在 Redis 和数据库缓存之间切换：

- `redis`（生产默认）：使用 Redis 做缓存
- `database`（本地默认）：使用 MySQL `sys_cache` 表做缓存（无需 Redis 环境）

---

## 核心特性

1. **免登录浏览** — 首页、商品列表、商品详情、搜索、分类、店铺页、领券中心、积分商城、秒杀、活动、排行榜均无需登录即可访问
2. **选品结算** — 购物车支持多选/全选，只结算选中的商品；支持优惠券选择和折扣计算
3. **完整的订单状态机** — CREATED -> PAID -> SHIPPED -> IN_TRANSIT -> DELIVERED -> COMPLETED
4. **模拟物流追踪** — 每 3 小时自动推进物流状态，动态路线图可视化（发货地→收货地卡车动画）
5. **商家发货地址** — 商家设置发货仓库地址，物流追踪显示实际发货城市
6. **省市区级联选择** — 用户地址管理和商家发货地址均使用三级级联选择器
7. **评价图片上传** — 对接文件上传 API，携带 Token 鉴权
8. **积分数据同步** — t_user.points 与 t_user_points 双表同步更新
9. **灵活缓存架构** — 工厂模式支持 Redis / 数据库双缓存实现，通过配置一键切换
10. **RBAC 权限模型** — 基于角色的细粒度权限控制（@PreAuthorize 注解）
11. **游戏化营销** — 优惠券发放/领取、积分签到/兑换、秒杀抢购（并发安全）、专题活动、排行榜
12. **安全防护** — DOMPurify XSS 防护、JWT Token 认证、密码 BCrypt 加密、接口权限校验（订单归属/支付归属）
13. **优惠券并发安全** — CAS 条件更新防止同一优惠券被重复使用
14. **SKU 库存管理** — 下单扣减 SKU 库存，退款恢复主库存 + SKU 库存
15. **Token 过期自动跳转** — JWT 过期（403）自动清除 token 并跳转登录页
16. **DDD 分层架构** — Controller/Service/Mapper 职责清晰，依赖方向正确，无层级穿透

---

## 文档

项目设计文档位于 `docs/` 目录：

| 文档 | 说明 |
|------|------|
| `ARCH-001_Architecture.md` | 架构设计文档 |
| `DB-001_Database.md` | 数据库设计文档 |
| `API-001_API_Contract.md` | API 接口契约文档 |
| `TASK-001_Task_Breakdown.md` | 开发任务拆解 |
| `DEV-001_Gamification_Roadmap.md` | 游戏化功能路线图 |
| `REVIEW-001_M0-M3_Doc_Impl_Diff_Fixes.md` | 文档与实现差异修复记录 |

---

## License

本项目仅供学习交流使用。
