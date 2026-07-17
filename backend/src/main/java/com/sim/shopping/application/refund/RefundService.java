package com.sim.shopping.application.refund;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.domain.common.exception.OrderException;
import com.sim.shopping.infrastructure.persistence.entity.OrderDO;
import com.sim.shopping.infrastructure.persistence.entity.OrderItemDO;
import com.sim.shopping.infrastructure.persistence.entity.ProductSkuDO;
import com.sim.shopping.infrastructure.persistence.entity.RefundDO;
import com.sim.shopping.infrastructure.persistence.mapper.OrderItemMapper;
import com.sim.shopping.infrastructure.persistence.mapper.OrderMapper;
import com.sim.shopping.infrastructure.persistence.mapper.ProductSkuMapper;
import com.sim.shopping.infrastructure.persistence.mapper.RefundMapper;
import com.sim.shopping.application.settlement.SettlementService;
import com.sim.shopping.interfaces.dto.refund.RefundRequest;
import com.sim.shopping.interfaces.dto.refund.RefundVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 退款服务，处理退款申请、审核和退款处理
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Service
public class RefundService {

    private static final Logger log = LoggerFactory.getLogger(RefundService.class);

    private final RefundMapper refundMapper;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ProductSkuMapper productSkuMapper;
    private final SettlementService settlementService;

    public RefundService(RefundMapper refundMapper, OrderMapper orderMapper,
                          OrderItemMapper orderItemMapper, ProductSkuMapper productSkuMapper,
                          SettlementService settlementService) {
        this.refundMapper = refundMapper;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.productSkuMapper = productSkuMapper;
        this.settlementService = settlementService;
    }

    /**
     * 申请退款
     * @param userId userId
     * @param orderNo orderNo
     * @param req req
     * @return 返回结果
     */
    @Transactional
    public RefundVO applyRefund(Long userId, String orderNo, RefundRequest req) {
        OrderDO order = orderByNo(orderNo);
        if (order == null) {
            throw new OrderException.OrderNotFoundException("订单不存在: " + orderNo);
        }
        if (!userId.equals(order.getUserId())) {
            throw new BusinessException(403, "无权操作此订单");
        }

        // 已支付且未取消的订单才能退款
        if (!"PAID".equals(order.getStatus())
                && !"SHIPPED".equals(order.getStatus())
                && !"IN_TRANSIT".equals(order.getStatus())
                && !"DELIVERED".equals(order.getStatus())) {
            throw new BusinessException(400, "当前订单状态不支持退款");
        }

        // 检查是否在3天内
        if (order.getPaidAt() == null) {
            throw new BusinessException(400, "订单未支付，无法退款");
        }
        long daysSincePaid = ChronoUnit.DAYS.between(order.getPaidAt(), LocalDateTime.now());
        if (daysSincePaid > 3) {
            throw new BusinessException(400, "订单已超过3天，无法申请退款");
        }

        // 检查是否已有退款申请
        LambdaQueryWrapper<RefundDO> existWrapper = new LambdaQueryWrapper<>();
        existWrapper.eq(RefundDO::getOrderNo, orderNo);
        if (refundMapper.selectCount(existWrapper) > 0) {
            throw new BusinessException(400, "该订单已存在退款申请");
        }

        // 退款金额不能超过实付金额
        if (req.getAmount().compareTo(order.getPayAmount()) > 0) {
            throw new BusinessException(400, "退款金额不能超过实付金额");
        }

        RefundDO refund = new RefundDO();
        refund.setOrderId(order.getId());
        refund.setOrderNo(orderNo);
        refund.setUserId(userId);
        refund.setRefundType(req.getRefundType());
        refund.setStatus("PENDING");
        refund.setReason(req.getReason());
        refund.setAmount(req.getAmount());
        refundMapper.insert(refund);

        return toVO(refund);
    }

    /**
     * 获取Refund By Order No
     * @param orderNo orderNo
     * @return 返回结果
     */
    public RefundVO getRefundByOrderNo(String orderNo) {
        RefundDO refund = refundMapper.selectByOrderNo(orderNo);
        if (refund == null) {
            return null;
        }
        return toVO(refund);
    }

    /**
     * auto Approve Refund
     * @param orderNo orderNo
     * @return 返回结果
     */
    @Transactional
    public RefundVO autoApproveRefund(String orderNo) {
        RefundDO refund = refundMapper.selectByOrderNo(orderNo);
        if (refund == null || !"PENDING".equals(refund.getStatus())) {
            return null;
        }

        refund.setStatus("COMPLETED");
        refund.setShopResponse("模拟系统已自动同意退款");
        refund.setHandledAt(LocalDateTime.now());
        refund.setCompletedAt(LocalDateTime.now());
        refundMapper.updateById(refund);

        // 更新关联订单状态为 REFUNDED
        OrderDO order = orderByNo(orderNo);
        if (order != null) {
            order.setStatus("REFUNDED");
            orderMapper.updateById(order);

            // 恢复商品库存：查询订单项，逐个恢复 SKU 库存
            LambdaQueryWrapper<OrderItemDO> itemWrapper = new LambdaQueryWrapper<>();
            itemWrapper.eq(OrderItemDO::getOrderId, order.getId());
            List<OrderItemDO> items = orderItemMapper.selectList(itemWrapper);
            for (OrderItemDO item : items) {
                if (item.getSkuId() != null && item.getQuantity() != null) {
                    ProductSkuDO sku = productSkuMapper.selectById(item.getSkuId());
                    if (sku != null) {
                        sku.setStock(sku.getStock() + item.getQuantity());
                        productSkuMapper.updateById(sku);
                    }
                }
            }

            log.info("自动退款审批完成: orderNo={}, 恢复了{}个SKU库存", orderNo, items.size());

            // 退款结算扣减：从商户余额扣减退款金额
            BigDecimal refundAmount = refund.getAmount() != null ? refund.getAmount() : BigDecimal.ZERO;
            settlementService.reverseSettlement(order.getId(), orderNo, order.getShopId(), refundAmount);
        }

        return toVO(refund);
    }

    private OrderDO orderByNo(String orderNo) {
        LambdaQueryWrapper<OrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDO::getOrderNo, orderNo);
        return orderMapper.selectOne(wrapper);
    }

    private RefundVO toVO(RefundDO refund) {
        RefundVO vo = new RefundVO();
        vo.setRefundId(refund.getId());
        vo.setOrderNo(refund.getOrderNo());
        vo.setRefundType(refund.getRefundType());
        vo.setStatus(refund.getStatus());
        vo.setReason(refund.getReason());
        vo.setAmount(refund.getAmount());
        vo.setShopResponse(refund.getShopResponse());
        vo.setHandledAt(refund.getHandledAt());
        vo.setCompletedAt(refund.getCompletedAt());
        vo.setCreatedAt(refund.getCreatedAt());
        return vo;
    }
}
