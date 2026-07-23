package com.sim.shopping.interfaces.system;

import com.sim.shopping.application.system.MenuService;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.system.MenuRequest;
import com.sim.shopping.interfaces.dto.system.MenuResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.sim.shopping.infrastructure.aop.Log;

/**
 * 菜单管理控制器，处理系统菜单树的配置
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/admin/menus")
@PreAuthorize("hasRole('ADMIN')")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * 获取Menu Tree
     * @return 返回结果
     */
    @GetMapping
    public ApiResponse<List<MenuResponse>> getMenuTree() {
        return ApiResponse.success(menuService.getMenuTree());
    }

    /**
     * 创建菜单
     * @param req req
     * @return 返回结果
     */
    @PostMapping
    @Log(module = "系统", type = "新增")
    public ApiResponse<MenuResponse> createMenu(@Valid @RequestBody MenuRequest req) {
        return ApiResponse.success(menuService.createMenu(req));
    }

    /**
     * 更新菜单
     * @return 返回结果
     */
    @PutMapping("/{menuId}")
    @Log(module = "系统", type = "修改")
    public ApiResponse<MenuResponse> updateMenu(@PathVariable Long menuId,
                                                 @Valid @RequestBody MenuRequest req) {
        return ApiResponse.success(menuService.updateMenu(menuId, req));
    }

    /**
     * 删除菜单
     * @param menuId menuId
     * @return 返回结果
     */
    @DeleteMapping("/{menuId}")
    @Log(module = "系统", type = "删除")
    public ApiResponse<Void> deleteMenu(@PathVariable Long menuId) {
        menuService.deleteMenu(menuId);
        return ApiResponse.success();
    }
}
