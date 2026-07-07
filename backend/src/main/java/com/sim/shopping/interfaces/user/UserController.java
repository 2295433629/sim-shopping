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

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/profile")
    public ApiResponse<Void> updateProfile(@Valid @RequestBody UpdateProfileRequest req) {
        Long userId = SecurityUtils.getCurrentUserId();
        userService.updateProfile(userId, req);
        return ApiResponse.success();
    }

    @PutMapping("/password")
    public ApiResponse<Void> changePassword(@Valid @RequestBody ChangePasswordRequest req) {
        Long userId = SecurityUtils.getCurrentUserId();
        userService.changePassword(userId, req);
        return ApiResponse.success();
    }

    @GetMapping("/addresses")
    public ApiResponse<List<AddressResponse>> getAddressList() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(userService.getAddressList(userId));
    }

    @PostMapping("/addresses")
    public ApiResponse<AddressResponse> createAddress(@Valid @RequestBody AddressRequest req) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(userService.createAddress(userId, req));
    }

    @PutMapping("/addresses/{addressId}")
    public ApiResponse<AddressResponse> updateAddress(@PathVariable Long addressId,
                                                       @Valid @RequestBody AddressRequest req) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(userService.updateAddress(userId, addressId, req));
    }

    @DeleteMapping("/addresses/{addressId}")
    public ApiResponse<Void> deleteAddress(@PathVariable Long addressId) {
        Long userId = SecurityUtils.getCurrentUserId();
        userService.deleteAddress(userId, addressId);
        return ApiResponse.success();
    }

    @PutMapping("/addresses/{addressId}/default")
    public ApiResponse<Void> setDefaultAddress(@PathVariable Long addressId) {
        Long userId = SecurityUtils.getCurrentUserId();
        userService.setDefaultAddress(userId, addressId);
        return ApiResponse.success();
    }

    @GetMapping("/search-history")
    public ApiResponse<List<SearchHistoryResponse>> getSearchHistory() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(userService.getSearchHistory(userId));
    }

    @DeleteMapping("/search-history")
    public ApiResponse<Void> clearSearchHistory() {
        Long userId = SecurityUtils.getCurrentUserId();
        userService.clearSearchHistory(userId);
        return ApiResponse.success();
    }

    @GetMapping("/history")
    public ApiResponse<List<BrowseHistoryResponse>> getBrowseHistory() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(userService.getBrowseHistory(userId));
    }

    @DeleteMapping("/history")
    public ApiResponse<Void> clearBrowseHistory() {
        Long userId = SecurityUtils.getCurrentUserId();
        userService.clearBrowseHistory(userId);
        return ApiResponse.success();
    }
}
