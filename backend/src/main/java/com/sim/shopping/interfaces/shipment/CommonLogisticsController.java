package com.sim.shopping.interfaces.shipment;

import com.sim.shopping.application.shipment.ShipmentService;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.shipment.LogisticsResponse;
import com.sim.shopping.interfaces.dto.shipment.ShipmentResponse;
import org.springframework.web.bind.annotation.*;

/**
 * CommonLogistics控制器，处理相关业务请求
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/common")
public class CommonLogisticsController {

    private final ShipmentService shipmentService;

    public CommonLogisticsController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    /**
     * 获取Logistics
     * @param orderNo orderNo
     * @return 返回结果
     */
    @GetMapping("/logistics/{orderNo}")
    public ApiResponse<LogisticsResponse> getLogistics(@PathVariable String orderNo) {
        LogisticsResponse response = shipmentService.getLogisticsByOrderNo(orderNo);
        if (response == null) {
            return ApiResponse.error(404, "物流信息不存在");
        }
        return ApiResponse.success(response);
    }

    /**
     * 查询物流信息
     * @param orderNo orderNo
     * @return 返回结果
     */
    @GetMapping("/shipments/{orderNo}")
    public ApiResponse<ShipmentResponse> getShipment(@PathVariable String orderNo) {
        ShipmentResponse response = shipmentService.getShipmentByOrderNo(orderNo);
        if (response == null) {
            return ApiResponse.error(404, "发货信息不存在");
        }
        return ApiResponse.success(response);
    }
}
