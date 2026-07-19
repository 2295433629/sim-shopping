package com.sim.shopping.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sim.shopping.infrastructure.persistence.entity.ActivityDO;
import com.sim.shopping.infrastructure.persistence.entity.ActivityQueryResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

/**
 * 专题活动数据访问层，提供专题活动的CRUD操作
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Mapper
public interface ActivityMapper extends BaseMapper<ActivityDO> {

    @Select("SELECT a.id, a.activity_name as activityName, a.banner_image as bannerImage, " +
            "a.description, a.start_time as startTime, a.end_time as endTime, " +
            "a.sort_order as sortOrder, a.status, a.created_at as createdAt, " +
            "a.updated_at as updatedAt, COUNT(ap.id) as productCount " +
            "FROM t_activity a " +
            "LEFT JOIN t_activity_product ap ON a.id = ap.activity_id AND ap.deleted = 0 " +
            "WHERE a.status = #{status} " +
            "AND a.start_time <= #{now} " +
            "AND a.end_time >= #{now} " +
            "AND a.deleted = 0 " +
            "GROUP BY a.id " +
            "ORDER BY a.sort_order ASC, a.start_time DESC")
    Page<ActivityQueryResult> selectActiveActivityPage(Page<ActivityQueryResult> page,
                                                      @Param("status") String status,
                                                      @Param("now") LocalDateTime now);

    @Select("<script>" +
            "SELECT a.id, a.activity_name as activityName, a.banner_image as bannerImage, " +
            "a.description, a.start_time as startTime, a.end_time as endTime, " +
            "a.sort_order as sortOrder, a.status, a.created_at as createdAt, " +
            "a.updated_at as updatedAt, COUNT(ap.id) as productCount " +
            "FROM t_activity a " +
            "LEFT JOIN t_activity_product ap ON a.id = ap.activity_id AND ap.deleted = 0 " +
            "WHERE a.deleted = 0 " +
            "<if test='status != null and status != \"\"'> AND a.status = #{status} </if>" +
            "<if test='keyword != null and keyword != \"\"'> AND a.activity_name LIKE CONCAT('%', #{keyword}, '%') </if>" +
            "GROUP BY a.id " +
            "ORDER BY a.sort_order ASC, a.start_time DESC" +
            "</script>")
    Page<ActivityQueryResult> selectActivityPage(Page<ActivityQueryResult> page,
                                               @Param("status") String status,
                                               @Param("keyword") String keyword);
}
