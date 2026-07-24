package com.sim.shopping.application.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sim.shopping.infrastructure.persistence.entity.MerchantDO;
import com.sim.shopping.infrastructure.persistence.entity.OrderDO;
import com.sim.shopping.infrastructure.persistence.entity.OrderItemDO;
import com.sim.shopping.infrastructure.persistence.entity.ProductDO;
import com.sim.shopping.infrastructure.persistence.entity.ShopDO;
import com.sim.shopping.infrastructure.persistence.entity.UserDO;
import com.sim.shopping.infrastructure.common.SystemConstants;
import com.sim.shopping.infrastructure.persistence.mapper.*;
import com.sim.shopping.interfaces.dto.order.OrderItemVO;
import com.sim.shopping.interfaces.dto.order.OrderListItemVO;
import com.sim.shopping.interfaces.dto.system.DashboardVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * AdminDashboard服务，处理相关业务逻辑
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Service
public class AdminDashboardService {

    private final UserMapper userMapper;
    private final MerchantMapper merchantMapper;
    private final OrderMapper orderMapper;
    private final ProductMapper productMapper;
    private final ShopMapper shopMapper;
    private final OrderItemMapper orderItemMapper;

    private static final List<String> PAID_STATUS_LIST = Arrays.asList(
            "PAID", "SHIPPED", "IN_TRANSIT", "DELIVERED", "COMPLETED"
    );

    public AdminDashboardService(UserMapper userMapper,
                                 MerchantMapper merchantMapper,
                                 OrderMapper orderMapper,
                                 ProductMapper productMapper,
                                 ShopMapper shopMapper,
                                 OrderItemMapper orderItemMapper) {
        this.userMapper = userMapper;
        this.merchantMapper = merchantMapper;
        this.orderMapper = orderMapper;
        this.productMapper = productMapper;
        this.shopMapper = shopMapper;
        this.orderItemMapper = orderItemMapper;
    }

    /**
     * 获取Dashboard
     * @return 返回结果
     */
    public DashboardVO getDashboard() {
        DashboardVO vo = new DashboardVO();

        // Total counts
        vo.setTotalUsers(userMapper.selectCount(null));
        vo.setTotalMerchants(merchantMapper.selectCount(null));
        vo.setTotalOrders(orderMapper.selectCount(null));
        vo.setTotalProducts(productMapper.selectCount(null));

        // Total sales: sum payAmount from orders with paid statuses (SQL聚合)
        vo.setTotalSales(orderMapper.sumPayAmountByStatus(PAID_STATUS_LIST));

        // Today's start and end
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd = LocalDate.now().atTime(LocalTime.MAX);

        // Today orders (SQL COUNT)
        vo.setTodayOrders(orderMapper.countByDateRange(todayStart, todayEnd));

        // Today sales (SQL SUM)
        vo.setTodaySales(orderMapper.sumPayAmountByDateAndStatus(todayStart, todayEnd, PAID_STATUS_LIST));

        // Pending merchants
        Long pendingMerchants = merchantMapper.selectCount(
                Wrappers.<MerchantDO>lambdaQuery().eq(MerchantDO::getStatus, SystemConstants.MERCHANT_STATUS_PENDING)
        );
        vo.setPendingMerchants(pendingMerchants);

        // Recent 10 orders
        LambdaQueryWrapper<OrderDO> recentWrapper = new LambdaQueryWrapper<>();
        recentWrapper.orderByDesc(OrderDO::getCreatedAt).last("LIMIT 10");
        List<OrderDO> recentOrders = orderMapper.selectList(recentWrapper);
        List<OrderListItemVO> recentOrderVOs = recentOrders.stream()
                .map(this::toOrderListItemVO)
                .collect(Collectors.toList());
        vo.setRecentOrders(recentOrderVOs);

        return vo;
    }

    private String getShopName(Long shopId) {
        if (shopId == null) return "";
        ShopDO shop = shopMapper.selectById(shopId);
        return shop != null ? shop.getShopName() : "";
    }

    private List<OrderItemVO> getOrderItemVOs(Long orderId) {
        LambdaQueryWrapper<OrderItemDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderItemDO::getOrderId, orderId);
        List<OrderItemDO> items = orderItemMapper.selectList(wrapper);
        List<OrderItemVO> itemVOs = new ArrayList<>();
        for (OrderItemDO item : items) {
            OrderItemVO vo = new OrderItemVO();
            vo.setProductId(item.getProductId());
            vo.setProductName(item.getProductName());
            vo.setProductImage(item.getProductImage());
            vo.setSkuId(item.getSkuId());
            vo.setSkuName(item.getSkuName());
            vo.setPrice(item.getPrice());
            vo.setQuantity(item.getQuantity());
            vo.setSubtotal(item.getSubtotal());
            itemVOs.add(vo);
        }
        return itemVOs;
    }

    private OrderListItemVO toOrderListItemVO(OrderDO order) {
        OrderListItemVO vo = new OrderListItemVO();
        vo.setOrderId(order.getId());
        vo.setOrderNo(order.getOrderNo());
        vo.setStatus(order.getStatus());
        vo.setTotalAmount(order.getTotalAmount());
        vo.setPayAmount(order.getPayAmount());
        vo.setShopId(order.getShopId());
        vo.setShopName(getShopName(order.getShopId()));
        vo.setCreatedAt(order.getCreatedAt());
        vo.setItems(getOrderItemVOs(order.getId()));
        return vo;
    }
}
