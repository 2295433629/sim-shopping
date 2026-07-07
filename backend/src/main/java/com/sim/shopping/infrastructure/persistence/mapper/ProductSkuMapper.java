package com.sim.shopping.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sim.shopping.infrastructure.persistence.entity.ProductSkuDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductSkuMapper extends BaseMapper<ProductSkuDO> {
}
