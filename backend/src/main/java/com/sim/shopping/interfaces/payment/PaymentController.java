package com.sim.shopping.interfaces.payment;

import com.sim.shopping.application.payment.PaymentService;
import com.sim.shopping.infrastructure.security.SecurityUtils;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.payment.PaymentRequest;
import com.sim.shopping.interfaces.dto.payment.PaymentResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/{orderNo}")
    public ApiResponse<PaymentResponse> createPayment(@PathVariable String orderNo,
                                                       @RequestBody PaymentRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(paymentService.createPayment(userId, orderNo, request.getPaymentMethod()));
    }

    @GetMapping("/{orderNo}")
    public ApiResponse<PaymentResponse> getPaymentStatus(@PathVariable String orderNo) {
        return ApiResponse.success(paymentService.getPaymentStatus(orderNo));
    }
}
