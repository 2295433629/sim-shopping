package com.sim.shopping.interfaces.refund;

import com.sim.shopping.application.refund.RefundService;
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

    public RefundController(RefundService refundService) {
        this.refundService = refundService;
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
        RefundVO refund = refundService.getRefundByOrderNo(orderNo);
        return ApiResponse.success(refund);
    }
}
