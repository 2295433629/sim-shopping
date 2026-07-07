package com.sim.shopping.application.points;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.infrastructure.persistence.entity.PointsProductDO;
import com.sim.shopping.infrastructure.persistence.entity.PointsRecordDO;
import com.sim.shopping.infrastructure.persistence.entity.UserPointsDO;
import com.sim.shopping.infrastructure.persistence.mapper.PointsProductMapper;
import com.sim.shopping.infrastructure.persistence.mapper.PointsRecordMapper;
import com.sim.shopping.infrastructure.persistence.mapper.UserPointsMapper;
import com.sim.shopping.interfaces.dto.points.PointsProductResponse;
import com.sim.shopping.interfaces.dto.points.PointsRecordResponse;
import com.sim.shopping.interfaces.dto.points.PointsStatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PointsAdminService {

    @Autowired
    private PointsRecordMapper pointsRecordMapper;

    @Autowired
    private PointsProductMapper pointsProductMapper;

    @Autowired
    private UserPointsMapper userPointsMapper;

    public IPage<PointsRecordResponse> getAllRecords(int page, int size, Long userId, String type, String source) {
        Page<PointsRecordDO> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<PointsRecordDO> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(PointsRecordDO::getUserId, userId);
        }
        if (StringUtils.hasText(type)) {
            wrapper.eq(PointsRecordDO::getType, type);
        }
        if (StringUtils.hasText(source)) {
            wrapper.eq(PointsRecordDO::getSource, source);
        }
        wrapper.orderByDesc(PointsRecordDO::getCreatedAt);

        IPage<PointsRecordDO> recordPage = pointsRecordMapper.selectPage(pageParam, wrapper);
        List<PointsRecordResponse> list = recordPage.getRecords().stream()
                .map(this::convertToRecordResponse)
                .collect(Collectors.toList());

        Page<PointsRecordResponse> resultPage = new Page<>(recordPage.getCurrent(), recordPage.getSize(), recordPage.getTotal());
        resultPage.setRecords(list);
        return resultPage;
    }

    public IPage<PointsProductResponse> getProductList(int page, int size, String status, String keyword) {
        Page<PointsProductDO> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<PointsProductDO> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(status)) {
            wrapper.eq(PointsProductDO::getStatus, status);
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.like(PointsProductDO::getProductName, keyword);
        }
        wrapper.orderByDesc(PointsProductDO::getCreatedAt);

        IPage<PointsProductDO> productPage = pointsProductMapper.selectPage(pageParam, wrapper);
        List<PointsProductResponse> list = productPage.getRecords().stream()
                .map(this::convertToProductResponse)
                .collect(Collectors.toList());

        Page<PointsProductResponse> resultPage = new Page<>(productPage.getCurrent(), productPage.getSize(), productPage.getTotal());
        resultPage.setRecords(list);
        return resultPage;
    }

    public void createProduct(PointsProductDO product) {
        if (product == null) {
            throw new BusinessException(400, "商品信息不能为空");
        }
        if (!StringUtils.hasText(product.getProductName())) {
            throw new BusinessException(400, "商品名称不能为空");
        }
        if (product.getPointsRequired() == null || product.getPointsRequired() <= 0) {
            throw new BusinessException(400, "所需积分必须大于0");
        }
        if (product.getStock() == null || product.getStock() < 0) {
            throw new BusinessException(400, "库存不能为负数");
        }
        if (!StringUtils.hasText(product.getStatus())) {
            product.setStatus("ACTIVE");
        }
        pointsProductMapper.insert(product);
    }

    public void updateProduct(Long id, PointsProductDO product) {
        if (id == null) {
            throw new BusinessException(400, "商品ID不能为空");
        }
        if (product == null) {
            throw new BusinessException(400, "商品信息不能为空");
        }
        PointsProductDO existing = pointsProductMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(404, "商品不存在");
        }

        LambdaUpdateWrapper<PointsProductDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(PointsProductDO::getId, id);
        if (StringUtils.hasText(product.getProductName())) {
            wrapper.set(PointsProductDO::getProductName, product.getProductName());
        }
        if (StringUtils.hasText(product.getDescription())) {
            wrapper.set(PointsProductDO::getDescription, product.getDescription());
        }
        if (StringUtils.hasText(product.getImageUrl())) {
            wrapper.set(PointsProductDO::getImageUrl, product.getImageUrl());
        }
        if (product.getPointsRequired() != null && product.getPointsRequired() > 0) {
            wrapper.set(PointsProductDO::getPointsRequired, product.getPointsRequired());
        }
        if (product.getStock() != null && product.getStock() >= 0) {
            wrapper.set(PointsProductDO::getStock, product.getStock());
        }
        if (StringUtils.hasText(product.getStatus())) {
            wrapper.set(PointsProductDO::getStatus, product.getStatus());
        }
        pointsProductMapper.update(null, wrapper);
    }

    public void deleteProduct(Long id) {
        if (id == null) {
            throw new BusinessException(400, "商品ID不能为空");
        }
        PointsProductDO existing = pointsProductMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(404, "商品不存在");
        }
        pointsProductMapper.deleteById(id);
    }

    public PointsStatisticsVO getStatistics() {
        Long totalUsers = userPointsMapper.selectCount(new LambdaQueryWrapper<>());

        LambdaQueryWrapper<PointsRecordDO> earnWrapper = new LambdaQueryWrapper<>();
        earnWrapper.eq(PointsRecordDO::getType, "EARN");
        List<PointsRecordDO> earnRecords = pointsRecordMapper.selectList(earnWrapper);
        int totalEarned = earnRecords.stream()
                .mapToInt(r -> r.getPoints() != null ? r.getPoints() : 0)
                .sum();

        LambdaQueryWrapper<PointsRecordDO> spendWrapper = new LambdaQueryWrapper<>();
        spendWrapper.eq(PointsRecordDO::getType, "SPEND");
        List<PointsRecordDO> spendRecords = pointsRecordMapper.selectList(spendWrapper);
        int totalSpent = spendRecords.stream()
                .mapToInt(r -> r.getPoints() != null ? r.getPoints() : 0)
                .sum();

        LambdaQueryWrapper<PointsRecordDO> exchangeWrapper = new LambdaQueryWrapper<>();
        exchangeWrapper.eq(PointsRecordDO::getSource, "EXCHANGE");
        Long totalExchanges = pointsRecordMapper.selectCount(exchangeWrapper);

        return new PointsStatisticsVO(
                totalUsers != null ? totalUsers : 0L,
                totalEarned,
                totalSpent,
                totalExchanges != null ? totalExchanges.intValue() : 0
        );
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

    private PointsProductResponse convertToProductResponse(PointsProductDO product) {
        PointsProductResponse response = new PointsProductResponse();
        response.setId(product.getId());
        response.setProductName(product.getProductName());
        response.setDescription(product.getDescription());
        response.setImageUrl(product.getImageUrl());
        response.setPointsRequired(product.getPointsRequired());
        response.setStock(product.getStock());
        response.setStatus(product.getStatus());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        return response;
    }
}
