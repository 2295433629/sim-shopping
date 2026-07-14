package com.sim.shopping.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sim.shopping.infrastructure.persistence.entity.FlashSaleDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface FlashSaleMapper extends BaseMapper<FlashSaleDO> {

    /**
     * 原子扣减秒杀库存并增加已售数量。
     */
    @Update("UPDATE t_flash_sale SET stock = stock - #{quantity}, sold_count = sold_count + #{quantity} " +
            "WHERE id = #{saleId} AND stock >= #{quantity} AND deleted = 0 AND status = 'ACTIVE'")
    int deductStock(@Param("saleId") Long saleId, @Param("quantity") int quantity);

    @Select("SELECT fs.*, p.name as productName, p.main_image as productImage " +
            "FROM t_flash_sale fs " +
            "LEFT JOIN t_product p ON fs.product_id = p.id " +
            "WHERE fs.status = #{status} " +
            "AND fs.start_time <= #{now} " +
            "AND fs.end_time >= #{now} " +
            "AND fs.deleted = 0 " +
            "ORDER BY fs.start_time DESC")
    List<FlashSaleDO> selectActiveFlashSales(@Param("status") String status, @Param("now") LocalDateTime now);

    @Select("SELECT fs.*, p.name as productName, p.main_image as productImage " +
            "FROM t_flash_sale fs " +
            "LEFT JOIN t_product p ON fs.product_id = p.id " +
            "WHERE fs.id = #{saleId} " +
            "AND fs.deleted = 0")
    FlashSaleDO selectFlashSaleWithProduct(@Param("saleId") Long saleId);
}
