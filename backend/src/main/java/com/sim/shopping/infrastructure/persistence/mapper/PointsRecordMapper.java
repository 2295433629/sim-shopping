package com.sim.shopping.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sim.shopping.infrastructure.persistence.entity.PointsRecordDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 积分流水数据访问层，提供积分变动的CRUD操作
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Mapper
public interface PointsRecordMapper extends BaseMapper<PointsRecordDO> {
}
