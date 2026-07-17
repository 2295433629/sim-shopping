package com.sim.shopping.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sim.shopping.infrastructure.persistence.entity.CartItemDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 购物车项数据访问层，提供购物车商品项的CRUD操作
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Mapper
public interface CartItemMapper extends BaseMapper<CartItemDO> {
}
