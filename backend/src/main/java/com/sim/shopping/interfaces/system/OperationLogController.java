package com.sim.shopping.interfaces.system;

import com.sim.shopping.application.system.OperationLogService;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.system.OperationLogItem;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class OperationLogController {

    private final OperationLogService operationLogService;

    public OperationLogController(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

    @GetMapping("/logs")
    public ApiResponse<PageResponse<OperationLogItem>> getOperationLogs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String operationType,
            @RequestParam(required = false) String keyword) {
        return ApiResponse.success(operationLogService.getOperationLogs(page, size, module, operationType, keyword));
    }
}
