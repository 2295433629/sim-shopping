package com.sim.shopping.interfaces.points;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sim.shopping.application.points.PointsAdminService;
import com.sim.shopping.infrastructure.persistence.entity.PointsProductDO;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.points.PointsProductResponse;
import com.sim.shopping.interfaces.dto.points.PointsRecordResponse;
import com.sim.shopping.interfaces.dto.points.PointsStatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/points")
@PreAuthorize("hasRole('ADMIN')")
public class PointsAdminController {

    @Autowired
    private PointsAdminService pointsAdminService;

    @GetMapping("/records")
    public ApiResponse<PageResponse<PointsRecordResponse>> getAllRecords(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String source) {
        IPage<PointsRecordResponse> result = pointsAdminService.getAllRecords(page, size, userId, type, source);
        PageResponse<PointsRecordResponse> pageResponse = PageResponse.of(
                result.getRecords(),
                result.getTotal(),
                (int) result.getCurrent(),
                (int) result.getSize()
        );
        return ApiResponse.success(pageResponse);
    }

    @GetMapping("/products")
    public ApiResponse<PageResponse<PointsProductResponse>> getProductList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword) {
        IPage<PointsProductResponse> result = pointsAdminService.getProductList(page, size, status, keyword);
        PageResponse<PointsProductResponse> pageResponse = PageResponse.of(
                result.getRecords(),
                result.getTotal(),
                (int) result.getCurrent(),
                (int) result.getSize()
        );
        return ApiResponse.success(pageResponse);
    }

    @PostMapping("/products")
    public ApiResponse<Void> createProduct(@RequestBody PointsProductDO product) {
        pointsAdminService.createProduct(product);
        return ApiResponse.success();
    }

    @PutMapping("/products/{productId}")
    public ApiResponse<Void> updateProduct(
            @PathVariable Long productId,
            @RequestBody PointsProductDO product) {
        pointsAdminService.updateProduct(productId, product);
        return ApiResponse.success();
    }

    @DeleteMapping("/products/{productId}")
    public ApiResponse<Void> deleteProduct(@PathVariable Long productId) {
        pointsAdminService.deleteProduct(productId);
        return ApiResponse.success();
    }

    @GetMapping("/statistics")
    public ApiResponse<PointsStatisticsVO> getStatistics() {
        PointsStatisticsVO statistics = pointsAdminService.getStatistics();
        return ApiResponse.success(statistics);
    }
}
