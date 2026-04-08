-- =============================================
-- 智能仓库管理系统 - 数据库初始化脚本
-- 数据库软件：Navicat Premium 17
-- 数据库账号：root
-- 数据库密码：123456
-- 创建日期：2026-03-11
-- =============================================

-- 1. 创建数据库
DROP DATABASE IF EXISTS warehouse_db;
CREATE DATABASE warehouse_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE warehouse_db;

-- =============================================
-- 2. 创建数据表
-- =============================================

-- 2.1 用户表
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) COMMENT '昵称',
    phone VARCHAR(20) COMMENT '手机号',
    role VARCHAR(20) DEFAULT 'user' COMMENT '角色：admin/user',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '用户表';

-- 2.2 供应商表
DROP TABLE IF EXISTS wms_supplier;
CREATE TABLE wms_supplier (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '供应商ID',
    supplier_code VARCHAR(50) NOT NULL UNIQUE COMMENT '供应商编码',
    supplier_name VARCHAR(100) NOT NULL COMMENT '供应商名称',
    contact VARCHAR(50) COMMENT '联系人',
    phone VARCHAR(20) COMMENT '联系电话',
    address VARCHAR(200) COMMENT '地址',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '供应商表';

-- 2.3 物料表
DROP TABLE IF EXISTS wms_material;
CREATE TABLE wms_material (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '物料ID',
    material_code VARCHAR(50) NOT NULL UNIQUE COMMENT '物料编码',
    material_name VARCHAR(100) NOT NULL COMMENT '物料名称',
    category VARCHAR(50) COMMENT '物料分类',
    unit VARCHAR(20) COMMENT '单位',
    supplier_id BIGINT COMMENT '供应商ID',
    warning_stock INT DEFAULT 10 COMMENT '预警库存',
    current_stock INT DEFAULT 0 COMMENT '当前库存',
    remark VARCHAR(200) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (supplier_id) REFERENCES wms_supplier(id) ON DELETE SET NULL
) COMMENT '物料表';

-- 2.4 仓位表
DROP TABLE IF EXISTS wms_location;
CREATE TABLE wms_location (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '仓位ID',
    location_code VARCHAR(50) NOT NULL UNIQUE COMMENT '仓位编码',
    location_name VARCHAR(100) NOT NULL COMMENT '仓位名称',
    warehouse VARCHAR(50) COMMENT '所属仓库',
    capacity INT COMMENT '容量',
    status VARCHAR(20) DEFAULT 'available' COMMENT '状态：available/occupied/disabled',
    remark VARCHAR(200) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '仓位表';

-- 2.5 入库记录表
DROP TABLE IF EXISTS wms_inbound;
CREATE TABLE wms_inbound (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '入库ID',
    inbound_no VARCHAR(50) NOT NULL UNIQUE COMMENT '入库单号',
    material_id BIGINT NOT NULL COMMENT '物料ID',
    location_id BIGINT COMMENT '仓位ID',
    quantity INT NOT NULL COMMENT '入库数量',
    operator VARCHAR(50) COMMENT '操作人',
    inbound_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '入库时间',
    remark VARCHAR(200) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (material_id) REFERENCES wms_material(id) ON DELETE CASCADE,
    FOREIGN KEY (location_id) REFERENCES wms_location(id) ON DELETE SET NULL
) COMMENT '入库记录表';

-- 2.6 出库记录表
DROP TABLE IF EXISTS wms_outbound;
CREATE TABLE wms_outbound (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '出库ID',
    outbound_no VARCHAR(50) NOT NULL UNIQUE COMMENT '出库单号',
    material_id BIGINT NOT NULL COMMENT '物料ID',
    location_id BIGINT COMMENT '仓位ID',
    quantity INT NOT NULL COMMENT '出库数量',
    operator VARCHAR(50) COMMENT '操作人',
    outbound_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '出库时间',
    remark VARCHAR(200) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (material_id) REFERENCES wms_material(id) ON DELETE CASCADE,
    FOREIGN KEY (location_id) REFERENCES wms_location(id) ON DELETE SET NULL
) COMMENT '出库记录表';

-- =============================================
-- 3. 插入演示数据
-- =============================================

-- 3.1 插入用户数据（密码统一为：123456，已使用BCrypt加密）
-- BCrypt加密后的123456密码格式（Spring Security BCryptPasswordEncoder生成）
INSERT INTO sys_user (username, password, nickname, phone, role) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '系统管理员', '13800138000', 'admin'),
('zhangsan', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '张三', '13800138001', 'user'),
('lisi', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '李四', '13800138002', 'user'),
('wangwu', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '王五', '13800138003', 'user'),
('zhaoliu', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '赵六', '13800138004', 'user');

-- 3.2 插入供应商数据
INSERT INTO wms_supplier (supplier_code, supplier_name, contact, phone, address) VALUES
('SUP001', '华为技术有限公司', '李经理', '021-12345678', '广东省深圳市龙岗区坂田华为基地'),
('SUP002', '小米科技有限公司', '王经理', '010-87654321', '北京市海淀区小米科技园'),
('SUP003', '联想集团有限公司', '刘经理', '010-88888888', '北京市海淀区上地信息路联想大厦'),
('SUP004', '比亚迪股份有限公司', '陈经理', '0755-89888888', '广东省深圳市龙岗区比亚迪路'),
('SUP005', '宁德时代新能源科技', '张经理', '0593-87654321', '福建省宁德市蕉城区'),
('SUP006', '京东方科技集团', '赵经理', '010-66666666', '北京市朝阳区酒仙桥路京东方大厦'),
('SUP007', '海康威视数字技术', '周经理', '0571-88888888', '浙江省杭州市滨江区海康威视科技园'),
('SUP008', '格力电器股份有限公司', '吴经理', '0756-88888888', '广东省珠海市前山金鸡西路格力电器'),
('SUP009', '美的集团股份有限公司', '郑经理', '0757-26338888', '广东省佛山市顺德区美的总部大楼'),
('SUP010', 'TCL科技集团股份', '孙经理', '0752-28288888', '广东省惠州市仲恺高新区TCL科技大厦');

-- 3.3 插入物料数据
INSERT INTO wms_material (material_code, material_name, category, unit, supplier_id, warning_stock, current_stock, remark) VALUES
('MAT001', '华为Mate60 Pro手机', '电子产品', '台', 1, 20, 150, '旗舰手机'),
('MAT002', '小米14 Ultra手机', '电子产品', '台', 2, 15, 80, '高端旗舰'),
('MAT003', '联想ThinkPad X1笔记本', '电子产品', '台', 3, 10, 45, '商务笔记本'),
('MAT004', '比亚迪汉EV电池组', '汽车配件', '组', 4, 5, 30, '新能源电池'),
('MAT005', '宁德时代动力电池', '汽车配件', '组', 5, 8, 25, '磷酸铁锂电池'),
('MAT006', '京东方OLED显示屏', '电子元件', '片', 6, 50, 200, '柔性屏幕'),
('MAT007', '海康威视监控摄像头', '安防设备', '个', 7, 30, 120, '4K高清摄像头'),
('MAT008', '格力空调压缩机', '家电配件', '台', 8, 10, 60, '变频压缩机'),
('MAT009', '美的冰箱压缩机', '家电配件', '台', 9, 12, 55, '节能压缩机'),
('MAT010', 'TCL液晶电视面板', '电子元件', '片', 10, 20, 90, '4K显示面板'),
('MAT011', '华为5G基站模块', '通信设备', '套', 1, 5, 15, '5G通信模块'),
('MAT012', '小米智能手环', '智能穿戴', '个', 2, 100, 500, '运动手环'),
('MAT013', '联想服务器主板', '电子元件', '块', 3, 8, 35, '企业级主板'),
('MAT014', '比亚迪电机控制器', '汽车配件', '个', 4, 6, 20, '电动车控制器'),
('MAT015', '宁德时代电池管理系统', '汽车配件', '套', 5, 5, 18, 'BMS系统'),
('MAT016', '京东方LCD显示模组', '电子元件', '个', 6, 40, 180, '液晶显示模组'),
('MAT017', '海康威视硬盘录像机', '安防设备', '台', 7, 15, 70, '网络录像机'),
('MAT018', '格力变频控制板', '家电配件', '块', 8, 25, 100, '空调控制板'),
('MAT019', '美的电机', '家电配件', '台', 9, 20, 85, '洗衣机电机'),
('MAT020', 'TCL音响系统', '电子产品', '套', 10, 10, 40, '家庭影院音响');

-- 3.4 插入仓位数据
INSERT INTO wms_location (location_code, location_name, warehouse, capacity, status, remark) VALUES
('A-01-01', 'A区1号货架1层', 'A仓库', 100, 'occupied', '电子产品存储区'),
('A-01-02', 'A区1号货架2层', 'A仓库', 100, 'occupied', '电子产品存储区'),
('A-01-03', 'A区1号货架3层', 'A仓库', 100, 'available', '电子产品存储区'),
('A-02-01', 'A区2号货架1层', 'A仓库', 150, 'occupied', '电子产品存储区'),
('A-02-02', 'A区2号货架2层', 'A仓库', 150, 'available', '电子产品存储区'),
('B-01-01', 'B区1号货架1层', 'B仓库', 200, 'occupied', '汽车配件存储区'),
('B-01-02', 'B区1号货架2层', 'B仓库', 200, 'occupied', '汽车配件存储区'),
('B-02-01', 'B区2号货架1层', 'B仓库', 180, 'available', '汽车配件存储区'),
('C-01-01', 'C区1号货架1层', 'C仓库', 120, 'occupied', '家电配件存储区'),
('C-01-02', 'C区1号货架2层', 'C仓库', 120, 'occupied', '家电配件存储区'),
('C-02-01', 'C区2号货架1层', 'C仓库', 150, 'available', '家电配件存储区'),
('D-01-01', 'D区1号货架1层', 'D仓库', 100, 'occupied', '安防设备存储区'),
('D-01-02', 'D区1号货架2层', 'D仓库', 100, 'available', '安防设备存储区'),
('E-01-01', 'E区1号货架1层', 'E仓库', 80, 'disabled', '维修中'),
('E-01-02', 'E区1号货架2层', 'E仓库', 80, 'available', '备用仓位');

-- 3.5 插入入库记录数据（最近30天的入库记录）
INSERT INTO wms_inbound (inbound_no, material_id, location_id, quantity, operator, inbound_time, remark) VALUES
('IN202603010001', 1, 1, 50, 'zhangsan', '2026-03-01 09:15:00', '新品到货'),
('IN202603010002', 2, 1, 30, 'zhangsan', '2026-03-01 10:30:00', '补货入库'),
('IN202603020001', 3, 2, 20, 'lisi', '2026-03-02 08:45:00', '采购入库'),
('IN202603020002', 6, 4, 100, 'lisi', '2026-03-02 14:20:00', '批量采购'),
('IN202603030001', 7, 12, 60, 'wangwu', '2026-03-03 09:00:00', '供应商直送'),
('IN202603030002', 12, 1, 200, 'wangwu', '2026-03-03 11:30:00', '促销备货'),
('IN202603040001', 4, 6, 15, 'zhangsan', '2026-03-04 10:15:00', '紧急采购'),
('IN202603040002', 5, 7, 10, 'zhangsan', '2026-03-04 15:45:00', '补充库存'),
('IN202603050001', 8, 9, 30, 'lisi', '2026-03-05 08:30:00', '定期采购'),
('IN202603050002', 9, 10, 25, 'lisi', '2026-03-05 13:00:00', '供应商送货'),
('IN202603060001', 10, 4, 40, 'wangwu', '2026-03-06 09:20:00', '新品入库'),
('IN202603060002', 1, 1, 50, 'wangwu', '2026-03-06 14:30:00', '补货'),
('IN202603070001', 11, 2, 10, 'zhangsan', '2026-03-07 10:00:00', '技术升级'),
('IN202603070002', 13, 2, 15, 'zhangsan', '2026-03-07 16:00:00', '采购入库'),
('IN202603080001', 14, 6, 8, 'lisi', '2026-03-08 09:30:00', '供应商直送'),
('IN202603080002', 15, 7, 7, 'lisi', '2026-03-08 11:45:00', '配套采购'),
('IN202603090001', 16, 4, 80, 'wangwu', '2026-03-09 08:15:00', '批量入库'),
('IN202603090002', 17, 12, 35, 'wangwu', '2026-03-09 15:20:00', '补充库存'),
('IN202603100001', 18, 9, 50, 'zhangsan', '2026-03-10 10:30:00', '定期采购'),
('IN202603100002', 19, 10, 40, 'zhangsan', '2026-03-10 14:00:00', '供应商送货'),
('IN202603110001', 20, 4, 20, 'lisi', '2026-03-11 09:00:00', '新品到货');

-- 3.6 插入出库记录数据（最近30天的出库记录）
INSERT INTO wms_outbound (outbound_no, material_id, location_id, quantity, operator, outbound_time, remark) VALUES
('OUT202603010001', 1, 1, 20, 'lisi', '2026-03-01 13:30:00', '销售出库'),
('OUT202603010002', 12, 1, 150, 'lisi', '2026-03-01 16:00:00', '促销发货'),
('OUT202603020001', 2, 1, 10, 'wangwu', '2026-03-02 10:15:00', '客户订单'),
('OUT202603020002', 6, 4, 50, 'wangwu', '2026-03-02 15:30:00', '生产领料'),
('OUT202603030001', 7, 12, 25, 'zhangsan', '2026-03-03 09:45:00', '项目使用'),
('OUT202603030002', 12, 1, 100, 'zhangsan', '2026-03-03 14:20:00', '批量出库'),
('OUT202603040001', 3, 2, 5, 'lisi', '2026-03-04 11:00:00', '内部调拨'),
('OUT202603040002', 4, 6, 5, 'lisi', '2026-03-04 16:15:00', '售后维修'),
('OUT202603050001', 5, 7, 5, 'wangwu', '2026-03-05 10:30:00', '研发测试'),
('OUT202603050002', 8, 9, 15, 'wangwu', '2026-03-05 15:00:00', '生产使用'),
('OUT202603060001', 9, 10, 10, 'zhangsan', '2026-03-06 09:00:00', '销售出库'),
('OUT202603060002', 10, 4, 20, 'zhangsan', '2026-03-06 13:45:00', '客户订单'),
('OUT202603070001', 1, 1, 30, 'lisi', '2026-03-07 11:30:00', '大客户订单'),
('OUT202603070002', 11, 2, 5, 'lisi', '2026-03-07 16:30:00', '项目交付'),
('OUT202603080001', 13, 2, 8, 'wangwu', '2026-03-08 10:00:00', '设备维护'),
('OUT202603080002', 14, 6, 3, 'wangwu', '2026-03-08 14:30:00', '售后服务'),
('OUT202603090001', 15, 7, 5, 'zhangsan', '2026-03-09 09:15:00', '技术支持'),
('OUT202603090002', 16, 4, 70, 'zhangsan', '2026-03-09 15:45:00', '生产领料'),
('OUT202603100001', 17, 12, 20, 'lisi', '2026-03-10 11:00:00', '项目安装'),
('OUT202603100002', 18, 9, 25, 'lisi', '2026-03-10 16:00:00', '销售出库'),
('OUT202603110001', 19, 10, 15, 'wangwu', '2026-03-11 10:00:00', '客户订单');

-- =============================================
-- 4. 创建视图（方便查询）
-- =============================================

-- 4.1 库存汇总视图
CREATE OR REPLACE VIEW v_inventory_summary AS
SELECT
    m.id,
    m.material_code,
    m.material_name,
    m.category,
    m.unit,
    m.current_stock,
    m.warning_stock,
    CASE
        WHEN m.current_stock <= m.warning_stock THEN '预警'
        WHEN m.current_stock <= m.warning_stock * 1.5 THEN '正常'
        ELSE '充足'
    END AS stock_status,
    s.supplier_name,
    s.contact AS supplier_contact
FROM wms_material m
LEFT JOIN wms_supplier s ON m.supplier_id = s.id;

-- 4.2 入库明细视图
CREATE OR REPLACE VIEW v_inbound_detail AS
SELECT
    i.id,
    i.inbound_no,
    m.material_code,
    m.material_name,
    m.category,
    i.quantity,
    m.unit,
    l.location_code,
    l.location_name,
    i.operator,
    i.inbound_time,
    i.remark
FROM wms_inbound i
LEFT JOIN wms_material m ON i.material_id = m.id
LEFT JOIN wms_location l ON i.location_id = l.id
ORDER BY i.inbound_time DESC;

-- 4.3 出库明细视图
CREATE OR REPLACE VIEW v_outbound_detail AS
SELECT
    o.id,
    o.outbound_no,
    m.material_code,
    m.material_name,
    m.category,
    o.quantity,
    m.unit,
    l.location_code,
    l.location_name,
    o.operator,
    o.outbound_time,
    o.remark
FROM wms_outbound o
LEFT JOIN wms_material m ON o.material_id = m.id
LEFT JOIN wms_location l ON o.location_id = l.id
ORDER BY o.outbound_time DESC;

-- =============================================
-- 5. 创建索引（提升查询性能）
-- =============================================

-- 用户表索引
CREATE INDEX idx_user_username ON sys_user(username);
CREATE INDEX idx_user_role ON sys_user(role);

-- 物料表索引
CREATE INDEX idx_material_code ON wms_material(material_code);
CREATE INDEX idx_material_category ON wms_material(category);
CREATE INDEX idx_material_supplier ON wms_material(supplier_id);

-- 供应商表索引
CREATE INDEX idx_supplier_code ON wms_supplier(supplier_code);

-- 仓位表索引
CREATE INDEX idx_location_code ON wms_location(location_code);
CREATE INDEX idx_location_status ON wms_location(status);

-- 入库记录索引
CREATE INDEX idx_inbound_no ON wms_inbound(inbound_no);
CREATE INDEX idx_inbound_material ON wms_inbound(material_id);
CREATE INDEX idx_inbound_time ON wms_inbound(inbound_time);

-- 出库记录索引
CREATE INDEX idx_outbound_no ON wms_outbound(outbound_no);
CREATE INDEX idx_outbound_material ON wms_outbound(material_id);
CREATE INDEX idx_outbound_time ON wms_outbound(outbound_time);

-- =============================================
-- 6. 数据统计查询（用于验证）
-- =============================================

-- 查看各表数据量
SELECT '用户表' AS table_name, COUNT(*) AS record_count FROM sys_user
UNION ALL
SELECT '供应商表', COUNT(*) FROM wms_supplier
UNION ALL
SELECT '物料表', COUNT(*) FROM wms_material
UNION ALL
SELECT '仓位表', COUNT(*) FROM wms_location
UNION ALL
SELECT '入库记录表', COUNT(*) FROM wms_inbound
UNION ALL
SELECT '出库记录表', COUNT(*) FROM wms_outbound;

-- 查看库存预警物料
SELECT material_code, material_name, current_stock, warning_stock
FROM wms_material
WHERE current_stock <= warning_stock
ORDER BY current_stock ASC;

-- 查看今日入库统计
SELECT COUNT(*) AS today_inbound_count, SUM(quantity) AS today_inbound_quantity
FROM wms_inbound
WHERE DATE(inbound_time) = CURDATE();

-- 查看今日出库统计
SELECT COUNT(*) AS today_outbound_count, SUM(quantity) AS today_outbound_quantity
FROM wms_outbound
WHERE DATE(outbound_time) = CURDATE();

-- =============================================
-- 初始化完成
-- =============================================
SELECT '数据库初始化完成！' AS message,
       '数据库名称: warehouse_db' AS db_name,
       '账号: root' AS username,
       '密码: 123456' AS password;
