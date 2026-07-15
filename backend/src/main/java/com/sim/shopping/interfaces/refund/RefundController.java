package com.sim.shopping.interfaces.refund;

import com.sim.shopping.application.refund.RefundService;
import com.sim.shopping.infrastructure.security.SecurityUtils;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.refund.RefundRequest;
import com.sim.shopping.interfaces.dto.refund.RefundVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/orders")
public class RefundController {

    private final RefundService refundService;

    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    @PostMapping("/{orderNo}/refund")
    public ApiResponse<RefundVO> applyRefund(@PathVariable String orderNo,
                                              @Valid @RequestBody RefundRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(refundService.applyRefund(userId, orderNo, request));
    }

    @GetMapping("/{orderNo}/refund")
    public ApiResponse<RefundVO> getRefund(@PathVariable String orderNo) {
        RefundVO refund = refundService.getRefundByOrderNo(orderNo);
        return ApiResponse.success(refund);
    }
}
