package com.sim.shopping.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sim.shopping.infrastructure.persistence.entity.ActivityDO;
import com.sim.shopping.interfaces.dto.activity.ActivityResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

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
    Page<ActivityResponse> selectActiveActivityPage(Page<ActivityResponse> page,
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
    Page<ActivityResponse> selectActivityPage(Page<ActivityResponse> page,
                                               @Param("status") String status,
                                               @Param("keyword") String keyword);
}
