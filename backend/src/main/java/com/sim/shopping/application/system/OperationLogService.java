package com.sim.shopping.application.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sim.shopping.infrastructure.persistence.entity.SysOperationLogDO;
import com.sim.shopping.infrastructure.persistence.mapper.SysOperationLogMapper;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.system.OperationLogItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OperationLogService {

    private final SysOperationLogMapper sysOperationLogMapper;

    public OperationLogService(SysOperationLogMapper sysOperationLogMapper) {
        this.sysOperationLogMapper = sysOperationLogMapper;
    }

    public PageResponse<OperationLogItem> getOperationLogs(int page, int size, String module, String operationType, String keyword) {
        Page<SysOperationLogDO> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<SysOperationLogDO> wrapper = new LambdaQueryWrapper<>();

        if (module != null && !module.isEmpty()) {
            wrapper.eq(SysOperationLogDO::getModule, module);
        }
        if (operationType != null && !operationType.isEmpty()) {
            wrapper.eq(SysOperationLogDO::getOperationType, operationType);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(SysOperationLogDO::getOperatorName, keyword)
                    .or().like(SysOperationLogDO::getRequestUrl, keyword));
        }
        wrapper.orderByDesc(SysOperationLogDO::getCreatedAt);

        Page<SysOperationLogDO> result = sysOperationLogMapper.selectPage(pageObj, wrapper);

        List<OperationLogItem> list = result.getRecords().stream()
                .map(this::toOperationLogItem)
                .collect(Collectors.toList());

        return PageResponse.of(list, result.getTotal(), page, size);
    }

    private OperationLogItem toOperationLogItem(SysOperationLogDO log) {
        OperationLogItem item = new OperationLogItem();
        item.setId(log.getId());
        item.setOperatorId(log.getOperatorId());
        item.setOperatorName(log.getOperatorName());
        item.setOperatorType(log.getOperatorType());
        item.setModule(log.getModule());
        item.setOperationType(log.getOperationType());
        item.setMethod(log.getMethod());
        item.setRequestUrl(log.getRequestUrl());
        item.setIp(log.getIp());
        item.setCostTime(log.getCostTime());
        item.setCreatedAt(log.getCreatedAt());
        return item;
    }
}
