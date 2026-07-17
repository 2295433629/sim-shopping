package com.sim.shopping.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sim.shopping.infrastructure.persistence.entity.OrderDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单数据访问层，提供订单表的CRUD操作
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Mapper
public interface OrderMapper extends BaseMapper<OrderDO> {

    /** 按状态列表统计支付金额总和 */
    @Select("<script>SELECT COALESCE(SUM(pay_amount), 0) FROM t_order WHERE status IN " +
            "<foreach collection='statusList' item='s' open='(' separator=',' close=')'>#{s}</foreach> " +
            "AND deleted = 0</script>")
    BigDecimal sumPayAmountByStatus(@Param("statusList") List<String> statusList);

    /** 按日期范围和状态统计订单数和支付金额 */
    @Select("<script>SELECT COALESCE(SUM(pay_amount), 0) FROM t_order " +
            "WHERE created_at BETWEEN #{start} AND #{end} " +
            "AND status IN <foreach collection='statusList' item='s' open='(' separator=',' close=')'>#{s}</foreach> " +
            "AND deleted = 0</script>")
    BigDecimal sumPayAmountByDateAndStatus(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                            @Param("statusList") List<String> statusList);

    /** 按日期范围统计订单数量 */
    @Select("SELECT COUNT(*) FROM t_order WHERE created_at BETWEEN #{start} AND #{end} AND deleted = 0")
    Long countByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
