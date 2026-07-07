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

@Service
public class AdminUserService {

    private final UserMapper userMapper;

    public AdminUserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public PageResponse<AdminUserItem> getUserList(int page, int size, String keyword, Integer status) {
        Page<UserDO> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();

        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(UserDO::getUsername, keyword)
                    .or().like(UserDO::getNickname, keyword)
                    .or().like(UserDO::getPhone, keyword));
        }
        if (status != null) {
            wrapper.eq(UserDO::getStatus, status);
        }
        wrapper.orderByDesc(UserDO::getCreatedAt);

        Page<UserDO> result = userMapper.selectPage(pageObj, wrapper);

        List<AdminUserItem> list = result.getRecords().stream()
                .map(this::toAdminUserItem)
                .collect(Collectors.toList());

        return PageResponse.of(list, result.getTotal(), page, size);
    }

    public AdminUserItem getUserDetail(Long userId) {
        UserDO user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在: " + userId);
        }
        return toAdminUserItem(user);
    }

    @Transactional
    public void disableUser(Long userId) {
        UserDO user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在: " + userId);
        }
        user.setStatus("DISABLED");
        userMapper.updateById(user);
    }

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
