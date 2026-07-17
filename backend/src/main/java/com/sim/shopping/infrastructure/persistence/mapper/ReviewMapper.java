package com.sim.shopping.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sim.shopping.infrastructure.persistence.entity.ReviewDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 评价数据访问层，提供评价记录的CRUD操作
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Mapper
public interface ReviewMapper extends BaseMapper<ReviewDO> {
}
