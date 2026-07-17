package com.sim.shopping.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sim.shopping.infrastructure.persistence.entity.ShipmentDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 物流数据访问层，提供物流信息的CRUD操作
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Mapper
public interface ShipmentMapper extends BaseMapper<ShipmentDO> {
}
