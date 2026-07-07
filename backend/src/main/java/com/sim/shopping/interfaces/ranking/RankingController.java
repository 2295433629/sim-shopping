package com.sim.shopping.interfaces.ranking;

import com.sim.shopping.application.ranking.RankingService;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.ranking.RankingListResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/rankings")
public class RankingController {

    private final RankingService rankingService;

    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @GetMapping("/consumption")
    public ApiResponse<RankingListResponse> getConsumptionRanking(@RequestParam String period) {
        return ApiResponse.success(rankingService.getConsumptionRanking(period));
    }

    @GetMapping("/sign-in")
    public ApiResponse<RankingListResponse> getSignInRanking(@RequestParam String period) {
        return ApiResponse.success(rankingService.getSignInRanking(period));
    }
}
