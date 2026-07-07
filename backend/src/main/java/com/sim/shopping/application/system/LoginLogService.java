package com.sim.shopping.application.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sim.shopping.infrastructure.persistence.entity.SysLoginLogDO;
import com.sim.shopping.infrastructure.persistence.mapper.SysLoginLogMapper;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.system.LoginLogItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoginLogService {

    private final SysLoginLogMapper sysLoginLogMapper;

    public LoginLogService(SysLoginLogMapper sysLoginLogMapper) {
        this.sysLoginLogMapper = sysLoginLogMapper;
    }

    public void recordLoginLog(Long userId, String username, String userType, Integer status, String ip, String userAgent, String errorMsg) {
        SysLoginLogDO log = new SysLoginLogDO();
        log.setUserId(userId);
        log.setUsername(username);
        log.setUserType(userType);
        log.setStatus(status);
        log.setIp(ip);
        log.setUserAgent(userAgent);
        log.setLoginType("LOGIN");
        log.setErrorMsg(errorMsg);
        sysLoginLogMapper.insert(log);
    }

    public PageResponse<LoginLogItem> getLoginLogs(int page, int size, String username, Integer status, String userType) {
        Page<SysLoginLogDO> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<SysLoginLogDO> wrapper = new LambdaQueryWrapper<>();

        if (username != null && !username.isEmpty()) {
            wrapper.like(SysLoginLogDO::getUsername, username);
        }
        if (status != null) {
            wrapper.eq(SysLoginLogDO::getStatus, status);
        }
        if (userType != null && !userType.isEmpty()) {
            wrapper.eq(SysLoginLogDO::getUserType, userType);
        }
        wrapper.orderByDesc(SysLoginLogDO::getCreatedAt);

        Page<SysLoginLogDO> result = sysLoginLogMapper.selectPage(pageObj, wrapper);

        List<LoginLogItem> list = result.getRecords().stream()
                .map(this::toLoginLogItem)
                .collect(Collectors.toList());

        return PageResponse.of(list, result.getTotal(), page, size);
    }

    private LoginLogItem toLoginLogItem(SysLoginLogDO log) {
        LoginLogItem item = new LoginLogItem();
        item.setId(log.getId());
        item.setUserId(log.getUserId());
        item.setUsername(log.getUsername());
        item.setUserType(log.getUserType());
        item.setStatus(log.getStatus());
        item.setIp(log.getIp());
        item.setLoginType(log.getLoginType());
        item.setUserAgent(log.getUserAgent());
        item.setErrorMsg(log.getErrorMsg());
        item.setCreatedAt(log.getCreatedAt());
        return item;
    }
}
