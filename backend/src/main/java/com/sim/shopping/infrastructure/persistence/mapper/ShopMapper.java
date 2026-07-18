package com.sim.shopping.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sim.shopping.infrastructure.persistence.entity.ShopDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

/**
 * 店铺数据访问层，提供店铺信息的CRUD操作
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Mapper
public interface ShopMapper extends BaseMapper<ShopDO> {

    @Update("UPDATE t_shop SET balance = balance + #{amount}, total_income = total_income + #{amount}, total_settled = total_settled + #{amount} WHERE id = #{shopId}")
    int addIncome(@Param("shopId") Long shopId, @Param("amount") BigDecimal amount);

    @Update("UPDATE t_shop SET balance = balance - #{amount}, total_income = total_income - #{amount}, total_settled = total_settled - #{amount} WHERE id = #{shopId}")
    int deductIncome(@Param("shopId") Long shopId, @Param("amount") BigDecimal amount);
}
