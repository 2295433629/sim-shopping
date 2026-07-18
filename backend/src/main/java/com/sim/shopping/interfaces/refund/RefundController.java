package com.sim.shopping.interfaces.refund;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sim.shopping.application.refund.RefundService;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.infrastructure.persistence.entity.OrderDO;
import com.sim.shopping.infrastructure.persistence.mapper.OrderMapper;
import com.sim.shopping.infrastructure.security.SecurityUtils;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.refund.RefundRequest;
import com.sim.shopping.interfaces.dto.refund.RefundVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 退款管理控制器，处理订单退款申请和审核
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/user/orders")
public class RefundController {

    private final RefundService refundService;
    private final OrderMapper orderMapper;

    public RefundController(RefundService refundService, OrderMapper orderMapper) {
        this.refundService = refundService;
        this.orderMapper = orderMapper;
    }

    /**
     * 申请退款
     * @return 返回结果
     */
    @PostMapping("/{orderNo}/refund")
    public ApiResponse<RefundVO> applyRefund(@PathVariable String orderNo,
                                              @Valid @RequestBody RefundRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(refundService.applyRefund(userId, orderNo, request));
    }

    /**
     * 获取Refund
     * @param orderNo orderNo
     * @return 返回结果
     */
    @GetMapping("/{orderNo}/refund")
    public ApiResponse<RefundVO> getRefund(@PathVariable String orderNo) {
        Long userId = SecurityUtils.getCurrentUserId();
        // 校验订单归属
        LambdaQueryWrapper<OrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDO::getOrderNo, orderNo);
        OrderDO order = orderMapper.selectOne(wrapper);
        if (order != null && !userId.equals(order.getUserId())) {
            throw new BusinessException(403, "无权查看此订单的退款信息");
        }
        RefundVO refund = refundService.getRefundByOrderNo(orderNo);
        return ApiResponse.success(refund);
    }
}
