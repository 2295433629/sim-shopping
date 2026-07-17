package com.sim.shopping.interfaces.flashsale;

import com.sim.shopping.application.flashsale.FlashSaleService;
import com.sim.shopping.infrastructure.security.SecurityUtils;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.flashsale.FlashSaleOrderRequest;
import com.sim.shopping.interfaces.dto.flashsale.FlashSaleOrderResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 秒杀活动管理控制器，处理秒杀活动的创建和商品管理
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/user/flash-sales")
public class FlashSaleController {

    private final FlashSaleService flashSaleService;

    public FlashSaleController(FlashSaleService flashSaleService) {
        this.flashSaleService = flashSaleService;
    }

    /**
     * 创建Flash Sale Order
     * @return 返回结果
     */
    @PostMapping("/{saleId}/order")
    public ApiResponse<FlashSaleOrderResponse> createFlashSaleOrder(
            @PathVariable Long saleId,
            @RequestBody FlashSaleOrderRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        FlashSaleOrderResponse response = flashSaleService.createFlashSaleOrder(
                userId, saleId, request.getAddressId(), request.getQuantity());
        return ApiResponse.success(response);
    }
}
