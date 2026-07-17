package com.sim.shopping.application.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.infrastructure.persistence.entity.SysMenuDO;
import com.sim.shopping.infrastructure.persistence.mapper.SysMenuMapper;
import com.sim.shopping.interfaces.dto.system.MenuRequest;
import com.sim.shopping.interfaces.dto.system.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 菜单服务，处理系统菜单树的配置和管理
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Service
public class MenuService {

    private final SysMenuMapper sysMenuMapper;

    public MenuService(SysMenuMapper sysMenuMapper) {
        this.sysMenuMapper = sysMenuMapper;
    }

    /**
     * 获取Menu Tree
     * @return 返回结果
     */
    public List<MenuResponse> getMenuTree() {
        LambdaQueryWrapper<SysMenuDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(SysMenuDO::getSortOrder);
        List<SysMenuDO> allMenus = sysMenuMapper.selectList(wrapper);
        return buildTree(allMenus);
    }

    /**
     * 创建菜单
     * @param req req
     * @return 返回结果
     */
    @Transactional
    public MenuResponse createMenu(MenuRequest req) {
        SysMenuDO menu = new SysMenuDO();
        menu.setParentId(req.getParentId() != null ? req.getParentId() : 0L);
        menu.setName(req.getMenuName());
        menu.setType(req.getMenuType());
        menu.setPath(req.getPath());
        menu.setComponent(req.getComponent());
        menu.setIcon(req.getIcon());
        menu.setSortOrder(req.getSortOrder() != null ? req.getSortOrder() : 0);
        menu.setVisible(req.getVisible() != null ? req.getVisible() : 1);
        sysMenuMapper.insert(menu);
        return toResponse(menu);
    }

    /**
     * 更新菜单
     * @param menuId menuId
     * @param req req
     * @return 返回结果
     */
    @Transactional
    public MenuResponse updateMenu(Long menuId, MenuRequest req) {
        SysMenuDO menu = sysMenuMapper.selectById(menuId);
        if (menu == null) {
            throw new BusinessException(404, "菜单不存在");
        }
        if (req.getParentId() != null) {
            menu.setParentId(req.getParentId());
        }
        menu.setName(req.getMenuName());
        menu.setType(req.getMenuType());
        menu.setPath(req.getPath());
        menu.setComponent(req.getComponent());
        menu.setIcon(req.getIcon());
        if (req.getSortOrder() != null) {
            menu.setSortOrder(req.getSortOrder());
        }
        if (req.getVisible() != null) {
            menu.setVisible(req.getVisible());
        }
        sysMenuMapper.updateById(menu);
        return toResponse(menu);
    }

    /**
     * 删除菜单
     * @param menuId menuId
     */
    @Transactional
    public void deleteMenu(Long menuId) {
        SysMenuDO menu = sysMenuMapper.selectById(menuId);
        if (menu == null) {
            throw new BusinessException(404, "菜单不存在");
        }
        // Delete children first
        LambdaQueryWrapper<SysMenuDO> childWrapper = new LambdaQueryWrapper<>();
        childWrapper.eq(SysMenuDO::getParentId, menuId);
        List<SysMenuDO> children = sysMenuMapper.selectList(childWrapper);
        for (SysMenuDO child : children) {
            deleteMenu(child.getId());
        }
        sysMenuMapper.deleteById(menuId);
    }

    private List<MenuResponse> buildTree(List<SysMenuDO> allMenus) {
        Map<Long, MenuResponse> map = allMenus.stream()
                .collect(Collectors.toMap(
                        SysMenuDO::getId,
                        this::toResponse
                ));

        List<MenuResponse> roots = new ArrayList<>();
        for (SysMenuDO menu : allMenus) {
            MenuResponse resp = map.get(menu.getId());
            Long parentId = menu.getParentId();
            if (parentId == null || parentId == 0L) {
                roots.add(resp);
            } else {
                MenuResponse parent = map.get(parentId);
                if (parent != null) {
                    parent.getChildren().add(resp);
                } else {
                    roots.add(resp);
                }
            }
        }
        return roots;
    }

    private MenuResponse toResponse(SysMenuDO menu) {
        MenuResponse resp = new MenuResponse();
        resp.setId(menu.getId());
        resp.setParentId(menu.getParentId());
        resp.setMenuName(menu.getName());
        resp.setMenuType(menu.getType());
        resp.setPath(menu.getPath());
        resp.setComponent(menu.getComponent());
        resp.setIcon(menu.getIcon());
        resp.setSortOrder(menu.getSortOrder());
        resp.setVisible(menu.getVisible());
        resp.setCreatedAt(menu.getCreatedAt());
        return resp;
    }
}
