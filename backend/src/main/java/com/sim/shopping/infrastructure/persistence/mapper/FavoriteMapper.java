package com.sim.shopping.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sim.shopping.infrastructure.persistence.entity.FavoriteDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收藏数据访问层，提供收藏记录的CRUD操作
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Mapper
public interface FavoriteMapper extends BaseMapper<FavoriteDO> {
}
