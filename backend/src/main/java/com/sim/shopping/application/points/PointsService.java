package com.sim.shopping.application.points;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.infrastructure.persistence.entity.PointsProductDO;
import com.sim.shopping.infrastructure.persistence.entity.PointsRecordDO;
import com.sim.shopping.infrastructure.persistence.entity.UserPointsDO;
import com.sim.shopping.infrastructure.persistence.mapper.PointsProductMapper;
import com.sim.shopping.infrastructure.persistence.mapper.PointsRecordMapper;
import com.sim.shopping.infrastructure.persistence.mapper.UserPointsMapper;
import com.sim.shopping.interfaces.dto.points.ExchangeResponse;
import com.sim.shopping.interfaces.dto.points.PointsBalanceVO;
import com.sim.shopping.interfaces.dto.points.PointsRecordResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PointsService {

    private final UserPointsMapper userPointsMapper;
    private final PointsRecordMapper pointsRecordMapper;
    private final PointsProductMapper pointsProductMapper;

    public PointsService(UserPointsMapper userPointsMapper, PointsRecordMapper pointsRecordMapper, PointsProductMapper pointsProductMapper) {
        this.userPointsMapper = userPointsMapper;
        this.pointsRecordMapper = pointsRecordMapper;
        this.pointsProductMapper = pointsProductMapper;
    }

    public PointsBalanceVO getPointsBalance(Long userId) {
        if (userId == null) {
            throw new BusinessException(400, "用户ID不能为空");
        }
        LambdaQueryWrapper<UserPointsDO> userPointsWrapper = new LambdaQueryWrapper<>();
        userPointsWrapper.eq(UserPointsDO::getUserId, userId);
        UserPointsDO userPoints = userPointsMapper.selectOne(userPointsWrapper);
        if (userPoints == null) {
            throw new BusinessException(404, "用户积分账户不存在");
        }

        Integer currentPoints = userPoints.getAvailablePoints() != null ? userPoints.getAvailablePoints() : 0;

        LambdaQueryWrapper<PointsRecordDO> earnWrapper = new LambdaQueryWrapper<>();
        earnWrapper.eq(PointsRecordDO::getUserId, userId).eq(PointsRecordDO::getType, "EARN");
        List<PointsRecordDO> earnRecords = pointsRecordMapper.selectList(earnWrapper);
        int totalEarned = earnRecords.stream()
                .mapToInt(r -> r.getPoints() != null ? r.getPoints() : 0)
                .sum();

        LambdaQueryWrapper<PointsRecordDO> spendWrapper = new LambdaQueryWrapper<>();
        spendWrapper.eq(PointsRecordDO::getUserId, userId).eq(PointsRecordDO::getType, "SPEND");
        List<PointsRecordDO> spendRecords = pointsRecordMapper.selectList(spendWrapper);
        int totalSpent = spendRecords.stream()
                .mapToInt(r -> r.getPoints() != null ? r.getPoints() : 0)
                .sum();

        return new PointsBalanceVO(currentPoints, totalEarned, totalSpent);
    }

    public IPage<PointsRecordResponse> getPointsRecords(Long userId, int page, int size, String type) {
        if (userId == null) {
            throw new BusinessException(400, "用户ID不能为空");
        }
        Page<PointsRecordDO> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<PointsRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PointsRecordDO::getUserId, userId);
        if (StringUtils.hasText(type)) {
            wrapper.eq(PointsRecordDO::getType, type);
        }
        wrapper.orderByDesc(PointsRecordDO::getCreatedAt);

        IPage<PointsRecordDO> recordPage = pointsRecordMapper.selectPage(pageParam, wrapper);
        List<PointsRecordResponse> list = recordPage.getRecords().stream().map(this::convertToRecordResponse).collect(Collectors.toList());

        Page<PointsRecordResponse> resultPage = new Page<>(recordPage.getCurrent(), recordPage.getSize(), recordPage.getTotal());
        resultPage.setRecords(list);
        return resultPage;
    }

    @Transactional(rollbackFor = Exception.class)
    public ExchangeResponse exchangeProduct(Long userId, Long productId, Integer quantity) {
        if (userId == null) {
            throw new BusinessException(400, "用户ID不能为空");
        }
        if (productId == null) {
            throw new BusinessException(400, "商品ID不能为空");
        }
        if (quantity == null || quantity <= 0) {
            throw new BusinessException(400, "兑换数量必须大于0");
        }

        PointsProductDO product = pointsProductMapper.selectById(productId);
        if (product == null) {
            throw new BusinessException(404, "商品不存在");
        }
        if (!"ACTIVE".equals(product.getStatus())) {
            throw new BusinessException(400, "商品未上架或已下架");
        }
        Integer stock = product.getStock() != null ? product.getStock() : 0;
        if (stock < quantity) {
            throw new BusinessException(400, "商品库存不足");
        }

        Integer pointsRequired = product.getPointsRequired() != null ? product.getPointsRequired() : 0;
        int totalPointsNeeded = pointsRequired * quantity;

        LambdaQueryWrapper<UserPointsDO> upWrapper = new LambdaQueryWrapper<>();
        upWrapper.eq(UserPointsDO::getUserId, userId);
        UserPointsDO userPoints = userPointsMapper.selectOne(upWrapper);
        if (userPoints == null) {
            throw new BusinessException(404, "用户积分账户不存在");
        }
        Integer availablePoints = userPoints.getAvailablePoints() != null ? userPoints.getAvailablePoints() : 0;
        if (availablePoints < totalPointsNeeded) {
            throw new BusinessException(400, "积分不足");
        }

        // 原子扣减积分商品库存（防止并发超兑换）
        int stockRows = pointsProductMapper.deductStock(productId, quantity);
        if (stockRows == 0) {
            throw new BusinessException(400, "商品库存不足");
        }

        // 原子扣减用户积分（防止并发超扣）
        int pointsRows = userPointsMapper.deductPoints(userId, totalPointsNeeded);
        if (pointsRows == 0) {
            throw new BusinessException(400, "积分不足或积分账户异常");
        }

        // 生成积分记录
        PointsRecordDO record = new PointsRecordDO();
        record.setUserId(userId);
        record.setPoints(totalPointsNeeded);
        record.setType("SPEND");
        record.setSource("EXCHANGE");
        record.setDescription("积分兑换商品: " + product.getProductName() + " x" + quantity);
        record.setRelatedId(productId);
        pointsRecordMapper.insert(record);

        // 查询最新积分余额
        UserPointsDO updatedPoints = userPointsMapper.selectByUserId(userId);
        int remainingPoints = updatedPoints != null && updatedPoints.getAvailablePoints() != null
                ? updatedPoints.getAvailablePoints() : 0;

        ExchangeResponse response = new ExchangeResponse();
        response.setRecordId(record.getId());
        response.setProductId(productId);
        response.setProductName(product.getProductName());
        response.setQuantity(quantity);
        response.setTotalPoints(totalPointsNeeded);
        response.setRemainingPoints(remainingPoints);
        response.setExchangeTime(LocalDateTime.now());
        return response;
    }

    private PointsRecordResponse convertToRecordResponse(PointsRecordDO record) {
        PointsRecordResponse response = new PointsRecordResponse();
        response.setId(record.getId());
        response.setUserId(record.getUserId());
        response.setPoints(record.getPoints());
        response.setType(record.getType());
        response.setSource(record.getSource());
        response.setDescription(record.getDescription());
        response.setRelatedId(record.getRelatedId());
        response.setCreatedAt(record.getCreatedAt());
        return response;
    }
}
