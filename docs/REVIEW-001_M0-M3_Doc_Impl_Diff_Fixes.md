# REVIEW-001 M0-M3 设计文档对照修正记录

> 日期：2026-07-06  
> 范围：以当前仓库 **M3 之前已实现部分** 为准，对照 `docs/` 下设计文档（ARCH/DB/API/TASK）进行一致性检查与修正。

## 结论摘要

- 发现的主要偏差集中在 **认证返回结构**、**管理员登录字段命名**、**分页参数命名**、以及 **架构文档的 DDD 落地程度描述**。
- 已完成必要的代码与文档修正，并确保：后端 `mvn compile`、前端三端 `npm run build` 均通过。

## 关键修正项

### 认证返回结构不一致（设计/前端 vs 后端）

**问题**
- `API-001` 中 `POST /api/common/login` 与 `POST /api/common/refresh-token` 的 Response 结构，与后端实际返回与前端消费字段不一致。
- 前端三端登录逻辑会读取 `nickname/avatar/role/token`，而后端 `TokenResponse` 之前只返回 `userId/username/token/expiresIn/refreshToken`，导致运行期字段缺失。

**修正**
- 扩展后端 `TokenResponse`：增加 `nickname/avatar/role` 字段，使 `/api/common/login|register|refresh-token` 返回结构与前端一致。
- 同步更新 `docs/API-001_API_Contract.md` 的认证模块示例响应。

涉及文件：
- `backend/src/main/java/com/sim/shopping/interfaces/dto/auth/TokenResponse.java`
- `backend/src/main/java/com/sim/shopping/application/auth/AuthService.java`
- `docs/API-001_API_Contract.md`

### 管理员登录 token 字段命名不一致

**问题**
- `POST /api/admin/login` 后端返回字段为 `accessToken`，但前端管理后台读取字段为 `token`。

**修正**
- 后端 `AdminAuthService` 增加 `token` 字段（并保留 `accessToken` 兼容字段），同时补充 `expiresIn`。
- `API-001` 补充管理员登录接口说明与示例返回。

涉及文件：
- `backend/src/main/java/com/sim/shopping/application/system/AdminAuthService.java`
- `docs/API-001_API_Contract.md`

### 用户申请入驻接口路径不一致

**问题**
- `API-001` 约定用户侧入驻申请为 `POST /api/user/merchant-application`。
- 实现中存在 `POST /api/merchant/apply`。

**修正**
- 新增 `POST /api/user/merchant-application` 作为别名，复用同一业务逻辑，避免前后端/文档割裂。

涉及文件：
- `backend/src/main/java/com/sim/shopping/interfaces/merchant/MerchantController.java`

### 订单列表分页参数命名不一致（pageSize vs size）

**问题**
- 后端接口使用 `size`（与 `API-001` 一致），但部分前端订单列表请求使用 `pageSize`，导致后端默认分页生效、前端分页不生效。

**修正**
- 将三端订单列表请求参数统一改为 `size`。
- 前端 `PageResponse` 类型保留 `size/pageSize` 双写法以兼容其他模块。

涉及文件：
- `frontend/user-web/src/api/modules/order.ts`
- `frontend/user-web/src/views/order/OrderListView.vue`
- `frontend/merchant-web/src/api/modules/order.ts`
- `frontend/merchant-web/src/views/orders/OrderListView.vue`
- `frontend/admin-web/src/api/modules/order.ts`
- `frontend/admin-web/src/views/orders/OrderListView.vue`
- `frontend/*/src/types/common.ts`

### 架构文档与当前实现的 DDD 落地程度描述偏差

**问题**
- `ARCH-001` 以“完整 DDD 四层 + 领域模型”作为 SSOT 描述，但当前实现阶段主要采用：
  - `application/*Service` 作为业务编排与事务边界
  - `infrastructure/persistence/entity/*DO + mapper` 作为持久化
  - `domain/` 目前主要承载业务异常与领域事件

**修正**
- 在 `ARCH-001` 领域聚合章节补充“V1 当前实现说明”，明确当前实现方式与后续演进方向。
- 修正依赖清单：移除未使用的 `lombok/mapstruct` 描述，使文档与 `pom.xml` 一致。

涉及文件：
- `docs/ARCH-001_Architecture.md`
- `backend/pom.xml`

### 上传大小限制（配置 vs 代码）不一致

**问题**
- 代码 `FileController` 以 5MB 为准，但 `application.yml` 允许 10MB，容易造成“上传成功/失败提示不一致”的误解。

**修正**
- 将 `application.yml` 的 multipart 限制调整为 5MB/20MB，与 `API-001` 和 `FileController` 保持一致。

涉及文件：
- `backend/src/main/resources/application.yml`

## 编译验证

- 后端：`mvn -DskipTests compile` ✅
- 前端：`user-web / merchant-web / admin-web` 执行 `npm run build` ✅

