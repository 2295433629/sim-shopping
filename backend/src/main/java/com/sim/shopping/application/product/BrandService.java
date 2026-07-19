package com.sim.shopping.application.product;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.infrastructure.persistence.entity.BrandDO;
import com.sim.shopping.infrastructure.persistence.mapper.BrandMapper;
import com.sim.shopping.interfaces.dto.product.BrandRequest;
import com.sim.shopping.interfaces.dto.product.BrandResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 品牌服务，处理品牌信息的增删改查
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Service
public class BrandService {

    private final BrandMapper brandMapper;

    public BrandService(BrandMapper brandMapper) {
        this.brandMapper = brandMapper;
    }

    /**
     * 查询品牌列表
     * @return 返回结果
     */
    public List<BrandResponse> getBrands() {
        LambdaQueryWrapper<BrandDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(BrandDO::getCreatedAt);
        return brandMapper.selectList(wrapper).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * 创建品牌
     * @param request request
     * @return 返回结果
     */
    @Transactional
    public BrandResponse createBrand(BrandRequest request) {
        BrandDO brand = toEntity(request);
        if (brand.getStatus() == null) {
            brand.setStatus("ENABLED");
        }
        brandMapper.insert(brand);
        return toResponse(brand);
    }

    /**
     * 更新品牌
     * @param id id
     * @param request request
     * @return 返回结果
     */
    @Transactional
    public BrandResponse updateBrand(Long id, BrandRequest request) {
        BrandDO existing = brandMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(404, "品牌不存在");
        }
        if (request.getBrandName() != null) {
            existing.setBrandName(request.getBrandName());
        }
        if (request.getBrandLogo() != null) {
            existing.setBrandLogo(request.getBrandLogo());
        }
        if (request.getBrandDescription() != null) {
            existing.setBrandDescription(request.getBrandDescription());
        }
        if (request.getStatus() != null) {
            existing.setStatus(request.getStatus());
        }
        brandMapper.updateById(existing);
        return toResponse(existing);
    }

    /**
     * 删除品牌
     * @param id id
     */
    @Transactional
    public void deleteBrand(Long id) {
        BrandDO existing = brandMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(404, "品牌不存在");
        }
        brandMapper.deleteById(id);
    }

    private BrandDO toEntity(BrandRequest request) {
        BrandDO brand = new BrandDO();
        brand.setBrandName(request.getBrandName());
        brand.setBrandLogo(request.getBrandLogo());
        brand.setBrandDescription(request.getBrandDescription());
        brand.setStatus(request.getStatus());
        return brand;
    }

    private BrandResponse toResponse(BrandDO brand) {
        BrandResponse resp = new BrandResponse();
        resp.setId(brand.getId());
        resp.setBrandName(brand.getBrandName());
        resp.setBrandLogo(brand.getBrandLogo());
        resp.setBrandDescription(brand.getBrandDescription());
        resp.setStatus(brand.getStatus());
        resp.setCreatedAt(brand.getCreatedAt());
        resp.setUpdatedAt(brand.getUpdatedAt());
        return resp;
    }
}
