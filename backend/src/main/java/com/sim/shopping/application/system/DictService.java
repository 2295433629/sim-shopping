package com.sim.shopping.application.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.infrastructure.persistence.entity.SysDictItemDO;
import com.sim.shopping.infrastructure.persistence.entity.SysDictTypeDO;
import com.sim.shopping.infrastructure.persistence.mapper.SysDictItemMapper;
import com.sim.shopping.infrastructure.persistence.mapper.SysDictTypeMapper;
import com.sim.shopping.interfaces.dto.system.DictItemCreateRequest;
import com.sim.shopping.interfaces.dto.system.DictItemRequest;
import com.sim.shopping.interfaces.dto.system.DictItemResponse;
import com.sim.shopping.interfaces.dto.system.DictTypeRequest;
import com.sim.shopping.interfaces.dto.system.DictTypeResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典服务，处理数据字典类型和字典项的管理
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Service
public class DictService {

    private final SysDictTypeMapper sysDictTypeMapper;
    private final SysDictItemMapper sysDictItemMapper;

    public DictService(SysDictTypeMapper sysDictTypeMapper,
                       SysDictItemMapper sysDictItemMapper) {
        this.sysDictTypeMapper = sysDictTypeMapper;
        this.sysDictItemMapper = sysDictItemMapper;
    }

    /**
     * 查询字典类型列表
     * @return 返回结果
     */
    public List<DictTypeResponse> getDictTypes() {
        LambdaQueryWrapper<SysDictTypeDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(SysDictTypeDO::getCreatedAt);
        List<SysDictTypeDO> list = sysDictTypeMapper.selectList(wrapper);
        return list.stream().map(this::toTypeResponse).collect(Collectors.toList());
    }

    /**
     * 创建Dict Type
     * @param req req
     * @return 返回结果
     */
    @Transactional
    public DictTypeResponse createDictType(DictTypeRequest req) {
        SysDictTypeDO dictType = new SysDictTypeDO();
        dictType.setDictName(req.getDictName());
        dictType.setDictCode(req.getDictCode());
        dictType.setStatus(req.getStatus() != null ? req.getStatus() : "ACTIVE");
        sysDictTypeMapper.insert(dictType);
        return toTypeResponse(dictType);
    }

    /**
     * 查询字典项列表
     * @param dictTypeId dictTypeId
     * @return 返回结果
     */
    public List<DictItemResponse> getDictItems(Long dictTypeId) {
        // Verify dict type exists
        SysDictTypeDO dictType = sysDictTypeMapper.selectById(dictTypeId);
        if (dictType == null) {
            throw new BusinessException(404, "字典类型不存在");
        }
        LambdaQueryWrapper<SysDictItemDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDictItemDO::getDictTypeId, dictTypeId)
               .orderByAsc(SysDictItemDO::getSortOrder);
        List<SysDictItemDO> list = sysDictItemMapper.selectList(wrapper);
        return list.stream().map(this::toItemResponse).collect(Collectors.toList());
    }

    /**
     * 创建Dict Item
     * @param dictTypeId dictTypeId
     * @param req req
     * @return 返回结果
     */
    @Transactional
    public DictItemResponse createDictItem(Long dictTypeId, DictItemCreateRequest req) {
        // Verify dict type exists
        SysDictTypeDO dictType = sysDictTypeMapper.selectById(dictTypeId);
        if (dictType == null) {
            throw new BusinessException(404, "字典类型不存在");
        }
        SysDictItemDO dictItem = new SysDictItemDO();
        dictItem.setDictTypeId(dictTypeId);
        dictItem.setLabel(req.getItemText());
        dictItem.setValue(req.getItemValue());
        dictItem.setSortOrder(req.getSortOrder() != null ? req.getSortOrder() : 0);
        dictItem.setStatus(req.getStatus() != null ? req.getStatus() : "ACTIVE");
        sysDictItemMapper.insert(dictItem);
        return toItemResponse(dictItem);
    }

    /**
     * 更新Dict Item
     * @param dictTypeId dictTypeId
     * @param itemId itemId
     * @param req req
     * @return 返回结果
     */
    @Transactional(rollbackFor = Exception.class)
    public DictItemResponse updateDictItem(Long dictTypeId, Long itemId, DictItemCreateRequest req) {
        SysDictItemDO dictItem = sysDictItemMapper.selectById(itemId);
        if (dictItem == null || !dictItem.getDictTypeId().equals(dictTypeId)) {
            throw new BusinessException(404, "字典项不存在");
        }
        dictItem.setLabel(req.getItemText());
        dictItem.setValue(req.getItemValue());
        dictItem.setSortOrder(req.getSortOrder() != null ? req.getSortOrder() : 0);
        dictItem.setStatus(req.getStatus() != null ? req.getStatus() : dictItem.getStatus());
        sysDictItemMapper.updateById(dictItem);
        return toItemResponse(dictItem);
    }

    /**
     * 删除Dict Item
     * @param dictTypeId dictTypeId
     * @param itemId itemId
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteDictItem(Long dictTypeId, Long itemId) {
        SysDictItemDO dictItem = sysDictItemMapper.selectById(itemId);
        if (dictItem == null || !dictItem.getDictTypeId().equals(dictTypeId)) {
            throw new BusinessException(404, "字典项不存在");
        }
        sysDictItemMapper.deleteById(itemId);
    }

    private DictTypeResponse toTypeResponse(SysDictTypeDO dictType) {
        DictTypeResponse resp = new DictTypeResponse();
        resp.setId(dictType.getId());
        resp.setDictName(dictType.getDictName());
        resp.setDictCode(dictType.getDictCode());
        resp.setStatus(dictType.getStatus());
        resp.setCreatedAt(dictType.getCreatedAt());
        return resp;
    }

    private DictItemResponse toItemResponse(SysDictItemDO dictItem) {
        DictItemResponse resp = new DictItemResponse();
        resp.setId(dictItem.getId());
        resp.setDictTypeId(dictItem.getDictTypeId());
        resp.setItemText(dictItem.getLabel());
        resp.setItemValue(dictItem.getValue());
        resp.setSortOrder(dictItem.getSortOrder());
        resp.setStatus(dictItem.getStatus());
        return resp;
    }
}
