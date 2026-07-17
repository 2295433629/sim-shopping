package com.sim.shopping.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sim.shopping.infrastructure.persistence.entity.UserCouponDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户优惠券数据访问层，提供用户优惠券关联的CRUD操作
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Mapper
public interface UserCouponMapper extends BaseMapper<UserCouponDO> {
}
