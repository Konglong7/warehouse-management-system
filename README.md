# 智能仓库管理系统 - 快速启动指南

## 项目简介

基于SpringBoot与Vue的智能仓库管理系统,实现物料管理、入库出库、库存查询、数据可视化等核心功能。

## 技术栈

### 后端
- SpringBoot 2.7.18
- MyBatis Plus 3.5.5
- MySQL 8.0
- Lombok
- Hutool

### 前端
- Vue 3
- Vite
- Naive UI
- ECharts
- Axios
- Pinia

## 环境要求

- JDK 1.8+
- Maven 3.6+
- Node.js 16+
- MySQL 8.0
- Navicat Premium 17 (可选)

## 快速启动

### 第一步:数据库初始化

1. 启动MySQL服务
2. 使用Navicat或命令行连接MySQL
   - 账号: root
   - 密码: 123456
3. 执行SQL文件: `init_warehouse.sql`

```bash
mysql -u root -p123456 < init_warehouse.sql
```

### 第二步:启动后端

```bash
# 进入后端目录
cd warehouse-backend

# 安装依赖
mvn clean install

# 启动项目
mvn spring-boot:run
```

后端启动成功后,访问: http://localhost:8080

### 第三步:启动前端

```bash
# 进入前端目录
cd warehouse-frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

前端启动成功后,访问: http://localhost:3000

## 默认账号

### 管理员账号
- 用户名: admin
- 密码: 123456

### 普通用户账号
- 用户名: zhangsan / lisi / wangwu
- 密码: 123456

## 项目结构

```
warehouse-backend/          # 后端项目
├── src/main/java/com/warehouse/
│   ├── WmsApplication.java         # 启动类
│   ├── controller/                 # 控制层
│   ├── service/                    # 服务层
│   ├── mapper/                     # 数据访问层
│   ├── entity/                     # 实体类
│   ├── common/                     # 公共类
│   └── config/                     # 配置类
├── src/main/resources/
│   └── application.yml             # 配置文件
└── pom.xml                         # Maven配置

warehouse-frontend/         # 前端项目
├── src/
│   ├── views/                      # 页面组件
│   ├── components/                 # 公共组件
│   ├── api/                        # API接口
│   ├── router/                     # 路由配置
│   ├── store/                      # 状态管理
│   ├── utils/                      # 工具函数
│   ├── App.vue                     # 根组件
│   └── main.js                     # 入口文件
├── index.html                      # HTML模板
├── vite.config.js                  # Vite配置
└── package.json                    # 依赖配置
```

## 核心功能

### 1. 用户管理
- 用户登录/注册
- 密码修改
- 用户列表管理

### 2. 物料管理
- 物料信息CRUD
- 物料搜索
- 库存预警

### 3. 供应商管理
- 供应商信息CRUD

### 4. 仓位管理
- 仓位信息CRUD
- 仓位状态管理

### 5. 入库管理
- 入库单据录入
- 自动生成入库单号
- 自动更新库存

### 6. 出库管理
- 出库单据录入
- 库存不足校验
- 自动更新库存

### 7. 库存查询
- 库存列表查询
- 库存状态显示
- 预警物料统计

### 8. 数据可视化
- 库存统计柱状图
- 物料分类饼图
- 数据统计卡片

## API接口

### 用户模块
- POST /api/user/login - 用户登录
- POST /api/user/register - 用户注册
- GET /api/user/list - 用户列表
- PUT /api/user/update - 更新用户
- PUT /api/user/password - 修改密码

### 物料模块
- GET /api/material/list - 物料列表
- POST /api/material - 新增物料
- PUT /api/material/{id} - 更新物料
- DELETE /api/material/{id} - 删除物料
- GET /api/material/warning - 预警物料

### 供应商模块
- GET /api/supplier/list - 供应商列表
- POST /api/supplier - 新增供应商
- PUT /api/supplier/{id} - 更新供应商
- DELETE /api/supplier/{id} - 删除供应商

### 仓位模块
- GET /api/location/list - 仓位列表
- POST /api/location - 新增仓位
- PUT /api/location/{id} - 更新仓位
- DELETE /api/location/{id} - 删除仓位

### 入库模块
- GET /api/inbound/list - 入库记录列表
- POST /api/inbound - 新增入库

### 出库模块
- GET /api/outbound/list - 出库记录列表
- POST /api/outbound - 新增出库

### 统计模块
- GET /api/stats/dashboard - 首页统计
- GET /api/stats/inventory - 库存统计
- GET /api/stats/trend - 趋势统计

## 常见问题

### 1. 后端启动失败
- 检查MySQL是否启动
- 检查数据库连接配置(application.yml)
- 检查JDK版本是否为1.8+

### 2. 前端启动失败
- 检查Node.js版本是否为16+
- 删除node_modules,重新npm install
- 检查端口3000是否被占用

### 3. 前后端无法通信
- 检查后端是否启动成功
- 检查跨域配置(CorsConfig.java)
- 检查前端代理配置(vite.config.js)

### 4. 数据库连接失败
- 检查MySQL服务是否启动
- 检查账号密码是否正确
- 检查数据库warehouse_db是否存在

## 开发建议

1. 使用IDEA开发后端
2. 使用VSCode开发前端
3. 使用Apifox测试接口
4. 使用Navicat管理数据库

## 项目演示

1. 登录系统(admin/123456)
2. 查看Dashboard数据统计和图表
3. 管理物料信息
4. 执行入库操作,观察库存变化
5. 执行出库操作,观察库存变化
6. 查看库存预警提示

## 联系方式

如有问题,请查看功能测试文档或联系开发团队。

---

**版本**: v1.0.0
**更新日期**: 2026-03-11
