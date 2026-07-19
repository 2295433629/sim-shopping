package com.sim.shopping.interfaces.order;

import com.sim.shopping.application.merchant.MerchantService;
import com.sim.shopping.application.order.OrderService;
import com.sim.shopping.infrastructure.security.SecurityUtils;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.order.OrderDetailVO;
import com.sim.shopping.interfaces.dto.order.OrderListItemVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 商家订单控制器，处理商家对订单的发货和管理
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/merchant/orders")
@PreAuthorize("hasRole('MERCHANT')")
public class MerchantOrderController {

    private final OrderService orderService;
    private final MerchantService merchantService;

    public MerchantOrderController(OrderService orderService,
                                   MerchantService merchantService) {
        this.orderService = orderService;
        this.merchantService = merchantService;
    }

    /**
     * 查询商家订单列表
     * @return 返回结果
     */
    @GetMapping
    public ApiResponse<PageResponse<OrderListItemVO>> getMerchantOrders(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword) {
        Long userId = SecurityUtils.getCurrentUserId();
        Long shopId = merchantService.getShopIdByUserId(userId);
        return ApiResponse.success(orderService.getMerchantOrders(shopId, page, size, status, keyword));
    }

    /**
     * 查询订单详情
     * @param orderNo orderNo
     * @return 返回结果
     */
    @GetMapping("/{orderNo}")
    public ApiResponse<OrderDetailVO> getOrderDetail(@PathVariable String orderNo) {
        Long userId = SecurityUtils.getCurrentUserId();
        Long shopId = merchantService.getShopIdByUserId(userId);
        return ApiResponse.success(orderService.getOrderDetailForShop(orderNo, shopId));
    }
}
