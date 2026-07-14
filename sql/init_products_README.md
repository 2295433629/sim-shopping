# 商品初始数据说明

## 概述
本目录包含模拟商城系统的商品初始数据，用于让首页/商品列表等页面不再为空。

**重要声明：所有数据均为纯模拟娱乐用途，不涉及任何真实商品、真实交易。**

## 文件清单

| 文件/目录 | 说明 |
|---------|------|
| `init_products.sql` | 商品数据 SQL 插入脚本（32 个商品 + 32 条图片记录） |
| `images/products/` | AI 生成的商品图片（16 张，覆盖 8 大品类） |

## 数据分布

| 品类 | 店铺 | 商品数 | 图片文件 |
|------|------|--------|---------|
| 数码 | 极客数码旗舰店 | 4 | digital_phone, digital_laptop, digital_earbuds, digital_watch |
| 美妆 | 美丽佳人 | 4 | beauty_serum, beauty_lipstick |
| 服装 | 时尚潮流馆 | 4 | clothing_tshirt, clothing_dress, sports_shoes |
| 食品 | 美味食光 | 4 | food_nuts, food_fruit |
| 家居 | 温馨家居 | 4 | home_lamp, home_ceramic |
| 运动 | 运动达人旗舰店 | 4 | sports_shoes, sports_yoga |
| 图书 | 书香阁书店 | 4 | book_stack |
| 母婴 | 宝贝乐园 | 4 | baby_formula |

## 使用方法

### 1. 导入 SQL 数据
```bash
# 方式一：命令行
mysql -u root -p sim_shopping < init_products.sql

# 方式二：数据库客户端直接执行
# 打开 init_products.sql，全选后执行
```

### 2. 部署图片
图片已同时放置在以下两个位置：
- **SQL 备份目录**：`sql/images/products/`（随 SQL 一起归档）
- **前端静态资源**：`frontend/user-web/public/images/products/`（前端可直接访问）

如果在新环境部署，需要将 `sql/images/products/` 下的所有图片复制到前端项目的 `public/images/products/` 目录下。

### 3. 验证
启动后端后访问：
```
GET http://localhost:8080/api/public/products
```
应返回 35 条商品数据（含之前已有的 3 条 + 本次新增的 32 条）。

## 数据特点
- 所有商品状态为 `PUBLISHED`，可直接在商城展示
- 每个品类均有 `is_recommended=1` 的推荐商品，首页推荐位不会为空
- 每个品类均有 `is_new=1` 的新品，新品专区不会为空
- 商品包含合理的价格、库存、销量、浏览量、评分等模拟数据
- 图片路径统一为 `/images/products/xxx.jpg`，与前端静态资源目录对应
