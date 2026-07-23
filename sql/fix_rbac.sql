-- =====================================================
-- 角色权限菜单数据修复脚本
-- 执行前请确保 t_sys_role 表已有数据（SUPER_ADMIN=1, ADMIN=2, MERCHANT=3, USER=4）
-- =====================================================

-- 1. 插入权限数据
INSERT INTO `t_sys_permission` (`permission_code`, `permission_name`, `permission_type`, `module`, `description`) VALUES
-- 系统管理模块
('system:user:list', '用户列表', 3, '系统管理', '查看用户列表'),
('system:user:disable', '禁用用户', 2, '系统管理', '禁用用户账号'),
('system:user:enable', '启用用户', 2, '系统管理', '启用用户账号'),
('system:merchant:list', '商家列表', 3, '系统管理', '查看商家列表'),
('system:merchant:approve', '审核商家', 2, '系统管理', '审核商家入驻'),
('system:role:list', '角色列表', 3, '系统管理', '查看角色列表'),
('system:role:add', '新增角色', 2, '系统管理', '创建新角色'),
('system:role:edit', '编辑角色', 2, '系统管理', '修改角色信息'),
('system:role:delete', '删除角色', 2, '系统管理', '删除角色'),
('system:role:assign', '分配权限', 2, '系统管理', '为角色分配权限'),
('system:permission:list', '权限列表', 3, '系统管理', '查看权限列表'),
('system:permission:add', '新增权限', 2, '系统管理', '创建新权限'),
('system:permission:edit', '编辑权限', 2, '系统管理', '修改权限信息'),
('system:permission:delete', '删除权限', 2, '系统管理', '删除权限'),
('system:menu:list', '菜单列表', 3, '系统管理', '查看菜单树'),
('system:menu:add', '新增菜单', 2, '系统管理', '创建新菜单'),
('system:menu:edit', '编辑菜单', 2, '系统管理', '修改菜单信息'),
('system:menu:delete', '删除菜单', 2, '系统管理', '删除菜单'),
('system:dict:list', '字典列表', 3, '系统管理', '查看字典列表'),
('system:dict:add', '新增字典', 2, '系统管理', '创建新字典'),
('system:dict:edit', '编辑字典', 2, '系统管理', '修改字典信息'),
('system:dict:delete', '删除字典', 2, '系统管理', '删除字典'),
-- 商品管理模块
('product:list', '商品列表', 3, '商品管理', '查看商品列表'),
('product:audit', '商品审核', 2, '商品管理', '审核商品上下架'),
('product:category:list', '分类列表', 3, '商品管理', '查看商品分类'),
('product:category:add', '新增分类', 2, '商品管理', '创建商品分类'),
('product:category:edit', '编辑分类', 2, '商品管理', '修改商品分类'),
('product:category:delete', '删除分类', 2, '商品管理', '删除商品分类'),
('product:brand:list', '品牌列表', 3, '商品管理', '查看品牌列表'),
('product:brand:add', '新增品牌', 2, '商品管理', '创建品牌'),
('product:brand:edit', '编辑品牌', 2, '商品管理', '修改品牌信息'),
('product:brand:delete', '删除品牌', 2, '商品管理', '删除品牌'),
-- 订单管理模块
('order:list', '订单列表', 3, '订单管理', '查看订单列表'),
('order:detail', '订单详情', 3, '订单管理', '查看订单详情'),
-- 内容管理模块
('content:review:list', '评价列表', 3, '内容管理', '查看评价列表'),
('content:review:audit', '评价审核', 2, '内容管理', '审核评价'),
('content:review:delete', '删除评价', 2, '内容管理', '删除评价'),
('content:banner:list', 'Banner列表', 3, '内容管理', '查看Banner列表'),
('content:banner:add', '新增Banner', 2, '内容管理', '创建Banner'),
('content:banner:edit', '编辑Banner', 2, '内容管理', '修改Banner信息'),
('content:banner:delete', '删除Banner', 2, '内容管理', '删除Banner'),
-- 营销管理模块
('marketing:coupon:list', '优惠券列表', 3, '营销管理', '查看优惠券列表'),
('marketing:coupon:add', '新增优惠券', 2, '营销管理', '创建优惠券'),
('marketing:coupon:edit', '编辑优惠券', 2, '营销管理', '修改优惠券'),
('marketing:coupon:delete', '删除优惠券', 2, '营销管理', '删除优惠券'),
('marketing:points:list', '积分商品列表', 3, '营销管理', '查看积分商品'),
('marketing:flashsale:list', '秒杀活动列表', 3, '营销管理', '查看秒杀活动'),
('marketing:activity:list', '专题活动列表', 3, '营销管理', '查看专题活动'),
-- 系统监控模块
('monitor:log:operation', '操作日志', 3, '系统监控', '查看操作日志'),
('monitor:log:login', '登录日志', 3, '系统监控', '查看登录日志'),
('monitor:scheduler:list', '定时任务列表', 3, '系统监控', '查看定时任务'),
('monitor:scheduler:edit', '编辑定时任务', 2, '系统监控', '修改定时任务配置'),
('monitor:scheduler:execute', '执行定时任务', 2, '系统监控', '手动执行定时任务');

-- 2. 插入菜单数据
INSERT INTO `t_sys_menu` (`parent_id`, `name`, `path`, `component`, `icon`, `sort_order`, `type`, `permission`, `visible`) VALUES
-- 一级菜单
(0, '系统管理', '/system', NULL, 'Setting', 1, 'MENU', NULL, 1),
(0, '商品管理', '/product', NULL, 'Goods', 2, 'MENU', NULL, 1),
(0, '订单管理', '/order', NULL, 'Document', 3, 'MENU', NULL, 1),
(0, '内容管理', '/content', NULL, 'Picture', 4, 'MENU', NULL, 1),
(0, '营销管理', '/marketing', NULL, 'Ticket', 5, 'MENU', NULL, 1),
(0, '系统监控', '/monitor', NULL, 'Monitor', 6, 'MENU', NULL, 1),
-- 二级菜单：系统管理（parent_id=1）
(1, '用户管理', '/users', 'users/UserListView', 'User', 1, 'MENU', 'system:user:list', 1),
(1, '商家管理', '/merchants', 'merchants/MerchantListView', 'Shop', 2, 'MENU', 'system:merchant:list', 1),
(1, '角色管理', '/roles', 'roles/RoleListView', 'UserFilled', 3, 'MENU', 'system:role:list', 1),
(1, '权限管理', '/permissions', 'permissions/PermissionListView', 'Key', 4, 'MENU', 'system:permission:list', 1),
(1, '菜单管理', '/menus', 'menus/MenuListView', 'Menu', 5, 'MENU', 'system:menu:list', 1),
(1, '字典管理', '/dicts', 'dicts/DictListView', 'Collection', 6, 'MENU', 'system:dict:list', 1),
-- 二级菜单：商品管理（parent_id=2）
(2, '商品列表', '/products', 'products/ProductListView', 'Goods', 1, 'MENU', 'product:list', 1),
(2, '分类管理', '/categories', 'products/CategoryManageView', 'Grid', 2, 'MENU', 'product:category:list', 1),
(2, '品牌管理', '/brands', 'products/BrandManageView', 'Stamp', 3, 'MENU', 'product:brand:list', 1),
-- 二级菜单：订单管理（parent_id=3）
(3, '订单列表', '/orders', 'orders/OrderListView', 'Document', 1, 'MENU', 'order:list', 1),
-- 二级菜单：内容管理（parent_id=4）
(4, '评价管理', '/reviews', 'reviews/ReviewManageView', 'ChatDotRound', 1, 'MENU', 'content:review:list', 1),
(4, 'Banner管理', '/banners', 'banners/BannerManageView', 'Picture', 2, 'MENU', 'content:banner:list', 1),
-- 二级菜单：营销管理（parent_id=5）
(5, '优惠券管理', '/coupons', 'coupons/CouponListView', 'Ticket', 1, 'MENU', 'marketing:coupon:list', 1),
(5, '积分商品', '/points/products', 'points/PointsProductListView', 'Goods', 2, 'MENU', 'marketing:points:list', 1),
(5, '积分流水', '/points/records', 'points/PointsRecordListView', 'Document', 3, 'MENU', 'marketing:points:list', 1),
(5, '秒杀活动', '/flash-sales', 'flashsale/FlashSaleListView', 'AlarmClock', 4, 'MENU', 'marketing:flashsale:list', 1),
(5, '专题活动', '/activities', 'activity/ActivityListView', 'Star', 5, 'MENU', 'marketing:activity:list', 1),
-- 二级菜单：系统监控（parent_id=6）
(6, '操作日志', '/logs/operation', 'logs/OperationLogView', 'Document', 1, 'MENU', 'monitor:log:operation', 1),
(6, '登录日志', '/logs/login', 'logs/LoginLogView', 'Tickets', 2, 'MENU', 'monitor:log:login', 1),
(6, '定时任务', '/scheduler', 'scheduler/ScheduleJobListView', 'Timer', 3, 'MENU', 'monitor:scheduler:list', 1);

-- 3. 为ADMIN角色分配全部权限（通过子查询自动获取所有权限ID）
INSERT INTO `t_sys_role_permission` (`role_id`, `permission_id`)
SELECT r.id, p.id FROM `t_sys_role` r, `t_sys_permission` p
WHERE r.role_code = 'ADMIN';

-- 4. 为ADMIN角色分配全部菜单（通过子查询自动获取所有菜单ID）
INSERT INTO `t_sys_role_menu` (`role_id`, `menu_id`)
SELECT r.id, m.id FROM `t_sys_role` r, `t_sys_menu` m
WHERE r.role_code = 'ADMIN' AND m.deleted = 0;
