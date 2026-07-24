package com.sim.shopping.application.settlement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sim.shopping.infrastructure.common.SystemConstants;
import com.sim.shopping.infrastructure.persistence.entity.SettlementRecordDO;
import com.sim.shopping.infrastructure.persistence.mapper.SettlementRecordMapper;
import com.sim.shopping.infrastructure.persistence.mapper.ShopMapper;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.settlement.SettlementRecordResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 结算服务，处理商家结算计算和收入统计
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Service
public class SettlementService {

    private final SettlementRecordMapper settlementRecordMapper;
    private final ShopMapper shopMapper;

    public SettlementService(SettlementRecordMapper settlementRecordMapper,
                              ShopMapper shopMapper) {
        this.settlementRecordMapper = settlementRecordMapper;
        this.shopMapper = shopMapper;
    }

    /**
     * 订单结算：确认收货后，将订单金额结算到商户账户
     */
    @Transactional(rollbackFor = Exception.class)
    public void settleOrder(Long orderId, String orderNo, Long shopId, BigDecimal amount) {
        // 1. 创建结算记录
        SettlementRecordDO record = new SettlementRecordDO();
        record.setShopId(shopId);
        record.setOrderId(orderId);
        record.setOrderNo(orderNo);
        record.setAmount(amount);
        record.setType(SystemConstants.SETTLEMENT_TYPE_ORDER);
        record.setStatus(SystemConstants.SETTLEMENT_STATUS_SETTLED);
        record.setDescription("订单结算: " + orderNo);
        settlementRecordMapper.insert(record);

        // 2. 原子增加商户余额和收入
        shopMapper.addIncome(shopId, amount);
    }

    /**
     * 退款结算扣减：退款时从商户余额扣减
     */
    @Transactional(rollbackFor = Exception.class)
    public void reverseSettlement(Long orderId, String orderNo, Long shopId, BigDecimal amount) {
        // 1. 创建退回结算记录（type=REFUND, amount为负数）
        SettlementRecordDO record = new SettlementRecordDO();
        record.setShopId(shopId);
        record.setOrderId(orderId);
        record.setOrderNo(orderNo);
        record.setAmount(amount.negate());
        record.setType(SystemConstants.REFUND_TYPE_REFUND_ONLY);
        record.setStatus(SystemConstants.SETTLEMENT_STATUS_SETTLED);
        record.setDescription("退款扣减: " + orderNo);
        settlementRecordMapper.insert(record);

        // 2. 原子扣减商户余额和收入
        shopMapper.deductIncome(shopId, amount.negate());
    }

    /**
     * 获取商户结算记录
     */
    public PageResponse<SettlementRecordResponse> getSettlementRecords(Long shopId, int page, int size) {
        Page<SettlementRecordDO> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<SettlementRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SettlementRecordDO::getShopId, shopId)
               .orderByDesc(SettlementRecordDO::getCreatedAt);

        IPage<SettlementRecordDO> result = settlementRecordMapper.selectPage(pageObj, wrapper);

        List<SettlementRecordResponse> list = result.getRecords().stream()
                .map(this::toSettlementRecordResponse)
                .collect(Collectors.toList());

        return PageResponse.of(list, result.getTotal(), page, size);
    }

    private SettlementRecordResponse toSettlementRecordResponse(SettlementRecordDO record) {
        SettlementRecordResponse resp = new SettlementRecordResponse();
        resp.setId(record.getId());
        resp.setOrderNo(record.getOrderNo());
        resp.setAmount(record.getAmount());
        resp.setType(record.getType());
        resp.setStatus(record.getStatus());
        resp.setDescription(record.getDescription());
        resp.setCreatedAt(record.getCreatedAt());
        return resp;
    }
}
