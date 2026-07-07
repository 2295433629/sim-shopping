package com.sim.shopping.interfaces.system;

import com.sim.shopping.application.system.MenuService;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.system.MenuRequest;
import com.sim.shopping.interfaces.dto.system.MenuResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/menus")
@PreAuthorize("hasRole('ADMIN')")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    public ApiResponse<List<MenuResponse>> getMenuTree() {
        return ApiResponse.success(menuService.getMenuTree());
    }

    @PostMapping
    public ApiResponse<MenuResponse> createMenu(@Valid @RequestBody MenuRequest req) {
        return ApiResponse.success(menuService.createMenu(req));
    }

    @PutMapping("/{menuId}")
    public ApiResponse<MenuResponse> updateMenu(@PathVariable Long menuId,
                                                 @Valid @RequestBody MenuRequest req) {
        return ApiResponse.success(menuService.updateMenu(menuId, req));
    }

    @DeleteMapping("/{menuId}")
    public ApiResponse<Void> deleteMenu(@PathVariable Long menuId) {
        menuService.deleteMenu(menuId);
        return ApiResponse.success();
    }
}
