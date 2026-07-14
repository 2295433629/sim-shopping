# 模拟商城（Sim-Shopping）

> **重要声明**：本系统是一个纯模拟娱乐的商城系统，不涉及任何真实商品、真实货币、真实物流。所有商品、交易、支付、物流等数据均为虚拟模拟，仅供学习与娱乐体验使用，不产生任何实际消费或交易行为。

---

## 项目概述

模拟商城是一个功能完整的模拟娱乐 B2C 电商平台，涵盖用户端购物、商家端管理、管理后台运营三大子系统。用户可免登录浏览商品、搜索、分类筛选，登录后可体验完整的模拟购物流程（加购、下单、模拟支付、模拟物流追踪、评价）。商家可管理店铺商品和订单，管理员可进行全平台运营管理。系统还包含优惠券、积分商城、秒杀抢购、专题活动、排行榜等游戏化娱乐功能，所有数值均为模拟数据。

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
| Spring AOP | 6.1.6 | 日志与权限切面 |
| Hutool | 5.8.27 | 工具类库 |
| JJWT | 0.12.5 | JWT Token 生成与校验 |
| SpringDoc OpenAPI | 2.3.0 | API 文档（Swagger UI） |

### 前端

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue 3 | 3.x | 渐进式框架 |
| TypeScript | 5.x | 类型安全 |
| Element Plus | 2.x | UI 组件库 |
| Vite | 5.x | 构建工具 |
| Pinia | 2.x | 状态管理 |
| Vue Router | 4.x | 前端路由 |
| Axios | 1.x | HTTP 客户端 |
| DOMPurify | 3.x | XSS 防护 |

### 部署

| 技术 | 说明 |
|------|------|
| Docker | 容器化部署 |
| Docker Compose | 多服务编排（MySQL + Redis + Backend + 3 Frontend + Nginx） |
| Nginx | 反向代理与静态资源服务 |

---

## 项目结构

```
sim-shopping/
├── backend/                        # 后端服务（Spring Boot）
│   ├── src/main/java/com/sim/shopping/
│   │   ├── application/            #   应用层（Service、事件监听）
│   │   ├── domain/                 #   领域层（实体、事件、值对象）
│   │   ├── interfaces/             #   接口层（Controller 47个、DTO）
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
│   ├── user-web/                   # 用户端（消费者） — port 5173
│   ├── merchant-web/               # 商家端（店铺管理） — port 5174
│   └── admin-web/                  # 管理后台（平台运营） — port 5175
├── sql/
│   ├── init.sql                    # 数据库初始化脚本（38 张表 + 测试数据）
│   ├── init_products.sql           # 商品初始数据脚本（32 个商品 + 32 条图片记录）
│   └── images/products/             # AI 生成商品图片（16 张，8 大品类）
├── docker/
│   └── nginx/nginx.conf             # Nginx 反向代理配置
├── docs/                           # 设计文档
│   ├── PRD-001_产品需求文档.md
│   ├── ARCH-001_架构设计.md
│   ├── DB-001_数据库设计.md
│   ├── API-001_接口契约.md
│   └── TASK-001_任务拆解.md
├── docker-compose.yml               # Docker Compose 编排文件
└── README.md                        # 本文件
```

---

## 后端架构（DDD 四层架构）

```
┌─────────────────────────────────────┐
│  interfaces（接口层）                │  47 个 Controller + DTO + 参数校验
├─────────────────────────────────────┤
│  application（应用层）               │  Service + 事件监听 + 事务编排
├─────────────────────────────────────┤
│  domain（领域层）                    │  领域实体 + 领域事件 + 值对象
├─────────────────────────────────────┤
│  infrastructure（基础设施层）         │  Mapper + Redis + JWT + 配置
└─────────────────────────────────────┘
```

---

## 功能模块

### 用户端（user-web）— port 5173

| 模块 | 功能 |
|------|------|
| 首页 | Banner 轮播、推荐商品、热门商品、新品上市、商品分类导航 |
| 商品浏览 | 商品列表、商品详情、SKU 选择、收藏商品、浏览历史 |
| 搜索 | 关键词搜索、搜索建议、热门搜索词、搜索历史 |
| 分类 | 一级/二级分类树、分类商品列表、价格筛选、排序 |
| 店铺 | 店铺详情、店铺商品列表 |
| 购物车 | 添加/删除/修改数量、选择结算、批量操作 |
| 订单 | 创建订单、订单列表、订单详情、取消订单、确认收货、物流追踪 |
| 支付 | 模拟支付（支持支付宝/微信/余额） |
| 评价 | 提交评价、查看评价、我的评价 |
| 签到 | 每日签到赚积分 |
| 优惠券 | 领券中心、我的优惠券 |
| 积分商城 | 积分商品兑换、积分明细 |
| 秒杀 | 限时秒杀商品浏览、抢购 |
| 活动 | 专题活动浏览、活动商品 |
| 排行榜 | 消费排行榜、签到排行榜 |
| 消息 | 系统通知、消息中心 |
| 个人中心 | 用户信息、收货地址、收藏列表、积分、浏览历史 |

### 商家端（merchant-web）— port 5174

| 模块 | 功能 |
|------|------|
| 数据看板 | 店铺概览、销售数据 |
| 商品管理 | 发布商品、编辑商品、上架/下架/删除、SKU 管理 |
| 订单管理 | 订单列表、订单详情、发货、物流管理 |
| 待发货 | 待发货订单专项列表 |
| 评价管理 | 查看商品评价、回复评价 |
| 消息通知 | 新订单通知、系统消息 |
| 店铺设置 | 店铺信息编辑 |

### 管理后台（admin-web）— port 5175

| 模块 | 功能 |
|------|------|
| 数据看板 | 用户统计、订单统计、销售统计、商品统计、图表可视化 |
| 用户管理 | 用户列表、启用/禁用、查看详情 |
| 商家管理 | 商家入驻审核、商家列表、店铺管理 |
| 商品管理 | 全平台商品列表、审核、上下架 |
| 订单管理 | 全平台订单列表、订单详情 |
| 评价管理 | 全平台评价审核、删除 |
| Banner 管理 | 轮播图管理、排序、上下线 |
| 优惠券管理 | 创建/编辑/删除优惠券、发放统计 |
| 积分商品管理 | 积分商品 CRUD、积分流水管理 |
| 秒杀活动管理 | 创建/编辑/删除秒杀活动 |
| 专题活动管理 | 创建/编辑/删除专题活动 |
| 分类管理 | 分类树管理、添加/编辑/删除 |
| 品牌管理 | 品牌列表、添加/编辑/删除 |
| 角色管理 | 角色 CRUD、权限分配 |
| 权限管理 | 权限列表、CRUD |
| 菜单管理 | 菜单树管理、CRUD |
| 字典管理 | 字典类型 CRUD、字典项 CRUD |
| 操作日志 | 操作日志查询 |
| 登录日志 | 登录日志查询 |

---

## 数据库设计

- **38 张数据表**，覆盖用户、商家、商品、订单、支付、物流、评价、营销、系统配置等全领域
- 所有业务表均包含 `deleted` 软删除字段和 `created_at` / `updated_at` 审计字段
- 支持数据库降级缓存（`sys_cache` 表，当 Redis 不可用时自动切换）
- 关键表已添加索引优化（`t_order`、`t_product`、`t_cart` 等高频查询表）

---

## API 接口

- **47 个 Controller**，覆盖 12 个功能域
- 公开接口（无需认证）：`/api/public/**` — 商品浏览、Banner、优惠券中心、秒杀、活动、排行榜
- 用户接口（需登录）：`/api/user/**` — 购物车、订单、支付、评价、收藏、签到、积分、优惠券
- 商家接口（需商家角色）：`/api/merchant/**` — 商品管理、订单发货、评价回复
- 管理接口（需管理员角色）：`/api/admin/**` — 全平台运营管理
- 统一响应格式：`{ "code": 200, "message": "...", "data": {...} }`
- JWT Token 认证，角色权限控制（USER / MERCHANT / ADMIN / SUPER_ADMIN）
- Swagger UI 文档：`http://localhost:8080/swagger-ui.html`

---

## 初始数据

### 商品数据（32 个商品）

| 品类 | 店铺 | 商品数 | 代表商品 |
|------|------|--------|---------|
| 数码 | 极客数码旗舰店 | 4 | iPhone 15 Pro、MacBook Air M3、AirPods Pro 2、华为 WATCH GT4 |
| 美妆 | 美丽佳人 | 4 | 雅诗兰黛小棕瓶、兰蔻粉底液、迪奥口红、SK-II 神仙水 |
| 服装 | 时尚潮流馆 | 4 | 优衣库 T 恤、ZARA 连衣裙、耐克跑鞋、阿迪达斯运动 T 恤 |
| 食品 | 美味食光 | 4 | 三只松鼠坚果、牛肉干、NFC 橙汁、进口橙子 |
| 家居 | 温馨家居 | 4 | 无印良品四件套、宜家台灯、景德镇餐具、收纳盒 |
| 运动 | 运动达人旗舰店 | 4 | 耐克 Zoom Fly、阿迪达斯 Ultraboost、李宁篮球鞋、瑜伽垫 |
| 图书 | 书香阁书店 | 4 | 《活着》《三体》《百年孤独》《深入理解计算机系统》 |
| 母婴 | 宝贝乐园 | 4 | 飞鹤奶粉、爱他美奶粉、花王纸尿裤、乐高积木 |

- 所有商品状态为 `PUBLISHED`，可直接展示
- 每个品类均有推荐商品和新品
- 16 张 AI 生成商品图片，路径统一为 `/images/products/xxx.jpg`
- 图片归档位置：`sql/images/products/`，前端静态资源：`frontend/user-web/public/images/products/`

### 测试账号

| 角色 | 用户名 | 密码 | 昵称 |
|------|--------|------|------|
| 管理员 | admin | admin123 | 超级管理员 |
| 商家（数码） | merchant1 | merchant123 | 数码达人 |
| 商家（美妆） | merchant2 | merchant123 | 美妆女王 |
| 商家（服装） | merchant3 | merchant123 | 潮流服饰 |
| 商家（食品） | merchant4 | merchant123 | 美食天下 |
| 商家（家居） | merchant5 | merchant123 | 居家好物 |
| 商家（运动） | merchant6 | merchant123 | 运动达人 |
| 商家（图书） | merchant7 | merchant123 | 书虫书屋 |
| 商家（母婴） | merchant8 | merchant123 | 宝贝乐园 |
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
# 创建数据库
create database sim_shopping default character set utf8mb4;

# 执行基础表结构和测试账号
mysql -uroot -p sim_shopping < sql/init.sql

# 导入商品初始数据（可选，让页面不为空）
mysql -uroot -p sim_shopping < sql/init_products.sql
```

### 2. 部署商品图片

```bash
# 将商品图片复制到前端静态资源目录
# 图片源: sql/images/products/ → 目标: frontend/user-web/public/images/products/
```

### 3. 启动后端

```bash
cd backend
mvn spring-boot:run
```

后端服务默认运行在 `http://localhost:8080`

### 4. 启动前端

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

三个前端项目的 `/api` 请求均通过 Vite proxy 代理到 `http://localhost:8080`。

---

## 配置说明

### 多环境配置

| 文件 | 场景 | 说明 |
|------|------|------|
| `application.yml` | 公共配置 | JWT、缓存类型、文件上传等 |
| `application-local.yml` | 本地开发 | 数据库/Redis 连接（含默认回退值） |
| `application-prod.yml` | 生产部署 | 数据库/Redis 连接（强制使用环境变量） |

### 敏感配置（环境变量）

| 变量 | 说明 | 生产环境 |
|------|------|---------|
| `JWT_SECRET` | JWT 签名密钥 | 必须设置，无默认值 |
| `DB_USERNAME` | 数据库用户名 | 必须设置，无默认值 |
| `DB_PASSWORD` | 数据库密码 | 必须设置，无默认值 |
| `REDIS_PASSWORD` | Redis 密码 | 必须设置，无默认值 |

### 缓存模式切换

通过 `app.cache.type` 配置在 Redis 和数据库缓存之间切换：

- `redis`（默认）：使用 Redis 做缓存
- `database`：使用 MySQL `sys_cache` 表做缓存（无需 Redis 环境）

---

## Docker 部署

```bash
# 构建并启动所有服务（MySQL + Redis + Backend + 3 Frontend + Nginx）
docker-compose up -d

# 服务端口
# - Nginx 入口:    http://localhost:80（通过路径区分三个前端）
# - 用户端:       http://localhost:5173 或通过 Nginx 访问
# - 商家端:       http://localhost:5174 或通过 Nginx 访问
# - 管理后台:     http://localhost:5175 或通过 Nginx 访问
# - 后端 API:     http://localhost:8080
# - MySQL:        localhost:3306
# - Redis:        localhost:6379
```

---

## 核心特性

1. **免登录浏览** — 首页、商品列表、商品详情、搜索、分类、店铺页、领券中心、积分商城、秒杀、活动、排行榜均无需登录即可访问
2. **完整的订单状态机** — CREATED → PAID → SHIPPED → IN_TRANSIT → DELIVERED → COMPLETED
3. **模拟物流追踪** — 自动推进物流状态，生成物流轨迹，支持实时查询
4. **灵活缓存架构** — 工厂模式支持 Redis / 数据库双缓存实现，通过配置一键切换
5. **RBAC 权限模型** — 基于角色的细粒度权限控制（@PreAuthorize 注解）
6. **游戏化营销** — 优惠券发放/领取、积分签到/兑换、秒杀抢购、专题活动、排行榜
7. **安全防护** — DOMPurify XSS 防护、JWT 环境变量注入、密码 BCrypt 加密（各用户独立哈希）
8. **前后端分离** — 3 套前端独立部署，统一后端 API 服务
9. **统一异常处理** — GlobalExceptionHandler 全局捕获，标准化错误响应
10. **SQL 聚合优化** — Dashboard 统计使用 SQL 聚合查询，避免全表加载到内存

---

## 文档

项目设计文档位于 `docs/` 目录：

| 文档 | 说明 |
|------|------|
| `PRD-001_产品需求文档.md` | 产品需求文档 |
| `ARCH-001_架构设计.md` | 架构设计文档 |
| `DB-001_数据库设计.md` | 数据库设计文档 |
| `API-001_接口契约.md` | API 接口契约文档 |
| `TASK-001_任务拆解.md` | 开发任务拆解 |

---

## License

本项目仅供学习交流使用。
