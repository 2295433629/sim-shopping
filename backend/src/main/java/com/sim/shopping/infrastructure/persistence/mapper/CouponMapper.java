package com.sim.shopping.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sim.shopping.infrastructure.persistence.entity.CouponDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券数据访问层，提供优惠券的CRUD操作
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Mapper
public interface CouponMapper extends BaseMapper<CouponDO> {
}
