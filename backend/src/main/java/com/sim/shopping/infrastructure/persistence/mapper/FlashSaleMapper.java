package com.sim.shopping.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sim.shopping.infrastructure.persistence.entity.FlashSaleDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface FlashSaleMapper extends BaseMapper<FlashSaleDO> {

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
