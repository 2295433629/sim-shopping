#!/bin/bash
set -e

echo "=========================================="
echo "  模拟商城 Docker 部署脚本"
echo "=========================================="

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 检查 Docker
echo -e "${YELLOW}[1/6] 检查 Docker 环境...${NC}"
if ! command -v docker &> /dev/null; then
    echo -e "${RED}错误: Docker 未安装${NC}"
    exit 1
fi
if ! command -v docker-compose &> /dev/null && ! docker compose version &> /dev/null; then
    echo -e "${RED}错误: Docker Compose 未安装${NC}"
    exit 1
fi

echo -e "${GREEN}Docker 环境正常${NC}"

# 检查内存
echo -e "${YELLOW}[2/6] 检查系统资源...${NC}"
TOTAL_MEM=$(free -m | awk '/^Mem:/{print $2}')
echo "总内存: ${TOTAL_MEM}MB"
if [ "$TOTAL_MEM" -lt 1024 ]; then
    echo -e "${YELLOW}警告: 内存小于 1GB，建议升级服务器配置${NC}"
fi

# 拉取最新代码（可选）
echo -e "${YELLOW}[3/6] 拉取最新代码...${NC}"
if [ -d ".git" ]; then
    git pull origin main || git pull origin master || echo "无远程仓库，跳过拉取"
else
    echo "非 git 仓库，跳过拉取"
fi

# 清理旧容器和镜像
echo -e "${YELLOW}[4/6] 清理旧容器...${NC}"
docker-compose down --remove-orphans 2>/dev/null || true
docker system prune -f 2>/dev/null || true

# 构建并启动
echo -e "${YELLOW}[5/6] 构建并启动服务...${NC}"
docker-compose up -d --build

# 等待服务就绪
echo -e "${YELLOW}[6/6] 等待服务就绪...${NC}"
echo "正在检查 MySQL 健康状态..."
for i in {1..60}; do
    if docker-compose ps mysql | grep -q "healthy"; then
        echo -e "${GREEN}MySQL 已就绪${NC}"
        break
    fi
    echo -n "."
    sleep 2
done

echo ""
echo "正在检查后端服务..."
for i in {1..60}; do
    if curl -sf http://localhost:8080/api/public/categories > /dev/null 2>&1; then
        echo -e "${GREEN}后端服务已就绪${NC}"
        break
    fi
    echo -n "."
    sleep 2
done

echo ""
echo "=========================================="
echo -e "${GREEN}  部署完成！${NC}"
echo "=========================================="
echo ""
echo "访问地址:"
echo "  用户端:   http://<服务器IP>/"
echo "  商户端:   http://<服务器IP>/merchant/"
echo "  管理后台: http://<服务器IP>/admin/"
echo "  API文档:  http://<服务器IP>/swagger-ui.html"
echo ""
echo "查看日志:"
echo "  docker-compose logs -f"
echo ""
echo "管理命令:"
echo "  停止:     docker-compose stop"
echo "  重启:     docker-compose restart"
echo "  查看状态: docker-compose ps"
echo "  进入容器: docker exec -it shopping-mysql mysql -uroot -proot"
echo "=========================================="
