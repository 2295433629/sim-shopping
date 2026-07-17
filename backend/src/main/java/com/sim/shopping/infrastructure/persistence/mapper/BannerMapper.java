package com.sim.shopping.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sim.shopping.infrastructure.persistence.entity.BannerDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * Banner数据访问层，提供轮播图的CRUD操作
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Mapper
public interface BannerMapper extends BaseMapper<BannerDO> {
}
