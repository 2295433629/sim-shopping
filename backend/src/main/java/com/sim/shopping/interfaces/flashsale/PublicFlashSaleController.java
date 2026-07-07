package com.sim.shopping.interfaces.flashsale;

import com.sim.shopping.application.flashsale.FlashSaleService;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.flashsale.FlashSaleDetailResponse;
import com.sim.shopping.interfaces.dto.flashsale.FlashSaleResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/flash-sales")
public class PublicFlashSaleController {

    private final FlashSaleService flashSaleService;

    public PublicFlashSaleController(FlashSaleService flashSaleService) {
        this.flashSaleService = flashSaleService;
    }

    @GetMapping
    public ApiResponse<PageResponse<FlashSaleResponse>> getActiveFlashSales(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<FlashSaleResponse> result = flashSaleService.getActiveFlashSales(page, size);
        return ApiResponse.success(result);
    }

    @GetMapping("/{saleId}")
    public ApiResponse<FlashSaleDetailResponse> getFlashSaleDetail(
            @PathVariable Long saleId) {
        FlashSaleDetailResponse detail = flashSaleService.getFlashSaleDetail(saleId);
        return ApiResponse.success(detail);
    }
}
