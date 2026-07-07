package com.sim.shopping.interfaces.banner;

import com.sim.shopping.application.marketing.BannerService;
import com.sim.shopping.interfaces.dto.banner.BannerResponse;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/banners")
public class PublicBannerController {

    private final BannerService bannerService;

    public PublicBannerController(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    @GetMapping
    public ApiResponse<List<BannerResponse>> getActiveBanners() {
        return ApiResponse.success(bannerService.getActiveBanners());
    }
}
