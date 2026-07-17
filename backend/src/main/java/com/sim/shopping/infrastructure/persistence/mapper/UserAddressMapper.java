package com.sim.shopping.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sim.shopping.infrastructure.persistence.entity.UserAddressDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 地址数据访问层，提供收货地址的CRUD操作
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Mapper
public interface UserAddressMapper extends BaseMapper<UserAddressDO> {
}
