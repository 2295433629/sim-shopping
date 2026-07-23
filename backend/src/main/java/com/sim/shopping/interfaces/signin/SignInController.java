package com.sim.shopping.interfaces.signin;

import com.sim.shopping.application.marketing.SignInService;
import com.sim.shopping.infrastructure.security.SecurityUtils;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.signin.SignInRecordResponse;
import com.sim.shopping.interfaces.dto.signin.SignInResult;
import org.springframework.web.bind.annotation.*;
import com.sim.shopping.infrastructure.aop.Log;

/**
 * 签到控制器，处理用户每日签到和签到记录查询
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/user/sign-in")
public class SignInController {

    private final SignInService signInService;

    public SignInController(SignInService signInService) {
        this.signInService = signInService;
    }

    /**
     * 每日签到
     * @return 返回结果
     */
    @PostMapping
    @Log(module = "营销", type = "操作")
    public ApiResponse<SignInResult> signIn() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(signInService.signIn(userId));
    }

    /**
     * 获取Today Status
     * @return 返回结果
     */
    @GetMapping("/today")
    public ApiResponse<SignInResult> getTodayStatus() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(signInService.getTodayStatus(userId));
    }

    /**
     * 查询签到记录
     * @return 返回结果
     */
    @GetMapping("/records")
    public ApiResponse<PageResponse<SignInRecordResponse>> getSignInRecords(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(signInService.getSignInRecords(userId, page, size));
    }
}
