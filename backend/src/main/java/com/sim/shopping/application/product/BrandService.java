package com.sim.shopping.application.product;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.infrastructure.persistence.entity.BrandDO;
import com.sim.shopping.infrastructure.persistence.mapper.BrandMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BrandService {

    private final BrandMapper brandMapper;

    public BrandService(BrandMapper brandMapper) {
        this.brandMapper = brandMapper;
    }

    public List<BrandDO> getBrands() {
        LambdaQueryWrapper<BrandDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(BrandDO::getCreatedAt);
        return brandMapper.selectList(wrapper);
    }

    @Transactional
    public BrandDO createBrand(BrandDO brand) {
        if (brand.getStatus() == null) {
            brand.setStatus("ENABLED");
        }
        brandMapper.insert(brand);
        return brand;
    }

    @Transactional
    public BrandDO updateBrand(Long id, BrandDO brand) {
        BrandDO existing = brandMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(404, "品牌不存在");
        }
        if (brand.getBrandName() != null) {
            existing.setBrandName(brand.getBrandName());
        }
        if (brand.getBrandLogo() != null) {
            existing.setBrandLogo(brand.getBrandLogo());
        }
        if (brand.getBrandDescription() != null) {
            existing.setBrandDescription(brand.getBrandDescription());
        }
        if (brand.getStatus() != null) {
            existing.setStatus(brand.getStatus());
        }
        brandMapper.updateById(existing);
        return existing;
    }

    @Transactional
    public void deleteBrand(Long id) {
        BrandDO existing = brandMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(404, "品牌不存在");
        }
        brandMapper.deleteById(id);
    }
}
