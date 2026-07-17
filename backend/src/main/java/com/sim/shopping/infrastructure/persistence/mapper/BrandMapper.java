package com.sim.shopping.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sim.shopping.infrastructure.persistence.entity.BrandDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 品牌数据访问层，提供品牌表的CRUD操作
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Mapper
public interface BrandMapper extends BaseMapper<BrandDO> {
}
