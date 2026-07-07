package com.sim.shopping.interfaces.system;

import com.sim.shopping.application.system.DictService;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.system.DictItemRequest;
import com.sim.shopping.interfaces.dto.system.DictItemResponse;
import com.sim.shopping.interfaces.dto.system.DictTypeRequest;
import com.sim.shopping.interfaces.dto.system.DictTypeResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/dicts")
@PreAuthorize("hasRole('ADMIN')")
public class DictController {

    private final DictService dictService;

    public DictController(DictService dictService) {
        this.dictService = dictService;
    }

    @GetMapping
    public ApiResponse<List<DictTypeResponse>> getDictTypes() {
        return ApiResponse.success(dictService.getDictTypes());
    }

    @PostMapping
    public ApiResponse<DictTypeResponse> createDictType(@Valid @RequestBody DictTypeRequest req) {
        return ApiResponse.success(dictService.createDictType(req));
    }

    @GetMapping("/{dictTypeId}/items")
    public ApiResponse<List<DictItemResponse>> getDictItems(@PathVariable Long dictTypeId) {
        return ApiResponse.success(dictService.getDictItems(dictTypeId));
    }

    @PostMapping("/{dictTypeId}/items")
    public ApiResponse<DictItemResponse> createDictItem(@PathVariable Long dictTypeId,
                                                         @Valid @RequestBody DictItemRequest req) {
        return ApiResponse.success(dictService.createDictItem(dictTypeId, req));
    }
}
