# 模拟商城（Sim-Shopping）

> 模拟商城是一个提供真实购物体验的模拟电商平台，所有商品及交易均为模拟数据，仅供学习与体验使用。

## 项目概述

模拟商城是一个功能完整的 B2C 电商平台，涵盖用户端购物、商家端管理、管理后台运营三大子系统。用户可免登录浏览商品、搜索、分类筛选，登录后可完成完整的购物流程（加购、下单、支付、物流追踪、评价）。商家可管理店铺商品和订单，管理员可进行全平台运营管理。

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

### 部署

| 技术 | 说明 |
|------|------|
| Docker | 容器化部署 |
| Docker Compose | 多服务编排 |
| Nginx | 反向代理与静态资源服务 |

## 项目结构

```
sim-shopping/
├── backend/                    # 后端服务（Spring Boot）
│   ├── src/main/java/com/sim/shopping/
│   │   ├── application/        # 应用层（Service、事件监听）
│   │   ├── domain/             # 领域层（实体、事件、值对象）
│   │   ├── interfaces/         # 接口层（Controller、DTO）
│   │   └── infrastructure/     # 基础设施层（持久化、安全、缓存、配置）
│   └── pom.xml
├── frontend/
│   ├── user-web/               # 用户端（消费者）
│   ├── merchant-web/           # 商家端（店铺管理）
│   └── admin-web/              # 管理后台（平台运营）
├── sql/
│   └── init.sql                # 数据库初始化脚本（37张表 + 测试数据）
├── docker/
│   └── nginx/nginx.conf        # Nginx 配置文件
├── docs/                       # 设计文档
│   ├── PRD-001_产品需求文档.md
│   ├── ARCH-001_架构设计.md
│   ├── DB-001_数据库设计.md
│   ├── API-001_接口契约.md
│   └── TASK-001_任务拆解.md
├── docker-compose.yml          # Docker Compose 编排文件
└── README.md                   # 本文件
```

## 后端架构（DDD 四层架构）

```
┌─────────────────────────────────────┐
│  interfaces（接口层）                │  Controller + DTO + 参数校验
├─────────────────────────────────────┤
│  application（应用层）               │  Service + 事件监听 + 事务编排
├─────────────────────────────────────┤
│  domain（领域层）                    │  领域实体 + 领域事件 + 值对象
├─────────────────────────────────────┤
│  infrastructure（基础设施层）         │  Mapper + Redis + JWT + 配置
└─────────────────────────────────────┘
```

## 功能模块

### 用户端（user-web）

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
| 消息 | 系统通知、消息中心 |
| 个人中心 | 用户信息、收货地址、收藏列表、积分、浏览历史 |

### 商家端（merchant-web）

| 模块 | 功能 |
|------|------|
| 商品管理 | 发布商品、编辑商品、上架/下架/删除、SKU 管理 |
| 订单管理 | 订单列表、订单详情、发货、物流管理 |
| 评价管理 | 查看商品评价、回复评价 |
| 消息通知 | 新订单通知、系统消息 |
| 数据统计 | 店铺概览、销售数据 |

### 管理后台（admin-web）

| 模块 | 功能 |
|------|------|
| 数据看板 | 用户统计、订单统计、销售统计、商品统计、图表可视化 |
| 用户管理 | 用户列表、启用/禁用、查看详情 |
| 商家管理 | 商家入驻审核、商家列表、店铺管理 |
| 商品管理 | 全平台商品列表、审核、上下架 |
| 订单管理 | 全平台订单列表、订单详情 |
| 分类管理 | 分类树管理、添加/编辑/删除 |
| 品牌管理 | 品牌列表、添加/编辑/删除 |
| Banner管理 | 轮播图管理、排序、上下线 |
| 评价管理 | 全平台评价审核、删除 |
| 系统管理 | 角色权限、菜单管理、字典管理、操作日志、登录日志 |

## 数据库设计

- **37 张数据表**，覆盖用户、商家、商品、订单、支付、物流、评价、营销、系统配置等全领域
- 所有业务表均包含 `deleted` 软删除字段和 `created_at` / `updated_at` 审计字段
- 支持数据库降级缓存（`sys_cache` 表，当 Redis 不可用时自动切换）

## API 接口

- **134 个 REST API 接口**，覆盖 12 个功能域
- 统一响应格式：`{ "code": 200, "message": "...", "data": {...} }`
- JWT Token 认证，角色权限控制（USER / MERCHANT / ADMIN / SUPER_ADMIN）
- Swagger UI 文档：`http://localhost:8080/swagger-ui.html`

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

# 执行初始化脚本
mysql -uroot -p sim_shopping < sql/init.sql
```

### 2. 启动后端

```bash
cd backend
mvn spring-boot:run
```

后端服务默认运行在 `http://localhost:8080`

### 3. 启动前端

```bash
# 用户端
cd frontend/user-web
npm install
npm run dev

# 商家端
cd frontend/merchant-web
npm install
npm run dev

# 管理后台
cd frontend/admin-web
npm install
npm run dev
```

### 4. 测试账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 普通用户 | user1 | 123456 |
| 商家（数码） | merchant1 | 123456 |
| 商家（美妆） | merchant2 | 123456 |
| 管理员 | admin | 123456 |

## 配置说明

### application.yml 关键配置

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/sim_shopping
    username: root
    password: root
  redis:
    host: localhost
    port: 6379

app:
  jwt:
    secret: your-secret-key-here
    expiration: 86400000
  upload:
    path: ./uploads
  cache:
    type: redis    # redis / database 切换缓存模式
```

### 缓存模式切换

支持通过 `app.cache.type` 配置在 Redis 和数据库缓存之间切换：

- `redis`（默认）：使用 Redis 做缓存
- `database`：使用 MySQL `sys_cache` 表做缓存（无需 Redis 环境）

## Docker 部署

```bash
# 构建并启动所有服务
docker-compose up -d

# 服务端口
# - 用户端: http://localhost:80
# - 商家端: http://localhost:81
# - 管理后台: http://localhost:82
# - 后端 API: http://localhost:8080
```

## 核心特性

1. **免登录浏览** — 首页、商品列表、商品详情、搜索、分类、店铺页均无需登录即可访问
2. **完整的订单状态机** — CREATED → PAID → SHIPPED → IN_TRANSIT → DELIVERED → COMPLETED
3. **模拟物流追踪** — 自动推进物流状态，生成物流轨迹，支持实时查询
4. **灵活缓存架构** — 工厂模式支持 Redis / 数据库双缓存实现，通过配置一键切换
5. **RBAC 权限模型** — 基于角色的细粒度权限控制
6. **前后端分离** — 3 套前端独立部署，统一后端 API 服务

## 文档

项目设计文档位于 `docs/` 目录：

- `PRD-001_ShoppingDream_PRD_Complete_v1.0.0.md` — 产品需求文档
- `ARCH-001_Architecture.md` — 架构设计文档
- `DB-001_Database.md` — 数据库设计文档
- `API-001_API_Contract.md` — API 接口契约文档
- `TASK-001_Task_Breakdown.md` — 开发任务拆解

## License

本项目仅供学习交流使用。
