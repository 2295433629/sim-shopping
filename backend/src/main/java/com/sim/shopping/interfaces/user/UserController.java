package com.sim.shopping.interfaces.user;

import com.sim.shopping.application.user.UserService;
import com.sim.shopping.infrastructure.security.SecurityUtils;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.user.AddressRequest;
import com.sim.shopping.interfaces.dto.user.AddressResponse;
import com.sim.shopping.interfaces.dto.user.BrowseHistoryResponse;
import com.sim.shopping.interfaces.dto.user.ChangePasswordRequest;
import com.sim.shopping.interfaces.dto.user.SearchHistoryResponse;
import com.sim.shopping.interfaces.dto.user.UpdateProfileRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.sim.shopping.infrastructure.aop.Log;

/**
 * 用户管理控制器，处理用户信息修改、收货地址管理
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 更新Profile
     * @param req req
     * @return 返回结果
     */
    @PutMapping("/profile")
    @Log(module = "用户", type = "修改")
    public ApiResponse<Void> updateProfile(@Valid @RequestBody UpdateProfileRequest req) {
        Long userId = SecurityUtils.getCurrentUserId();
        userService.updateProfile(userId, req);
        return ApiResponse.success();
    }

    /**
     * change Password
     * @param req req
     * @return 返回结果
     */
    @PutMapping("/password")
    @Log(module = "用户", type = "修改")
    public ApiResponse<Void> changePassword(@Valid @RequestBody ChangePasswordRequest req) {
        Long userId = SecurityUtils.getCurrentUserId();
        userService.changePassword(userId, req);
        return ApiResponse.success();
    }

    /**
     * 获取Address List
     * @return 返回结果
     */
    @GetMapping("/addresses")
    public ApiResponse<List<AddressResponse>> getAddressList() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(userService.getAddressList(userId));
    }

    /**
     * 添加收货地址
     * @param req req
     * @return 返回结果
     */
    @PostMapping("/addresses")
    @Log(module = "用户", type = "新增")
    public ApiResponse<AddressResponse> createAddress(@Valid @RequestBody AddressRequest req) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(userService.createAddress(userId, req));
    }

    /**
     * 更新收货地址
     * @return 返回结果
     */
    @PutMapping("/addresses/{addressId}")
    @Log(module = "用户", type = "修改")
    public ApiResponse<AddressResponse> updateAddress(@PathVariable Long addressId,
                                                       @Valid @RequestBody AddressRequest req) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(userService.updateAddress(userId, addressId, req));
    }

    /**
     * 删除收货地址
     * @param addressId addressId
     * @return 返回结果
     */
    @DeleteMapping("/addresses/{addressId}")
    @Log(module = "用户", type = "删除")
    public ApiResponse<Void> deleteAddress(@PathVariable Long addressId) {
        Long userId = SecurityUtils.getCurrentUserId();
        userService.deleteAddress(userId, addressId);
        return ApiResponse.success();
    }

    /**
     * 设置默认地址
     * @param addressId addressId
     * @return 返回结果
     */
    @PutMapping("/addresses/{addressId}/default")
    @Log(module = "用户", type = "修改")
    public ApiResponse<Void> setDefaultAddress(@PathVariable Long addressId) {
        Long userId = SecurityUtils.getCurrentUserId();
        userService.setDefaultAddress(userId, addressId);
        return ApiResponse.success();
    }

    /**
     * 获取Search History
     * @return 返回结果
     */
    @GetMapping("/search-history")
    public ApiResponse<List<SearchHistoryResponse>> getSearchHistory() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(userService.getSearchHistory(userId));
    }

    /**
     * 清空Search History
     * @return 返回结果
     */
    @DeleteMapping("/search-history")
    @Log(module = "用户", type = "删除")
    public ApiResponse<Void> clearSearchHistory() {
        Long userId = SecurityUtils.getCurrentUserId();
        userService.clearSearchHistory(userId);
        return ApiResponse.success();
    }

    /**
     * 获取Browse History
     * @return 返回结果
     */
    @GetMapping("/history")
    public ApiResponse<List<BrowseHistoryResponse>> getBrowseHistory() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(userService.getBrowseHistory(userId));
    }

    /**
     * 清空Browse History
     * @return 返回结果
     */
    @DeleteMapping("/history")
    @Log(module = "用户", type = "删除")
    public ApiResponse<Void> clearBrowseHistory() {
        Long userId = SecurityUtils.getCurrentUserId();
        userService.clearBrowseHistory(userId);
        return ApiResponse.success();
    }
}
