package com.sim.shopping.interfaces.ranking;

import com.sim.shopping.application.ranking.RankingService;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.ranking.RankingListResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 排行榜控制器，提供消费榜、签到榜等排行数据
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/public/rankings")
public class RankingController {

    private final RankingService rankingService;

    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    /**
     * 查询消费排行榜
     * @param period period
     * @return 返回结果
     */
    @GetMapping("/consumption")
    public ApiResponse<RankingListResponse> getConsumptionRanking(@RequestParam String period) {
        return ApiResponse.success(rankingService.getConsumptionRanking(period));
    }

    /**
     * 查询签到排行榜
     * @param period period
     * @return 返回结果
     */
    @GetMapping("/sign-in")
    public ApiResponse<RankingListResponse> getSignInRanking(@RequestParam String period) {
        return ApiResponse.success(rankingService.getSignInRanking(period));
    }
}
