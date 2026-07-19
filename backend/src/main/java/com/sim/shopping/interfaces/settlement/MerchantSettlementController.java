package com.sim.shopping.interfaces.settlement;

import com.sim.shopping.application.merchant.MerchantService;
import com.sim.shopping.application.settlement.SettlementService;
import com.sim.shopping.infrastructure.persistence.entity.ShopDO;
import com.sim.shopping.infrastructure.security.SecurityUtils;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.settlement.SettlementRecordResponse;
import com.sim.shopping.interfaces.dto.settlement.ShopFinanceResponse;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 商家结算控制器，处理商家结算记录查询
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/merchant/settlement")
public class MerchantSettlementController {

    private final SettlementService settlementService;
    private final MerchantService merchantService;

    public MerchantSettlementController(SettlementService settlementService,
                                         MerchantService merchantService) {
        this.settlementService = settlementService;
        this.merchantService = merchantService;
    }

    /**
     * 获取财务概览（余额、总收入、今日收入等）
     */
    @GetMapping("/finance")
    public ApiResponse<ShopFinanceResponse> getFinance() {
        Long userId = SecurityUtils.getCurrentUserId();
        ShopDO shop = merchantService.getShopByUserId(userId);

        ShopFinanceResponse resp = new ShopFinanceResponse();
        resp.setBalance(shop.getBalance() != null ? shop.getBalance() : BigDecimal.ZERO);
        resp.setTotalIncome(shop.getTotalIncome() != null ? shop.getTotalIncome() : BigDecimal.ZERO);
        resp.setTotalSettled(shop.getTotalSettled() != null ? shop.getTotalSettled() : BigDecimal.ZERO);
        resp.setFrozenAmount(shop.getFrozenAmount() != null ? shop.getFrozenAmount() : BigDecimal.ZERO);
        resp.setTodayIncome(BigDecimal.ZERO); // 占位，后续可通过结算记录聚合

        return ApiResponse.success(resp);
    }

    /**
     * 获取结算记录分页
     */
    @GetMapping("/records")
    public ApiResponse<PageResponse<SettlementRecordResponse>> getSettlementRecords(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long userId = SecurityUtils.getCurrentUserId();
        ShopDO shop = merchantService.getShopByUserId(userId);

        return ApiResponse.success(settlementService.getSettlementRecords(shop.getId(), page, size));
    }
}
