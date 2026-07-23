package com.sim.shopping.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sim.shopping.infrastructure.persistence.entity.ScheduleLogDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务执行日志数据访问层，提供执行日志的CRUD操作
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Mapper
public interface ScheduleLogMapper extends BaseMapper<ScheduleLogDO> {
}
