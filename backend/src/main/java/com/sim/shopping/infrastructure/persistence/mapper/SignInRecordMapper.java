package com.sim.shopping.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sim.shopping.infrastructure.persistence.entity.SignInRecordDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 签到记录数据访问层，提供签到记录的CRUD操作
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Mapper
public interface SignInRecordMapper extends BaseMapper<SignInRecordDO> {
}
