package com.sim.shopping.interfaces.banner;

import com.sim.shopping.application.marketing.BannerService;
import com.sim.shopping.interfaces.dto.banner.BannerResponse;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PublicBanner控制器，处理相关业务请求
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/public/banners")
public class PublicBannerController {

    private final BannerService bannerService;

    public PublicBannerController(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    /**
     * 获取Active Banners
     * @return 返回结果
     */
    @GetMapping
    public ApiResponse<List<BannerResponse>> getActiveBanners() {
        return ApiResponse.success(bannerService.getActiveBanners());
    }
}
