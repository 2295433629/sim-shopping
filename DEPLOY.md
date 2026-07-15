# 模拟商城 Docker 部署文档

## 服务器信息

| 项目 | 值 |
|------|-----|
| 公网IP | `47.101.68.251` |
| 操作系统 | CentOS/Ubuntu (Docker 镜像) |
| Docker 版本 | `26.1.3` |
| 规格 | 2vCPU / 1GiB 内存 / 30GiB ESSD |
| 到期时间 | 2026年8月15日 |

---

## 一、前置准备

### 1.1 开放安全组端口

登录阿里云控制台，进入 ECS 实例的安全组配置，添加入方向规则：

| 端口 | 协议 | 授权对象 | 用途 |
|------|------|----------|------|
| 80 | TCP | 0.0.0.0/0 | Nginx 主入口 |
| 8080 | TCP | 0.0.0.0/0 | 后端 API（调试用，可选） |
| 3306 | TCP | 你的IP/32 | MySQL（建议仅开放给本地调试） |
| 22 | TCP | 你的IP/32 | SSH（已有） |

> **安全建议**：生产环境建议关闭 3306 和 8080 的公网访问，仅保留 80 端口对外。

### 1.2 检查 Docker 环境

SSH 登录服务器后执行：

```bash
docker --version        # 应显示 26.1.3
docker-compose --version # 应显示 2.x
```

如果 docker-compose 未安装：

```bash
# 安装 docker-compose (v2)
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```

---

## 二、部署步骤

### 2.1 上传项目代码

将项目代码上传到服务器（选择一种方式）：

**方式A：通过 git 克隆（推荐）**

```bash
cd /opt
git clone <你的仓库地址> sim-shopping
cd sim-shopping
```

**方式B：通过 scp/ftp 上传**

将本地项目压缩后上传到 `/opt/sim-shopping/` 目录。

### 2.2 配置环境变量（可选）

编辑 `.env` 文件修改敏感配置：

```bash
vim .env
```

默认配置：

```
MYSQL_ROOT_PASSWORD=root
DB_USERNAME=root
DB_PASSWORD=root
REDIS_PASSWORD=
JWT_SECRET=ShoppingDreamDockerSecretKey2026ForJwtTokenGeneration
```

> **生产环境务必修改**：`MYSQL_ROOT_PASSWORD` 和 `JWT_SECRET`。

### 2.3 执行部署脚本

```bash
chmod +x deploy.sh
./deploy.sh
```

脚本会自动完成：检查环境、拉取代码、清理旧容器、构建镜像、启动服务、健康检查。

### 2.4 手动部署（如果脚本不可用）

```bash
# 构建并启动所有服务
docker-compose up -d --build

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f backend
```

---

## 三、访问地址

部署完成后，通过浏览器访问：

| 端点 | 地址 |
|------|------|
| 用户端（商城首页） | `http://47.101.68.251/` |
| 商户端 | `http://47.101.68.251/merchant/` |
| 管理后台 | `http://47.101.68.251/admin/` |
| Swagger API 文档 | `http://47.101.68.251/swagger-ui.html` |

### 默认账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | `admin` | `admin123` |
| 商户 | `merchant1` | `admin123` |
| 普通用户 | `user1` | `123456` |

---

## 四、内存优化（1GiB 服务器必看）

当前服务器仅 1GiB 内存，同时运行 7 个容器（MySQL + Redis + 后端 + 3个前端 + Nginx）比较紧张。已做以下优化：

### 4.1 已配置的优化项

- **后端 JVM**：限制 `-Xms256m -Xmx512m`，使用 G1 垃圾回收器
- **Redis**：限制 `maxmemory 64mb`，超出时自动淘汰最近最少使用的键
- **MySQL**：使用 `--default-authentication-plugin=mysql_native_password` 减少兼容层内存占用

### 4.2 如果仍出现内存不足

1. **创建 swap 分区**（推荐）：

```bash
sudo fallocate -l 2G /swapfile
sudo chmod 600 /swapfile
sudo mkswap /swapfile
sudo swapon /swapfile
sudo swapon --show
```

2. **进一步限制 MySQL 内存**：创建 `docker/mysql/my.cnf`：

```ini
[mysqld]
innodb_buffer_pool_size = 128M
max_connections = 50
key_buffer_size = 16M
query_cache_size = 16M
tmp_table_size = 16M
max_heap_table_size = 16M
thread_cache_size = 4
table_open_cache = 64
```

然后在 `docker-compose.yml` 的 mysql 服务中挂载：

```yaml
volumes:
  - ./docker/mysql/my.cnf:/etc/mysql/conf.d/custom.cnf:ro
```

3. **升级服务器**：如果预算允许，建议升级到 2GiB 或 4GiB 内存。

---

## 五、服务架构

```
用户浏览器
    |
    v
Nginx (80端口)
    |-- /api/     --> backend:8080 (Spring Boot)
    |-- /uploads/ --> backend:8080
    |-- /merchant/ --> merchant-web:80
    |-- /admin/   --> admin-web:80
    |-- /         --> user-web:80

backend:8080
    |-- MySQL:3306
    |-- Redis:6379
```

---

## 六、常用维护命令

### 查看服务状态

```bash
docker-compose ps
docker-compose logs -f          # 查看所有日志
docker-compose logs -f backend  # 只看后端日志
docker-compose logs -f mysql    # 只看MySQL日志
```

### 重启服务

```bash
docker-compose restart backend  # 重启后端
docker-compose restart          # 重启所有
```

### 更新部署（代码更新后）

```bash
git pull                        # 拉取最新代码
./deploy.sh                     # 重新部署
```

### 进入数据库

```bash
docker exec -it shopping-mysql mysql -uroot -proot
USE sim_shopping;
SHOW TABLES;
```

### 备份数据库

```bash
docker exec shopping-mysql mysqldump -uroot -proot sim_shopping > backup_$(date +%Y%m%d).sql
```

### 恢复数据库

```bash
docker exec -i shopping-mysql mysql -uroot -proot sim_shopping < backup_20260101.sql
```

### 清理磁盘空间

```bash
docker system prune -f          # 清理未使用的镜像/容器/卷
docker volume prune -f          # 清理未使用的数据卷
```

---

## 七、故障排查

### 问题1：后端启动失败，提示数据库连接超时

**原因**：MySQL 启动较慢，后端先于 MySQL 就绪启动。

**解决**：已配置 `depends_on` + `condition: service_healthy`，后端会等待 MySQL 就绪后才启动。如果仍失败，手动重启：

```bash
docker-compose restart backend
```

### 问题2：商户端/管理后台页面空白或404

**原因**：Nginx 子路径代理时，前端构建产物的资源路径不匹配。

**解决**：已在 `vite.config.ts` 中配置 `base: '/merchant/'` 和 `base: '/admin/'`。重新构建部署：

```bash
docker-compose up -d --build merchant-web admin-web nginx
```

### 问题3：图片上传后无法显示

**原因**：上传目录未正确挂载。

**解决**：`docker-compose.yml` 中已配置：

```yaml
backend:
  volumes:
    - ./uploads:/app/uploads
nginx:
  volumes:
    - ./uploads:/usr/share/nginx/html/uploads:ro
```

### 问题4：内存不足，容器被 kill

**原因**：1GiB 内存不足。

**解决**：见第4节内存优化。优先创建 swap 分区。

### 问题5：MySQL 初始化脚本未执行

**原因**：`mysql_data` 卷已存在数据，MySQL 跳过初始化。

**解决**：

```bash
docker-compose down -v          # 删除数据卷（会清空数据！）
docker-compose up -d --build    # 重新启动，重新执行 init.sql
```

> **警告**：`down -v` 会删除所有数据库数据，仅在新部署或测试环境使用。

---

## 八、项目变更记录

本次为部署就绪做的调整：

1. **数据库**：`init.sql` 补充 9 张缺失表 + t_shop 财务字段 + 初始化数据
2. **前端**：`merchant-web` / `admin-web` 添加 `base` 配置，支持子路径部署
3. **后端**：`Dockerfile` 添加 JVM 内存限制参数（-Xmx512m）
4. **Docker**：`docker-compose.yml` 添加健康检查、depends_on condition、restart 策略
5. **部署**：新增 `deploy.sh` 一键部署脚本、`.env` 环境变量文件、`.dockerignore`
6. **Nginx**：挂载 uploads 目录，支持静态文件直接访问

---

## 九、联系与支持

如有问题，请检查：
1. 安全组端口是否开放
2. 服务器内存是否充足
3. `docker-compose logs` 中的错误信息
