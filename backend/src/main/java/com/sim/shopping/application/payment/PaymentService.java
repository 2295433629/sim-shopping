package com.sim.shopping.application.payment;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.infrastructure.persistence.entity.OrderDO;
import com.sim.shopping.infrastructure.persistence.entity.PaymentDO;
import com.sim.shopping.infrastructure.persistence.mapper.OrderMapper;
import com.sim.shopping.infrastructure.persistence.mapper.PaymentMapper;
import com.sim.shopping.interfaces.dto.payment.PaymentResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Payment服务，处理相关业务逻辑
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Service
public class PaymentService {

    private final PaymentMapper paymentMapper;
    private final OrderMapper orderMapper;

    public PaymentService(PaymentMapper paymentMapper, OrderMapper orderMapper) {
        this.paymentMapper = paymentMapper;
        this.orderMapper = orderMapper;
    }

    /**
     * 创建Payment
     * @param userId userId
     * @param orderNo orderNo
     * @param paymentMethod paymentMethod
     * @return 返回结果
     */
    @Transactional
    public PaymentResponse createPayment(Long userId, String orderNo, String paymentMethod) {
        // Find the order
        LambdaQueryWrapper<OrderDO> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.eq(OrderDO::getOrderNo, orderNo);
        OrderDO order = orderMapper.selectOne(orderWrapper);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }
        if (!userId.equals(order.getUserId())) {
            throw new BusinessException(403, "无权操作此订单");
        }
        if (!"CREATED".equals(order.getStatus())) {
            throw new BusinessException(400, "订单状态不支持支付");
        }

        // Check if payment already exists for this order
        LambdaQueryWrapper<PaymentDO> existWrapper = new LambdaQueryWrapper<>();
        existWrapper.eq(PaymentDO::getOrderNo, orderNo);
        PaymentDO existingPayment = paymentMapper.selectOne(existWrapper);
        if (existingPayment != null && "SUCCESS".equals(existingPayment.getStatus())) {
            throw new BusinessException(400, "订单已支付");
        }

        // Generate payment number: PAY + yyyyMMddHHmmss + 3-digit random
        String paymentNo = "PAY" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + String.format("%03d", ThreadLocalRandom.current().nextInt(1000));

        // Create payment record
        PaymentDO payment = new PaymentDO();
        payment.setOrderId(order.getId());
        payment.setOrderNo(orderNo);
        payment.setPaymentNo(paymentNo);
        payment.setPaymentMethod(paymentMethod != null ? paymentMethod : "WECHAT");
        payment.setAmount(order.getPayAmount());
        payment.setStatus("SUCCESS");
        payment.setPaidAt(LocalDateTime.now());
        paymentMapper.insert(payment);

        // Update order status to PAID
        order.setStatus("PAID");
        order.setPaidAt(LocalDateTime.now());
        orderMapper.updateById(order);

        return toPaymentResponse(payment);
    }

    /**
     * 获取Payment Status
     * @param orderNo orderNo
     * @return 返回结果
     */
    public PaymentResponse getPaymentStatus(String orderNo) {
        LambdaQueryWrapper<PaymentDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentDO::getOrderNo, orderNo);
        wrapper.orderByDesc(PaymentDO::getCreatedAt);
        wrapper.last("LIMIT 1");
        PaymentDO payment = paymentMapper.selectOne(wrapper);
        if (payment == null) {
            throw new BusinessException(404, "支付记录不存在");
        }
        return toPaymentResponse(payment);
    }

    private PaymentResponse toPaymentResponse(PaymentDO payment) {
        PaymentResponse response = new PaymentResponse();
        response.setPaymentNo(payment.getPaymentNo());
        response.setOrderNo(payment.getOrderNo());
        response.setAmount(payment.getAmount());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setStatus(payment.getStatus());
        response.setPaidAt(payment.getPaidAt());
        return response;
    }
}
