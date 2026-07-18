package com.sim.shopping.interfaces.payment;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sim.shopping.application.payment.PaymentService;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.infrastructure.persistence.entity.OrderDO;
import com.sim.shopping.infrastructure.persistence.mapper.OrderMapper;
import com.sim.shopping.infrastructure.security.SecurityUtils;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.payment.PaymentRequest;
import com.sim.shopping.interfaces.dto.payment.PaymentResponse;
import org.springframework.web.bind.annotation.*;

/**
 * Payment控制器，处理相关业务请求
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/user/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderMapper orderMapper;

    public PaymentController(PaymentService paymentService, OrderMapper orderMapper) {
        this.paymentService = paymentService;
        this.orderMapper = orderMapper;
    }

    /**
     * 创建Payment
     * @return 返回结果
     */
    @PostMapping("/{orderNo}")
    public ApiResponse<PaymentResponse> createPayment(@PathVariable String orderNo,
                                                       @RequestBody PaymentRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(paymentService.createPayment(userId, orderNo, request.getPaymentMethod()));
    }

    /**
     * 获取Payment Status
     * @param orderNo orderNo
     * @return 返回结果
     */
    @GetMapping("/{orderNo}")
    public ApiResponse<PaymentResponse> getPaymentStatus(@PathVariable String orderNo) {
        Long userId = SecurityUtils.getCurrentUserId();
        // 校验订单归属
        LambdaQueryWrapper<OrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDO::getOrderNo, orderNo);
        OrderDO order = orderMapper.selectOne(wrapper);
        if (order != null && !userId.equals(order.getUserId())) {
            throw new BusinessException(403, "无权查看此订单的支付信息");
        }
        return ApiResponse.success(paymentService.getPaymentStatus(orderNo));
    }
}
