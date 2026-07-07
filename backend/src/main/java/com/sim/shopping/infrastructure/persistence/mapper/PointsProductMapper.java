package com.sim.shopping.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sim.shopping.infrastructure.persistence.entity.PointsProductDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PointsProductMapper extends BaseMapper<PointsProductDO> {
}
