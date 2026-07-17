package com.sim.shopping.application.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sim.shopping.infrastructure.persistence.entity.SysOperationLogDO;
import com.sim.shopping.infrastructure.persistence.mapper.SysOperationLogMapper;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.system.OperationLogItem;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 操作日志服务，处理操作日志的记录和查询
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Service
public class OperationLogService {

    private final SysOperationLogMapper sysOperationLogMapper;

    public OperationLogService(SysOperationLogMapper sysOperationLogMapper) {
        this.sysOperationLogMapper = sysOperationLogMapper;
    }

    /**
     * 查询操作日志
     * @return 返回结果
     */
    public PageResponse<OperationLogItem> getOperationLogs(int page, int size, String module, String operationType,
                                                            String keyword, Long operatorId, String startDate, String endDate) {
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
        if (operatorId != null) {
            wrapper.eq(SysOperationLogDO::getOperatorId, operatorId);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (startDate != null && !startDate.isEmpty()) {
            LocalDateTime start = LocalDate.parse(startDate, formatter).atStartOfDay();
            wrapper.ge(SysOperationLogDO::getCreatedAt, start);
        }
        if (endDate != null && !endDate.isEmpty()) {
            LocalDateTime end = LocalDate.parse(endDate, formatter).atTime(23, 59, 59);
            wrapper.le(SysOperationLogDO::getCreatedAt, end);
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
