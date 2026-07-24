package com.sim.shopping.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sim.shopping.infrastructure.persistence.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 用户数据访问层，提供用户表的CRUD操作
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDO> {

    /**
     * 原子增加用户积分（参数绑定，避免SQL拼接）
     * @param userId 用户ID
     * @param points 增加的积分值
     * @return 影响行数
     */
    @Update("UPDATE t_user SET points = points + #{points} WHERE id = #{userId}")
    int addPoints(@Param("userId") Long userId, @Param("points") int points);

    /**
     * 原子扣减用户积分（参数绑定，避免SQL拼接）
     * @param userId 用户ID
     * @param points 扣减的积分值
     * @return 影响行数
     */
    @Update("UPDATE t_user SET points = points - #{points} WHERE id = #{userId}")
    int deductPoints(@Param("userId") Long userId, @Param("points") int points);

    /**
     * 原子扣减用户积分，最低为0（参数绑定，避免SQL拼接）
     * @param userId 用户ID
     * @param points 扣减的积分值
     * @return 影响行数
     */
    @Update("UPDATE t_user SET points = GREATEST(points - #{points}, 0) WHERE id = #{userId}")
    int deductPointsWithFloor(@Param("userId") Long userId, @Param("points") int points);
}
