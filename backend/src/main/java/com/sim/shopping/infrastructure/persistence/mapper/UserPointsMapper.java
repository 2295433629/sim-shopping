package com.sim.shopping.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sim.shopping.infrastructure.persistence.entity.UserPointsDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * UserPoints数据访问层，提供数据库CRUD操作
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Mapper
public interface UserPointsMapper extends BaseMapper<UserPointsDO> {

    UserPointsDO selectByUserId(@Param("userId") Long userId);

    /**
     * 原子扣减用户可用积分，防止并发超扣。
     */
    @Update("UPDATE t_user_points SET available_points = available_points - #{points}, total_spent = total_spent + #{points} " +
            "WHERE user_id = #{userId} AND available_points >= #{points}")
    int deductPoints(@Param("userId") Long userId, @Param("points") int points);

    /**
     * 原子增加用户可用积分。
     */
    @Update("UPDATE t_user_points SET available_points = available_points + #{points}, total_points = total_points + #{points} " +
            "WHERE user_id = #{userId}")
    int addPoints(@Param("userId") Long userId, @Param("points") int points);
}
