package com.sim.shopping.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sim.shopping.infrastructure.persistence.entity.PaymentDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * Payment数据访问层，提供数据库CRUD操作
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Mapper
public interface PaymentMapper extends BaseMapper<PaymentDO> {
}
