package com.sim.shopping.interfaces.shipment;

import com.sim.shopping.application.shipment.ShipmentService;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.shipment.LogisticsResponse;
import com.sim.shopping.interfaces.dto.shipment.ShipmentResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/common")
public class CommonLogisticsController {

    private final ShipmentService shipmentService;

    public CommonLogisticsController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping("/logistics/{orderNo}")
    public ApiResponse<LogisticsResponse> getLogistics(@PathVariable String orderNo) {
        LogisticsResponse response = shipmentService.getLogisticsByOrderNo(orderNo);
        if (response == null) {
            return ApiResponse.error(404, "物流信息不存在");
        }
        return ApiResponse.success(response);
    }

    @GetMapping("/shipments/{orderNo}")
    public ApiResponse<ShipmentResponse> getShipment(@PathVariable String orderNo) {
        ShipmentResponse response = shipmentService.getShipmentByOrderNo(orderNo);
        if (response == null) {
            return ApiResponse.error(404, "发货信息不存在");
        }
        return ApiResponse.success(response);
    }
}
