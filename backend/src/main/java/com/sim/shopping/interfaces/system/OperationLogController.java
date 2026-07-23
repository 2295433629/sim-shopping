package com.sim.shopping.interfaces.system;

import com.sim.shopping.application.system.DictService;
import com.sim.shopping.application.system.OperationLogService;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.system.DictItemResponse;
import com.sim.shopping.interfaces.dto.system.OperationLogItem;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 操作日志控制器，处理操作日志查询
 * 模块和类型字典从系统字典管理读取（dict_code: OPERATION_LOG_MODULE / OPERATION_LOG_TYPE）
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class OperationLogController {

    private final OperationLogService operationLogService;
    private final DictService dictService;

    public OperationLogController(OperationLogService operationLogService, DictService dictService) {
        this.operationLogService = operationLogService;
        this.dictService = dictService;
    }

    /**
     * 查询操作日志
     * @return 返回结果
     */
    @GetMapping("/logs")
    public ApiResponse<PageResponse<OperationLogItem>> getOperationLogs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String operationType,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long operatorId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return ApiResponse.success(operationLogService.getOperationLogs(page, size, module, operationType, keyword, operatorId, startDate, endDate));
    }

    /**
     * 获取操作日志模块字典（从系统字典 OPERATION_LOG_MODULE 读取）
     * @return 模块列表
     */
    @GetMapping("/logs/modules")
    public ApiResponse<List<String>> getLogModules() {
        List<DictItemResponse> items = dictService.getDictItemsByCode("OPERATION_LOG_MODULE");
        List<String> modules = items.stream().map(DictItemResponse::getItemValue).collect(Collectors.toList());
        return ApiResponse.success(modules);
    }

    /**
     * 获取操作日志类型字典（从系统字典 OPERATION_LOG_TYPE 读取）
     * @return 类型列表
     */
    @GetMapping("/logs/operation-types")
    public ApiResponse<List<String>> getLogOperationTypes() {
        List<DictItemResponse> items = dictService.getDictItemsByCode("OPERATION_LOG_TYPE");
        List<String> types = items.stream().map(DictItemResponse::getItemValue).collect(Collectors.toList());
        return ApiResponse.success(types);
    }
}
