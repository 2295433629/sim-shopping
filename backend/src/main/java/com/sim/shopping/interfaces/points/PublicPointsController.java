package com.sim.shopping.interfaces.points;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sim.shopping.application.points.PointsAdminService;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.points.PointsProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/points")
public class PublicPointsController {

    @Autowired
    private PointsAdminService pointsAdminService;

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
}
