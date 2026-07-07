package com.sim.shopping.interfaces.signin;

import com.sim.shopping.application.marketing.SignInService;
import com.sim.shopping.infrastructure.security.SecurityUtils;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.signin.SignInRecordResponse;
import com.sim.shopping.interfaces.dto.signin.SignInResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/sign-in")
public class SignInController {

    private final SignInService signInService;

    public SignInController(SignInService signInService) {
        this.signInService = signInService;
    }

    @PostMapping
    public ApiResponse<SignInResult> signIn() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(signInService.signIn(userId));
    }

    @GetMapping("/today")
    public ApiResponse<SignInResult> getTodayStatus() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(signInService.getTodayStatus(userId));
    }

    @GetMapping("/records")
    public ApiResponse<PageResponse<SignInRecordResponse>> getSignInRecords(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(signInService.getSignInRecords(userId, page, size));
    }
}
