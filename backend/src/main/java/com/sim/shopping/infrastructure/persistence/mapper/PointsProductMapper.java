package com.sim.shopping.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sim.shopping.infrastructure.persistence.entity.PointsProductDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PointsProductMapper extends BaseMapper<PointsProductDO> {

    /**
     * 原子扣减积分商品库存，防止并发超兑换。
     */
    @Update("UPDATE t_points_product SET stock = stock - #{quantity} " +
            "WHERE id = #{productId} AND stock >= #{quantity} AND deleted = 0 AND status = 'ACTIVE'")
    int deductStock(@Param("productId") Long productId, @Param("quantity") int quantity);
}
