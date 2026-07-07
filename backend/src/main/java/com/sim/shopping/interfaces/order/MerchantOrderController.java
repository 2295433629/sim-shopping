package com.sim.shopping.interfaces.order;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sim.shopping.application.order.OrderService;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.infrastructure.persistence.entity.MerchantDO;
import com.sim.shopping.infrastructure.persistence.entity.ShopDO;
import com.sim.shopping.infrastructure.persistence.mapper.MerchantMapper;
import com.sim.shopping.infrastructure.persistence.mapper.ShopMapper;
import com.sim.shopping.infrastructure.security.SecurityUtils;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.order.OrderDetailVO;
import com.sim.shopping.interfaces.dto.order.OrderListItemVO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/merchant/orders")
public class MerchantOrderController {

    private final OrderService orderService;
    private final MerchantMapper merchantMapper;
    private final ShopMapper shopMapper;

    public MerchantOrderController(OrderService orderService,
                                   MerchantMapper merchantMapper,
                                   ShopMapper shopMapper) {
        this.orderService = orderService;
        this.merchantMapper = merchantMapper;
        this.shopMapper = shopMapper;
    }

    @GetMapping
    public ApiResponse<PageResponse<OrderListItemVO>> getMerchantOrders(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword) {
        Long shopId = resolveShopId();
        return ApiResponse.success(orderService.getMerchantOrders(shopId, page, size, status, keyword));
    }

    @GetMapping("/{orderNo}")
    public ApiResponse<OrderDetailVO> getOrderDetail(@PathVariable String orderNo) {
        Long shopId = resolveShopId();
        return ApiResponse.success(orderService.getOrderDetailForShop(orderNo, shopId));
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
}
