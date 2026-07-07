# ShoppingDream 开发进度追踪

> **此文件是跨会话恢复上下文的唯一入口。每次会话开始时先读此文件。**

## 项目基本信息

| 项 | 值 |
|---|---|
| 项目名称 | ShoppingDream — AI Virtual Shopping Platform |
| PRD 位置 | `D:\workspace\shopping\PRD-001_ShoppingDream_PRD_Complete_v1.0.0.md` |
| 代码根目录 | `D:\workspace\shopping\sim-shopping\` |
| 技术栈 | Spring Boot 3 + MyBatis-Plus + MySQL 8 + Redis 7 + Vue 3 + Element Plus + Vite 5 |
| 后端架构 | DDD 领域分层 |
| 前端架构 | 三端独立仓库（user-web / merchant-web / admin-web） |

## 文档清单

| 编号 | 文档名称 | 状态 | 位置 |
|---|---|---|---|
| PRD-001 | 产品需求文档 | ✅ 已完成 | `D:\workspace\shopping\PRD-001_...md` |
| ARCH-001 | 架构设计文档 | ✅ 已完成 | `sim-shopping/docs/ARCH-001_Architecture.md` |
| DB-001 | 数据库设计文档 | ✅ 已完成 | `sim-shopping/docs/DB-001_Database.md` |
| API-001 | API 契约文档 | ✅ 已完成 | `sim-shopping/docs/API-001_API_Contract.md` |
| TASK-001 | 任务拆分文档 | ✅ 已完成 | `sim-shopping/docs/TASK-001_Task_Breakdown.md` |

## 里程碑进度

| 里程碑 | 名称 | 状态 | 开始时间 | 完成时间 |
|---|---|---|---|---|
| M0 | 项目启动（文档+脚手架） | ✅ 已完成 | 2026-07-03 | 2026-07-04 |
| M1 | 基础设施（用户+RBAC+三端框架） | ✅ 已完成 | 2026-07-05 | 2026-07-05 |
| M2 | 商品闭环（商家+店铺+商品） | ✅ 已完成 | 2026-07-05 | 2026-07-06 |
| M3 | 交易闭环（购物车+订单+支付） | ✅ 已完成 | 2026-07-06 | 2026-07-06 |
| M4 | 履约闭环（发货+物流） | ⏳ | — | — |
| M5 | 体验闭环（评价+签到+推荐） | ⏳ | — | — |
| M6 | 管理闭环（管理后台全功能） | ⏳ | — | — |
| M7 | 验收发布（集成测试+Docker部署） | ⏳ | — | — |

## 当前任务

**正在做：** M3 全部完成，准备进入 M4

**已完成：**
- ✅ M0-01 PRD-001 产品需求文档
- ✅ M0-02 ARCH-001 架构设计文档
- ✅ M0-03 DB-001 数据库设计文档（37 张表）
- ✅ M0-04 API-001 API 契约文档（134 个接口）
- ✅ M0-05 TASK-001 任务拆分文档（164 个任务）
- ✅ PRD 对比审查 + 8 处遗漏完整修复
- ✅ 四份设计文档交叉验证
- ✅ M0-06 后端脚手架（Spring Boot 3.2.5 + 95 源文件编译通过 + 打包成功）
- ✅ M0-07 数据库初始化（37张表 + 初始化数据，MySQL 9.6.0 验证通过）
- ✅ M0-08/09/10 前端三端脚手架（user-web / merchant-web / admin-web，各21文件，Vue 3 + TS + Vite 5 + Element Plus + Pinia，三端构建全部通过）
- ✅ M0-11 Docker Compose 配置（docker-compose.yml + Nginx 反代 + 4个 Dockerfile + .gitignore）
- ✅ M1 后端核心（AuthService/AuthController + RBAC RoleService/PermissionService/RoleController/PermissionController + FileController + MetaObjectHandler + @EnableMethodSecurity）
- ✅ M1 后端 User+System（UserService/UserController + AdminAuthService/AdminAuthController + MenuService/MenuController + DictService/DictController + NotificationService/NotificationController + SecurityUtils）
- ✅ M1 前端三端（Axios拦截器/路由守卫/Pinia Store/登录注册/BasicLayout/首页/个人中心/地址管理/商家工作台/管理后台仪表盘/字典管理）
- ✅ M1 编译验证：后端 133 Java 文件 mvn compile + mvn package 通过，三端 npm run build 通过
- ✅ M2 后端商家+店铺（13文件：入驻/审核/禁启用/店铺编辑/Banner/Dashboard）
- ✅ M2 后端商品（20文件：CRUD/上下架/搜索/推荐/热门/详情/分类树/Brand CRUD/Favorite/Search联想）
- ✅ M2 前端 user-web（ProductList/Detail/Search/Shop/Category/Favorite/Home 完整页面+API层）
- ✅ M2 前端 merchant-web（ProductList/ProductEdit/ShopSettings/Dashboard + product.ts/merchant.ts API层 + 路由）
- ✅ M2 前端 admin-web（MerchantList/ProductList/CategoryManage/BrandManage + product.ts/merchant.ts API层 + 路由）
- ✅ M2 编译验证：后端 mvn compile BUILD SUCCESS，三端 npm run build 全部通过
- ✅ M3 后端 Cart（CartService/CartController + 6 DTOs + Redis 缓存 cart:user:{userId} 7天TTL）
- ✅ M3 后端 Order（OrderService + 3 Controllers + 8 DTOs + 领域事件 OrderCreatedEvent/OrderCancelledEvent + OrderEventListener）
- ✅ M3 后端 Payment（PaymentService/PaymentController + 2 DTOs + Mock 瞬时支付 + 自动更新订单状态为 PAID）
- ✅ M3 前端 user-web（CartView/CheckoutView/PaymentView/OrderListView/OrderDetailView + cart.ts/order.ts API层 + 路由更新）
- ✅ M3 前端 merchant-web（OrderListView/OrderDetailView + order.ts API层 + 路由更新）
- ✅ M3 前端 admin-web（OrderListView/OrderDetailView + order.ts API层 + 路由更新）
- ✅ M3 编译验证：后端 mvn compile BUILD SUCCESS，三端 vite build 全部通过

## 技术决策记录 (ADR)

| 编号 | 决策 | 日期 | 说明 |
|---|---|---|---|
| ADR-001 | 后端采用 DDD 领域分层 | 2026-07-03 | PRD 明确 12 个领域，DDD 最契合 |
| ADR-002 | 前端三端独立仓库 | 2026-07-03 | 三端权限/构建/部署独立，避免耦合 |
| ADR-003 | 文档先行策略 | 2026-07-03 | ARCH→DB→API→TASK 全部就位后再写代码 |
| ADR-004 | 移除 Lombok 依赖 | 2026-07-04 | Lombok 注解处理器在 Maven 编译中不稳定，改为手动 getter/setter |
| ADR-005 | 前端使用 TypeScript | 2026-07-04 | 子代理选择 TS 而非 JS，更规范，保留 |

## 已知问题

（暂无 — PRD 对比审查中的 8 处遗漏已全部修复）

## 备注

- 每次会话开始时，先读取此文件恢复上下文
- 每完成一个任务，更新此文件
- 每次技术决策记录到 ADR 表
