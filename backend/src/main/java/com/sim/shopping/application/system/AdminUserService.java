package com.sim.shopping.application.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.infrastructure.persistence.entity.UserDO;
import com.sim.shopping.infrastructure.persistence.mapper.UserMapper;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.system.AdminUserItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * AdminUser服务，处理相关业务逻辑
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Service
public class AdminUserService {

    private final UserMapper userMapper;

    public AdminUserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 获取User List
     * @param page page
     * @param size size
     * @param keyword keyword
     * @param status status（前端传1=正常/0=禁用，需转为数据库的ACTIVE/DISABLED）
     * @return 返回结果
     */
    public PageResponse<AdminUserItem> getUserList(int page, int size, String keyword, Integer status) {
        Page<UserDO> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();

        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(UserDO::getUsername, keyword)
                    .or().like(UserDO::getNickname, keyword)
                    .or().like(UserDO::getPhone, keyword));
        }
        if (status != null) {
            // 前端传1=正常，0=禁用；数据库存ACTIVE/DISABLED
            String dbStatus = status == 1 ? "ACTIVE" : "DISABLED";
            wrapper.eq(UserDO::getStatus, dbStatus);
        }
        wrapper.orderByDesc(UserDO::getCreatedAt);

        Page<UserDO> result = userMapper.selectPage(pageObj, wrapper);

        List<AdminUserItem> list = result.getRecords().stream()
                .map(this::toAdminUserItem)
                .collect(Collectors.toList());

        return PageResponse.of(list, result.getTotal(), page, size);
    }

    /**
     * 获取User Detail
     * @param userId userId
     * @return 返回结果
     */
    public AdminUserItem getUserDetail(Long userId) {
        UserDO user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在: " + userId);
        }
        return toAdminUserItem(user);
    }

    /**
     * 禁用User
     * @param userId userId
     */
    @Transactional
    public void disableUser(Long userId) {
        UserDO user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在: " + userId);
        }
        user.setStatus("DISABLED");
        userMapper.updateById(user);
    }

    /**
     * 启用User
     * @param userId userId
     */
    @Transactional
    public void enableUser(Long userId) {
        UserDO user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在: " + userId);
        }
        user.setStatus("ACTIVE");
        userMapper.updateById(user);
    }

    private AdminUserItem toAdminUserItem(UserDO user) {
        AdminUserItem item = new AdminUserItem();
        item.setUserId(user.getId());
        item.setUsername(user.getUsername());
        item.setNickname(user.getNickname());
        item.setPhone(user.getPhone());
        item.setEmail(user.getEmail());
        item.setRole(user.getRole());
        item.setStatus(user.getStatus());
        item.setPoints(user.getPoints());
        item.setCreatedAt(user.getCreatedAt());
        return item;
    }
}
