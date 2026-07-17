package com.sim.shopping.interfaces.merchant;

import com.sim.shopping.application.merchant.MerchantService;
import com.sim.shopping.infrastructure.security.SecurityUtils;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.merchant.AuditRequest;
import com.sim.shopping.interfaces.dto.merchant.MerchantApplyRequest;
import com.sim.shopping.interfaces.dto.merchant.MerchantInfoResponse;
import com.sim.shopping.interfaces.dto.merchant.MerchantListResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Merchant控制器，处理相关业务请求
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@PreAuthorize("hasRole('MERCHANT')")
public class MerchantController {

    private final MerchantService merchantService;

    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    // ===== Merchant endpoints (requires login) =====

    /**
     * apply
     * @param req req
     * @return 返回结果
     */
    @PostMapping("/api/merchant/apply")
    public ApiResponse<MerchantInfoResponse> apply(@Valid @RequestBody MerchantApplyRequest req) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(merchantService.apply(userId, req));
    }

    /**
     * 兼容 API-001 设计文档：用户提交商家入驻申请
     * - 原实现为 /api/merchant/apply（仅要求已登录）
     * - 新增 /api/user/merchant-application 作为别名，避免前后端/文档不一致
     */
    @PostMapping("/api/user/merchant-application")
    public ApiResponse<MerchantInfoResponse> applyFromUser(@Valid @RequestBody MerchantApplyRequest req) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(merchantService.apply(userId, req));
    }

    /**
     * 获取Merchant Info
     * @return 返回结果
     */
    @GetMapping("/api/merchant/info")
    public ApiResponse<MerchantInfoResponse> getMerchantInfo() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(merchantService.getMerchantInfo(userId));
    }

    /**
     * 更新Merchant Info
     * @param req req
     * @return 返回结果
     */
    @PutMapping("/api/merchant/info")
    public ApiResponse<MerchantInfoResponse> updateMerchantInfo(@Valid @RequestBody MerchantApplyRequest req) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(merchantService.updateMerchantInfo(userId, req));
    }

    // ===== Admin endpoints (requires admin login) =====

    /**
     * 获取Merchant List
     * @return 返回结果
     */
    @GetMapping("/api/admin/merchants")
    public ApiResponse<PageResponse<MerchantListResponse>> getMerchantList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword) {
        return ApiResponse.success(merchantService.getMerchantList(page, size, status, keyword));
    }

    /**
     * 获取Pending Merchants
     * @return 返回结果
     */
    @GetMapping("/api/admin/merchants/pending")
    public ApiResponse<PageResponse<MerchantListResponse>> getPendingMerchants(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(merchantService.getPendingMerchants(page, size));
    }

    /**
     * 获取Merchant Detail
     * @param merchantId merchantId
     * @return 返回结果
     */
    @GetMapping("/api/admin/merchants/{merchantId}")
    public ApiResponse<MerchantInfoResponse> getMerchantDetail(@PathVariable Long merchantId) {
        return ApiResponse.success(merchantService.getMerchantDetail(merchantId));
    }

    /**
     * approve Merchant
     * @param merchantId merchantId
     * @return 返回结果
     */
    @PatchMapping("/api/admin/merchants/{merchantId}/approve")
    public ApiResponse<Void> approveMerchant(@PathVariable Long merchantId) {
        Long adminId = SecurityUtils.getCurrentUserId();
        merchantService.approveMerchant(merchantId, adminId);
        return ApiResponse.success();
    }

    /**
     * reject Merchant
     * @return 返回结果
     */
    @PatchMapping("/api/admin/merchants/{merchantId}/reject")
    public ApiResponse<Void> rejectMerchant(@PathVariable Long merchantId,
                                             @Valid @RequestBody AuditRequest req) {
        Long adminId = SecurityUtils.getCurrentUserId();
        merchantService.rejectMerchant(merchantId, adminId, req.getReason());
        return ApiResponse.success();
    }

    /**
     * 禁用Merchant
     * @param merchantId merchantId
     * @return 返回结果
     */
    @PatchMapping("/api/admin/merchants/{merchantId}/disable")
    public ApiResponse<Void> disableMerchant(@PathVariable Long merchantId) {
        merchantService.disableMerchant(merchantId);
        return ApiResponse.success();
    }

    /**
     * 启用Merchant
     * @param merchantId merchantId
     * @return 返回结果
     */
    @PatchMapping("/api/admin/merchants/{merchantId}/enable")
    public ApiResponse<Void> enableMerchant(@PathVariable Long merchantId) {
        merchantService.enableMerchant(merchantId);
        return ApiResponse.success();
    }
}
