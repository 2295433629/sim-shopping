package com.sim.shopping.interfaces.order;

import com.sim.shopping.application.order.OrderService;
import com.sim.shopping.infrastructure.security.SecurityUtils;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.order.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ApiResponse<CreateOrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        List<OrderResponse> orders = orderService.createOrder(userId, request.getAddressId(), request.getRemark(), request.getCartItemIds());
        return ApiResponse.success(new CreateOrderResponse(orders));
    }

    @GetMapping
    public ApiResponse<PageResponse<OrderListItemVO>> getUserOrders(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String status) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(orderService.getUserOrders(userId, page, size, status));
    }

    @GetMapping("/{orderNo}")
    public ApiResponse<OrderDetailVO> getOrderDetail(@PathVariable String orderNo) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(orderService.getOrderDetailForUser(orderNo, userId));
    }

    @PatchMapping("/{orderNo}/cancel")
    public ApiResponse<Void> cancelOrder(@PathVariable String orderNo) {
        Long userId = SecurityUtils.getCurrentUserId();
        orderService.cancelOrder(userId, orderNo);
        return ApiResponse.success();
    }

    @PatchMapping("/{orderNo}/confirm")
    public ApiResponse<Void> confirmReceive(@PathVariable String orderNo) {
        Long userId = SecurityUtils.getCurrentUserId();
        orderService.confirmReceive(userId, orderNo);
        return ApiResponse.success();
    }
}
