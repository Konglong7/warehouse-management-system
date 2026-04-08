@echo off
echo ========================================
echo 智能仓库管理系统 - 启动验证
echo ========================================
echo.

echo [1/3] 检查Java版本...
java -version
echo.

echo [2/3] 检查MySQL连接...
mysql -u root -p123456 -e "SELECT 'MySQL连接成功!' AS status;"
echo.

echo [3/3] 启动说明
echo 后端启动: cd warehouse-backend && mvn spring-boot:run
echo 前端启动: cd warehouse-frontend && npm install && npm run dev
echo.

echo 访问地址:
echo 前端: http://localhost:3000
echo 后端: http://localhost:8080
echo.

echo 默认账号: admin / 123456
echo ========================================
pause
