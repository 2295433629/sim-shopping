package com.sim.shopping.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sim.shopping.infrastructure.persistence.entity.ProductSkuDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * ProductSku数据访问层，提供数据库CRUD操作
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Mapper
public interface ProductSkuMapper extends BaseMapper<ProductSkuDO> {

    /**
     * 原子扣减SKU库存，防止并发超卖。
     * 仅在库存 >= 扣减数量时生效。
     */
    @Update("UPDATE t_product_sku SET stock = stock - #{quantity} WHERE id = #{skuId} AND stock >= #{quantity}")
    int deductStock(@Param("skuId") Long skuId, @Param("quantity") int quantity);
}
