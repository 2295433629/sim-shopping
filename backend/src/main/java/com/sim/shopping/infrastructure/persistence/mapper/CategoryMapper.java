package com.sim.shopping.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sim.shopping.infrastructure.persistence.entity.CategoryDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 分类数据访问层，提供分类表的CRUD操作
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Mapper
public interface CategoryMapper extends BaseMapper<CategoryDO> {
}
