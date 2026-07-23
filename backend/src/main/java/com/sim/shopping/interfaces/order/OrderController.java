package com.sim.shopping.interfaces.order;

import com.sim.shopping.application.order.OrderService;
import com.sim.shopping.infrastructure.security.SecurityUtils;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.order.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.sim.shopping.infrastructure.aop.Log;

/**
 * 用户订单管理控制器，处理用户下单、查询、取消、确认收货等操作
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/user/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 创建订单（按购物车选中商品结算）
     * @param request request
     * @return 返回结果
     */
    @PostMapping
    @Log(module = "订单", type = "新增")
    public ApiResponse<CreateOrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        List<OrderResponse> orders = orderService.createOrder(userId, request.getAddressId(), request.getRemark(), request.getCartItemIds());
        return ApiResponse.success(new CreateOrderResponse(orders));
    }

    /**
     * 查询用户订单列表（支持分页和按状态筛选）
     * @return 返回结果
     */
    @GetMapping
    public ApiResponse<PageResponse<OrderListItemVO>> getUserOrders(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String status) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(orderService.getUserOrders(userId, page, size, status));
    }

    /**
     * 查询订单详情
     * @param orderNo orderNo
     * @return 返回结果
     */
    @GetMapping("/{orderNo}")
    public ApiResponse<OrderDetailVO> getOrderDetail(@PathVariable String orderNo) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(orderService.getOrderDetailForUser(orderNo, userId));
    }

    /**
     * 取消订单
     * @param orderNo orderNo
     * @return 返回结果
     */
    @PatchMapping("/{orderNo}/cancel")
    @Log(module = "订单", type = "修改")
    public ApiResponse<Void> cancelOrder(@PathVariable String orderNo) {
        Long userId = SecurityUtils.getCurrentUserId();
        orderService.cancelOrder(userId, orderNo);
        return ApiResponse.success();
    }

    /**
     * 确认收货
     * @param orderNo orderNo
     * @return 返回结果
     */
    @PatchMapping("/{orderNo}/confirm")
    @Log(module = "订单", type = "修改")
    public ApiResponse<Void> confirmReceive(@PathVariable String orderNo) {
        Long userId = SecurityUtils.getCurrentUserId();
        orderService.confirmReceive(userId, orderNo);
        return ApiResponse.success();
    }
}
