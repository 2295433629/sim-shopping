package com.sim.shopping.interfaces.shipment;

import com.sim.shopping.application.merchant.MerchantService;
import com.sim.shopping.application.order.OrderService;
import com.sim.shopping.application.shipment.ShipmentService;
import com.sim.shopping.infrastructure.security.SecurityUtils;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.order.OrderListItemVO;
import com.sim.shopping.interfaces.dto.shipment.CreateShipmentRequest;
import com.sim.shopping.interfaces.dto.shipment.ShipmentResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 物流管理控制器，处理物流信息查询和轨迹追踪
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/merchant")
@PreAuthorize("hasRole('MERCHANT')")
public class ShipmentController {

    private final ShipmentService shipmentService;
    private final MerchantService merchantService;
    private final OrderService orderService;

    public ShipmentController(ShipmentService shipmentService,
                              MerchantService merchantService,
                              OrderService orderService) {
        this.shipmentService = shipmentService;
        this.merchantService = merchantService;
        this.orderService = orderService;
    }

    /**
     * 创建Shipment
     * @param req req
     * @return 返回结果
     */
    @PostMapping("/shipments")
    public ApiResponse<ShipmentResponse> createShipment(@RequestBody CreateShipmentRequest req) {
        Long userId = SecurityUtils.getCurrentUserId();
        Long shopId = merchantService.getShopIdByUserId(userId);
        return ApiResponse.success(shipmentService.createShipment(shopId, req));
    }

    /**
     * 获取Pending Shipment Orders
     * @return 返回结果
     */
    @GetMapping("/orders/shipping")
    public ApiResponse<PageResponse<OrderListItemVO>> getPendingShipmentOrders(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long userId = SecurityUtils.getCurrentUserId();
        Long shopId = merchantService.getShopIdByUserId(userId);
        return ApiResponse.success(orderService.getPendingShipmentOrders(shopId, page, size));
    }
}
