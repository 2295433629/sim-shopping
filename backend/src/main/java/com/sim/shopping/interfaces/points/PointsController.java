package com.sim.shopping.interfaces.points;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sim.shopping.application.points.PointsService;
import com.sim.shopping.infrastructure.security.SecurityUtils;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.points.ExchangeResponse;
import com.sim.shopping.interfaces.dto.points.PointsBalanceVO;
import com.sim.shopping.interfaces.dto.points.PointsRecordResponse;
import org.springframework.web.bind.annotation.*;

/**
 * 积分管理控制器，处理积分兑换商品和积分流水查询
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/user/points")
public class PointsController {

    private final PointsService pointsService;

    public PointsController(PointsService pointsService) {
        this.pointsService = pointsService;
    }

    /**
     * 查询积分余额
     * @return 返回结果
     */
    @GetMapping
    public ApiResponse<PointsBalanceVO> getPointsBalance() {
        Long userId = SecurityUtils.getCurrentUserId();
        PointsBalanceVO balance = pointsService.getPointsBalance(userId);
        return ApiResponse.success(balance);
    }

    /**
     * 查询积分流水
     * @return 返回结果
     */
    @GetMapping("/records")
    public ApiResponse<PageResponse<PointsRecordResponse>> getPointsRecords(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type) {
        Long userId = SecurityUtils.getCurrentUserId();
        IPage<PointsRecordResponse> result = pointsService.getPointsRecords(userId, page, size, type);
        PageResponse<PointsRecordResponse> pageResponse = PageResponse.of(
                result.getRecords(),
                result.getTotal(),
                (int) result.getCurrent(),
                (int) result.getSize()
        );
        return ApiResponse.success(pageResponse);
    }

    /**
     * exchange Product
     * @return 返回结果
     */
    @PostMapping("/products/{productId}/exchange")
    public ApiResponse<ExchangeResponse> exchangeProduct(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "1") Integer quantity) {
        Long userId = SecurityUtils.getCurrentUserId();
        ExchangeResponse response = pointsService.exchangeProduct(userId, productId, quantity);
        return ApiResponse.success(response);
    }
}
