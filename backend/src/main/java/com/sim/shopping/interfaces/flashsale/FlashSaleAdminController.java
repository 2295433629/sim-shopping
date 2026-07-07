package com.sim.shopping.interfaces.flashsale;

import com.sim.shopping.application.flashsale.FlashSaleAdminService;
import com.sim.shopping.infrastructure.persistence.entity.FlashSaleDO;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.flashsale.FlashSaleResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/flash-sales")
@PreAuthorize("hasRole('ADMIN')")
public class FlashSaleAdminController {

    private final FlashSaleAdminService flashSaleAdminService;

    public FlashSaleAdminController(FlashSaleAdminService flashSaleAdminService) {
        this.flashSaleAdminService = flashSaleAdminService;
    }

    @GetMapping
    public ApiResponse<PageResponse<FlashSaleResponse>> getFlashSaleList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword) {
        PageResponse<FlashSaleResponse> result = flashSaleAdminService.getFlashSaleList(page, size, status, keyword);
        return ApiResponse.success(result);
    }

    @PostMapping
    public ApiResponse<Void> createFlashSale(@RequestBody FlashSaleDO sale) {
        flashSaleAdminService.createFlashSale(sale);
        return ApiResponse.success();
    }

    @PutMapping("/{saleId}")
    public ApiResponse<Void> updateFlashSale(
            @PathVariable Long saleId,
            @RequestBody FlashSaleDO sale) {
        flashSaleAdminService.updateFlashSale(saleId, sale);
        return ApiResponse.success();
    }

    @DeleteMapping("/{saleId}")
    public ApiResponse<Void> deleteFlashSale(@PathVariable Long saleId) {
        flashSaleAdminService.deleteFlashSale(saleId);
        return ApiResponse.success();
    }
}
