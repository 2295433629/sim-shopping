package com.sim.shopping.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sim.shopping.infrastructure.persistence.entity.ScheduleJobDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务数据访问层，提供定时任务配置的CRUD操作
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Mapper
public interface ScheduleJobMapper extends BaseMapper<ScheduleJobDO> {
}
