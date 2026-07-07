package com.sim.shopping.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sim.shopping.infrastructure.persistence.entity.UserPointsDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserPointsMapper extends BaseMapper<UserPointsDO> {

    UserPointsDO selectByUserId(@Param("userId") Long userId);
}
