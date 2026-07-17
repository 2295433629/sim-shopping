package com.sim.shopping.interfaces.payment;

import com.sim.shopping.application.payment.PaymentService;
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

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
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
        return ApiResponse.success(paymentService.getPaymentStatus(orderNo));
    }
}
