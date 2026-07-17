package com.sim.shopping.interfaces.shipment;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sim.shopping.application.shipment.ShipmentService;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.infrastructure.persistence.entity.MerchantDO;
import com.sim.shopping.infrastructure.persistence.entity.OrderDO;
import com.sim.shopping.infrastructure.persistence.entity.ShopDO;
import com.sim.shopping.infrastructure.persistence.mapper.MerchantMapper;
import com.sim.shopping.infrastructure.persistence.mapper.OrderMapper;
import com.sim.shopping.infrastructure.persistence.mapper.ShopMapper;
import com.sim.shopping.infrastructure.security.SecurityUtils;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.order.OrderListItemVO;
import com.sim.shopping.interfaces.dto.shipment.CreateShipmentRequest;
import com.sim.shopping.interfaces.dto.shipment.ShipmentResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 物流管理控制器，处理物流信息查询和轨迹追踪
 *
 * @author Sim Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/merchant")
@PreAuthorize("hasRole('MERCHANT')")
public class ShipmentController {

    private final ShipmentService shipmentService;
    private final MerchantMapper merchantMapper;
    private final ShopMapper shopMapper;
    private final OrderMapper orderMapper;

    public ShipmentController(ShipmentService shipmentService,
                              MerchantMapper merchantMapper,
                              ShopMapper shopMapper,
                              OrderMapper orderMapper) {
        this.shipmentService = shipmentService;
        this.merchantMapper = merchantMapper;
        this.shopMapper = shopMapper;
        this.orderMapper = orderMapper;
    }

    /**
     * 创建Shipment
     * @param req req
     * @return 返回结果
     */
    @PostMapping("/shipments")
    public ApiResponse<ShipmentResponse> createShipment(@RequestBody CreateShipmentRequest req) {
        Long shopId = resolveShopId();
        return ApiResponse.success(shipmentService.createShipment(shopId, req));
    }

    /**
     * 获取Pending Shipment Orders
     * @return 返回结果
     */
    @GetMapping("/orders/shipping")
    public ApiResponse<PageResponse<OrderListItemVO>> getPendingShipmentOrders(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long shopId = resolveShopId();
        Page<OrderDO> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<OrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDO::getShopId, shopId);
        wrapper.eq(OrderDO::getStatus, "PAID");
        wrapper.orderByAsc(OrderDO::getCreatedAt);

        IPage<OrderDO> result = orderMapper.selectPage(pageObj, wrapper);

        java.util.List<OrderListItemVO> list = result.getRecords().stream()
                .map(this::convertToOrderListItemVO)
                .collect(java.util.stream.Collectors.toList());

        return ApiResponse.success(PageResponse.of(list, result.getTotal(), page, size));
    }

    private Long resolveShopId() {
        Long userId = SecurityUtils.getCurrentUserId();
        LambdaQueryWrapper<MerchantDO> merchantWrapper = new LambdaQueryWrapper<>();
        merchantWrapper.eq(MerchantDO::getUserId, userId);
        MerchantDO merchant = merchantMapper.selectOne(merchantWrapper);
        if (merchant == null) {
            throw new BusinessException(403, "非商家用户");
        }
        LambdaQueryWrapper<ShopDO> shopWrapper = new LambdaQueryWrapper<>();
        shopWrapper.eq(ShopDO::getMerchantId, merchant.getId());
        ShopDO shop = shopMapper.selectOne(shopWrapper);
        if (shop == null) {
            throw new BusinessException(403, "店铺不存在");
        }
        return shop.getId();
    }

    private OrderListItemVO convertToOrderListItemVO(OrderDO order) {
        OrderListItemVO vo = new OrderListItemVO();
        vo.setOrderId(order.getId());
        vo.setOrderNo(order.getOrderNo());
        vo.setStatus(order.getStatus());
        vo.setTotalAmount(order.getTotalAmount());
        vo.setPayAmount(order.getPayAmount());
        vo.setShopId(order.getShopId());
        vo.setCreatedAt(order.getCreatedAt());
        return vo;
    }
}
