package com.sim.shopping.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sim.shopping.infrastructure.persistence.entity.ActivityProductDO;
import com.sim.shopping.infrastructure.persistence.entity.ActivityProductQueryResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 活动商品数据访问层，提供活动商品关联的CRUD操作
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Mapper
public interface ActivityProductMapper extends BaseMapper<ActivityProductDO> {

    @Select("SELECT ap.id, ap.product_id as productId, ap.sort_order as sortOrder, " +
            "p.name as productName, p.main_image as productImage, p.price " +
            "FROM t_activity_product ap " +
            "LEFT JOIN t_product p ON ap.product_id = p.id " +
            "WHERE ap.activity_id = #{activityId} " +
            "AND ap.deleted = 0 " +
            "ORDER BY ap.sort_order ASC")
    List<ActivityProductQueryResult> selectProductsByActivityId(@Param("activityId") Long activityId);

    @Select("SELECT COUNT(1) FROM t_activity_product WHERE activity_id = #{activityId} AND deleted = 0")
    Long countByActivityId(@Param("activityId") Long activityId);
}
