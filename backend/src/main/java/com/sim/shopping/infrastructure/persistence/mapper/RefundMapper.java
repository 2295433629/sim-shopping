package com.sim.shopping.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sim.shopping.infrastructure.persistence.entity.RefundDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RefundMapper extends BaseMapper<RefundDO> {

    @Select("SELECT * FROM t_order_refund WHERE order_no = #{orderNo} AND deleted = 0 LIMIT 1")
    RefundDO selectByOrderNo(@Param("orderNo") String orderNo);
}
