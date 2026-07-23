package com.sim.shopping.interfaces.banner;

import com.sim.shopping.application.marketing.BannerService;
import com.sim.shopping.infrastructure.persistence.entity.BannerDO;
import com.sim.shopping.interfaces.dto.banner.BannerResponse;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.sim.shopping.infrastructure.aop.Log;

/**
 * AdminBanner控制器，处理相关业务请求
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/admin/banners")
@PreAuthorize("hasRole('ADMIN')")
public class AdminBannerController {

    private final BannerService bannerService;

    public AdminBannerController(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    /**
     * 查询Banner列表
     * @return 返回结果
     */
    @GetMapping
    public ApiResponse<List<BannerResponse>> getBanners() {
        return ApiResponse.success(bannerService.getAllBanners());
    }

    /**
     * 创建Banner
     * @param banner banner
     * @return 返回结果
     */
    @PostMapping
    @Log(module = "内容", type = "新增")
    public ApiResponse<BannerResponse> createBanner(@RequestBody BannerDO banner) {
        return ApiResponse.success(bannerService.createBanner(banner));
    }

    /**
     * 更新Banner
     * @param bannerId bannerId
     * @param banner banner
     * @return 返回结果
     */
    @PutMapping("/{bannerId}")
    @Log(module = "内容", type = "修改")
    public ApiResponse<BannerResponse> updateBanner(@PathVariable Long bannerId, @RequestBody BannerDO banner) {
        return ApiResponse.success(bannerService.updateBanner(bannerId, banner));
    }

    /**
     * 删除Banner
     * @param bannerId bannerId
     * @return 返回结果
     */
    @DeleteMapping("/{bannerId}")
    @Log(module = "内容", type = "删除")
    public ApiResponse<Void> deleteBanner(@PathVariable Long bannerId) {
        bannerService.deleteBanner(bannerId);
        return ApiResponse.success();
    }
}
