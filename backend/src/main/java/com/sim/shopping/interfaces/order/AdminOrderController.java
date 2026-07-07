package com.sim.shopping.interfaces.order;

import com.sim.shopping.application.order.OrderService;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.order.OrderDetailVO;
import com.sim.shopping.interfaces.dto.order.OrderListItemVO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ApiResponse<PageResponse<OrderListItemVO>> getAdminOrders(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long shopId,
            @RequestParam(required = false) String keyword) {
        return ApiResponse.success(orderService.getAdminOrders(page, size, status, shopId, keyword));
    }

    @GetMapping("/{orderNo}")
    public ApiResponse<OrderDetailVO> getOrderDetail(@PathVariable String orderNo) {
        return ApiResponse.success(orderService.getOrderDetail(orderNo));
    }
}
