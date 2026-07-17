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

/**
 * 秒杀活动公开控制器，提供秒杀商品列表和详情查询
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/public/flash-sales")
public class PublicFlashSaleController {

    private final FlashSaleService flashSaleService;

    public PublicFlashSaleController(FlashSaleService flashSaleService) {
        this.flashSaleService = flashSaleService;
    }

    /**
     * 获取Active Flash Sales
     * @return 返回结果
     */
    @GetMapping
    public ApiResponse<PageResponse<FlashSaleResponse>> getActiveFlashSales(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<FlashSaleResponse> result = flashSaleService.getActiveFlashSales(page, size);
        return ApiResponse.success(result);
    }

    /**
     * 获取Flash Sale Detail
     * @return 返回结果
     */
    @GetMapping("/{saleId}")
    public ApiResponse<FlashSaleDetailResponse> getFlashSaleDetail(
            @PathVariable Long saleId) {
        FlashSaleDetailResponse detail = flashSaleService.getFlashSaleDetail(saleId);
        return ApiResponse.success(detail);
    }
}
