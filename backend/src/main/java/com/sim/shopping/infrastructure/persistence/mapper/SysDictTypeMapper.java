package com.sim.shopping.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sim.shopping.infrastructure.persistence.entity.SysDictTypeDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * SysDictType数据访问层，提供数据库CRUD操作
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Mapper
public interface SysDictTypeMapper extends BaseMapper<SysDictTypeDO> {
}
