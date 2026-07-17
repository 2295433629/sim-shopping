package com.sim.shopping.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sim.shopping.infrastructure.persistence.entity.OrderItemDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项数据访问层，提供订单商品明细的CRUD操作
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItemDO> {
}
