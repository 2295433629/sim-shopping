package com.sim.shopping.application.flashsale;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.infrastructure.common.SystemConstants;
import com.sim.shopping.infrastructure.persistence.entity.FlashSaleDO;
import com.sim.shopping.infrastructure.persistence.mapper.FlashSaleMapper;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.flashsale.FlashSaleResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * FlashSaleAdmin服务，处理相关业务逻辑
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Service
public class FlashSaleAdminService {



    private final FlashSaleMapper flashSaleMapper;

    public FlashSaleAdminService(FlashSaleMapper flashSaleMapper) {
        this.flashSaleMapper = flashSaleMapper;
    }

    /**
     * 获取Flash Sale List
     * @param page page
     * @param size size
     * @param status status
     * @param keyword keyword
     * @return 返回结果
     */
    public PageResponse<FlashSaleResponse> getFlashSaleList(int page, int size, String status, String keyword) {
        if (page < 1) {
            page = 1;
        }
        if (size < 1) {
            size = 10;
        }

        Page<FlashSaleDO> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<FlashSaleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlashSaleDO::getDeleted, 0);

        if (status != null && !status.isEmpty()) {
            wrapper.eq(FlashSaleDO::getStatus, status);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(FlashSaleDO::getId, keyword)
                    .or().like(FlashSaleDO::getProductId, keyword));
        }
        wrapper.orderByDesc(FlashSaleDO::getCreatedAt);

        IPage<FlashSaleDO> result = flashSaleMapper.selectPage(pageObj, wrapper);
        List<FlashSaleResponse> list = result.getRecords().stream()
                .map(this::convertToFlashSaleResponse)
                .collect(Collectors.toList());

        return PageResponse.of(list, result.getTotal(), page, size);
    }

    /**
     * 创建秒杀活动
     * @param sale sale
     */
    public void createFlashSale(FlashSaleDO sale) {
        if (sale == null) {
            throw new BusinessException(400, "秒杀活动信息不能为空");
        }
        if (sale.getProductId() == null) {
            throw new BusinessException(400, "商品ID不能为空");
        }
        if (sale.getFlashPrice() == null) {
            throw new BusinessException(400, "秒杀价格不能为空");
        }
        if (sale.getStock() == null || sale.getStock() < 0) {
            throw new BusinessException(400, "库存不能为空或负数");
        }
        if (sale.getStartTime() == null || sale.getEndTime() == null) {
            throw new BusinessException(400, "开始时间和结束时间不能为空");
        }
        if (sale.getStartTime().isAfter(sale.getEndTime())) {
            throw new BusinessException(400, "开始时间不能晚于结束时间");
        }
        if (sale.getLimitPerUser() == null || sale.getLimitPerUser() < 1) {
            sale.setLimitPerUser(1);
        }
        if (sale.getStatus() == null || sale.getStatus().isEmpty()) {
            sale.setStatus(SystemConstants.STATUS_INACTIVE);
        }
        if (sale.getSoldCount() == null) {
            sale.setSoldCount(0);
        }

        LocalDateTime now = LocalDateTime.now();
        if (SystemConstants.STATUS_ACTIVE.equals(sale.getStatus()) && sale.getEndTime().isBefore(now)) {
            throw new BusinessException(400, "活动已结束，无法设置为进行中状态");
        }

        flashSaleMapper.insert(sale);
    }

    /**
     * 更新秒杀活动
     * @param id id
     * @param sale sale
     */
    public void updateFlashSale(Long id, FlashSaleDO sale) {
        if (id == null) {
            throw new BusinessException(400, "秒杀活动ID不能为空");
        }
        if (sale == null) {
            throw new BusinessException(400, "秒杀活动信息不能为空");
        }

        FlashSaleDO existing = flashSaleMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(404, "秒杀活动不存在");
        }

        if (sale.getProductId() != null) {
            existing.setProductId(sale.getProductId());
        }
        if (sale.getOriginalPrice() != null) {
            existing.setOriginalPrice(sale.getOriginalPrice());
        }
        if (sale.getFlashPrice() != null) {
            existing.setFlashPrice(sale.getFlashPrice());
        }
        if (sale.getStock() != null) {
            if (sale.getStock() < 0) {
                throw new BusinessException(400, "库存不能为负数");
            }
            existing.setStock(sale.getStock());
        }
        if (sale.getStartTime() != null) {
            existing.setStartTime(sale.getStartTime());
        }
        if (sale.getEndTime() != null) {
            existing.setEndTime(sale.getEndTime());
        }
        if (sale.getLimitPerUser() != null) {
            existing.setLimitPerUser(sale.getLimitPerUser());
        }
        if (sale.getStatus() != null && !sale.getStatus().isEmpty()) {
            existing.setStatus(sale.getStatus());
        }

        if (existing.getStartTime() != null && existing.getEndTime() != null
                && existing.getStartTime().isAfter(existing.getEndTime())) {
            throw new BusinessException(400, "开始时间不能晚于结束时间");
        }

        LocalDateTime now = LocalDateTime.now();
        if (SystemConstants.STATUS_ACTIVE.equals(existing.getStatus()) && existing.getEndTime() != null && existing.getEndTime().isBefore(now)) {
            throw new BusinessException(400, "活动已结束，无法设置为进行中状态");
        }

        flashSaleMapper.updateById(existing);
    }

    /**
     * 删除秒杀活动
     * @param id id
     */
    public void deleteFlashSale(Long id) {
        if (id == null) {
            throw new BusinessException(400, "秒杀活动ID不能为空");
        }
        FlashSaleDO existing = flashSaleMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(404, "秒杀活动不存在");
        }
        flashSaleMapper.deleteById(id);
    }

    private FlashSaleResponse convertToFlashSaleResponse(FlashSaleDO sale) {
        FlashSaleResponse resp = new FlashSaleResponse();
        resp.setSaleId(sale.getId());
        resp.setProductId(sale.getProductId());
        resp.setOriginalPrice(sale.getOriginalPrice());
        resp.setFlashPrice(sale.getFlashPrice());
        resp.setStock(sale.getStock());
        resp.setSoldCount(sale.getSoldCount());
        resp.setStartTime(sale.getStartTime());
        resp.setEndTime(sale.getEndTime());
        resp.setLimitPerUser(sale.getLimitPerUser());
        resp.setStatus(sale.getStatus());
        return resp;
    }
}
