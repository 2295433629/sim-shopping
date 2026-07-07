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

@RestController
@RequestMapping("/api/user/flash-sales")
public class FlashSaleController {

    private final FlashSaleService flashSaleService;

    public FlashSaleController(FlashSaleService flashSaleService) {
        this.flashSaleService = flashSaleService;
    }

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
