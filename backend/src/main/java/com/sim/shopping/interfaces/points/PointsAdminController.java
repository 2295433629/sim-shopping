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
import com.sim.shopping.infrastructure.aop.Log;

/**
 * PointsAdmin控制器，处理相关业务请求
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/admin/points")
@PreAuthorize("hasRole('ADMIN')")
public class PointsAdminController {

    @Autowired
    private PointsAdminService pointsAdminService;

    /**
     * 获取All Records
     * @return 返回结果
     */
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

    /**
     * 获取Product List
     * @return 返回结果
     */
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

    /**
     * 发布商品
     * @param product product
     * @return 返回结果
     */
    @PostMapping("/products")
    @Log(module = "营销", type = "新增")
    public ApiResponse<Void> createProduct(@RequestBody PointsProductDO product) {
        pointsAdminService.createProduct(product);
        return ApiResponse.success();
    }

    /**
     * 编辑商品
     * @return 返回结果
     */
    @PutMapping("/products/{productId}")
    @Log(module = "营销", type = "修改")
    public ApiResponse<Void> updateProduct(
            @PathVariable Long productId,
            @RequestBody PointsProductDO product) {
        pointsAdminService.updateProduct(productId, product);
        return ApiResponse.success();
    }

    /**
     * 删除商品
     * @param productId productId
     * @return 返回结果
     */
    @DeleteMapping("/products/{productId}")
    @Log(module = "营销", type = "删除")
    public ApiResponse<Void> deleteProduct(@PathVariable Long productId) {
        pointsAdminService.deleteProduct(productId);
        return ApiResponse.success();
    }

    /**
     * 获取Statistics
     * @return 返回结果
     */
    @GetMapping("/statistics")
    public ApiResponse<PointsStatisticsVO> getStatistics() {
        PointsStatisticsVO statistics = pointsAdminService.getStatistics();
        return ApiResponse.success(statistics);
    }
}
