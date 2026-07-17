package com.sim.shopping.interfaces.system;

import com.sim.shopping.application.system.DictService;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.system.DictItemCreateRequest;
import com.sim.shopping.interfaces.dto.system.DictItemRequest;
import com.sim.shopping.interfaces.dto.system.DictItemResponse;
import com.sim.shopping.interfaces.dto.system.DictTypeRequest;
import com.sim.shopping.interfaces.dto.system.DictTypeResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典管理控制器，处理数据字典类型和项的管理
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/admin/dicts")
@PreAuthorize("hasRole('ADMIN')")
public class DictController {

    private final DictService dictService;

    public DictController(DictService dictService) {
        this.dictService = dictService;
    }

    /**
     * 查询字典类型列表
     * @return 返回结果
     */
    @GetMapping
    public ApiResponse<List<DictTypeResponse>> getDictTypes() {
        return ApiResponse.success(dictService.getDictTypes());
    }

    /**
     * 创建Dict Type
     * @param req req
     * @return 返回结果
     */
    @PostMapping
    public ApiResponse<DictTypeResponse> createDictType(@Valid @RequestBody DictTypeRequest req) {
        return ApiResponse.success(dictService.createDictType(req));
    }

    /**
     * 查询字典项列表
     * @param dictTypeId dictTypeId
     * @return 返回结果
     */
    @GetMapping("/{dictTypeId}/items")
    public ApiResponse<List<DictItemResponse>> getDictItems(@PathVariable Long dictTypeId) {
        return ApiResponse.success(dictService.getDictItems(dictTypeId));
    }

    /**
     * 创建Dict Item
     * @return 返回结果
     */
    @PostMapping("/{dictTypeId}/items")
    public ApiResponse<DictItemResponse> createDictItem(@PathVariable Long dictTypeId,
                                                         @Valid @RequestBody DictItemCreateRequest req) {
        return ApiResponse.success(dictService.createDictItem(dictTypeId, req));
    }

    /**
     * 更新Dict Item
     * @return 返回结果
     */
    @PutMapping("/{dictTypeId}/items/{itemId}")
    public ApiResponse<DictItemResponse> updateDictItem(@PathVariable Long dictTypeId,
                                                         @PathVariable Long itemId,
                                                         @Valid @RequestBody DictItemCreateRequest req) {
        return ApiResponse.success(dictService.updateDictItem(dictTypeId, itemId, req));
    }

    /**
     * 删除Dict Item
     * @return 返回结果
     */
    @DeleteMapping("/{dictTypeId}/items/{itemId}")
    public ApiResponse<Void> deleteDictItem(@PathVariable Long dictTypeId,
                                             @PathVariable Long itemId) {
        dictService.deleteDictItem(dictTypeId, itemId);
        return ApiResponse.success();
    }
}
