package com.sim.shopping.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sim.shopping.infrastructure.persistence.entity.SysCacheDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 缓存数据访问层，提供缓存表的CRUD操作
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Mapper
public interface SysCacheMapper extends BaseMapper<SysCacheDO> {
}
