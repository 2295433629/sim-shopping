package com.sim.shopping.application.order;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.domain.common.exception.OrderException;
import com.sim.shopping.domain.event.OrderCancelledEvent;
import com.sim.shopping.domain.event.OrderCreatedEvent;
import com.sim.shopping.infrastructure.persistence.entity.*;
import com.sim.shopping.infrastructure.persistence.mapper.*;
import com.sim.shopping.application.points.PointsService;
import com.sim.shopping.application.refund.RefundService;
import com.sim.shopping.application.settlement.SettlementService;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.order.*;
import com.sim.shopping.interfaces.dto.refund.RefundVO;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单服务，处理订单创建、支付、发货、取消、完成等完整生命周期
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Service
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final CartItemMapper cartItemMapper;
    private final ShoppingCartMapper shoppingCartMapper;
    private final ProductMapper productMapper;
    private final ProductSkuMapper productSkuMapper;
    private final UserAddressMapper userAddressMapper;
    private final ShopMapper shopMapper;
    private final PaymentMapper paymentMapper;
    private final ShipmentMapper shipmentMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final RefundService refundService;
    private final PointsService pointsService;
    private final SettlementService settlementService;

    private static final BigDecimal DEFAULT_SHIPPING_FEE = new BigDecimal("10.00");
    private static final String ORDER_STATUS_CREATED = "CREATED";
    private static final String ORDER_STATUS_CANCELLED = "CANCELLED";
    private static final String ORDER_STATUS_DELIVERED = "DELIVERED";
    private static final String ORDER_STATUS_COMPLETED = "COMPLETED";

    public OrderService(OrderMapper orderMapper,
                       OrderItemMapper orderItemMapper,
                       CartItemMapper cartItemMapper,
                       ShoppingCartMapper shoppingCartMapper,
                       ProductMapper productMapper,
                       ProductSkuMapper productSkuMapper,
                       UserAddressMapper userAddressMapper,
                       ShopMapper shopMapper,
                       PaymentMapper paymentMapper,
                       ShipmentMapper shipmentMapper,
                       ApplicationEventPublisher eventPublisher,
                       RefundService refundService,
                       PointsService pointsService,
                       SettlementService settlementService) {
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.cartItemMapper = cartItemMapper;
        this.shoppingCartMapper = shoppingCartMapper;
        this.productMapper = productMapper;
        this.productSkuMapper = productSkuMapper;
        this.userAddressMapper = userAddressMapper;
        this.shopMapper = shopMapper;
        this.paymentMapper = paymentMapper;
        this.shipmentMapper = shipmentMapper;
        this.eventPublisher = eventPublisher;
        this.refundService = refundService;
        this.pointsService = pointsService;
        this.settlementService = settlementService;
    }

    /**
     * 创建订单（按购物车选中商品结算）
     * @param userId userId
     * @param addressId addressId
     * @param remark remark
     * @param cartItemIds cartItemIds
     * @return 返回结果
     */
    @Transactional
    public List<OrderResponse> createOrder(Long userId, Long addressId, String remark, List<Long> cartItemIds) {
        if (cartItemIds == null || cartItemIds.isEmpty()) {
            throw new OrderException.EmptyCartException("购物车项不能为空");
        }

        // Validate address
        UserAddressDO address = userAddressMapper.selectById(addressId);
        if (address == null || !userId.equals(address.getUserId())) {
            throw new BusinessException(400, "收货地址不存在或不属于当前用户");
        }

        String fullAddress = address.getProvince() + address.getCity() + address.getDistrict() + address.getDetailAddress();

        // Get cart items
        List<CartItemDO> cartItems = cartItemMapper.selectBatchIds(cartItemIds);
        if (cartItems.size() != cartItemIds.size()) {
            throw new BusinessException(400, "部分购物车项不存在");
        }

        // Validate cart items belong to user's cart
        for (CartItemDO cartItem : cartItems) {
            ShoppingCartDO cart = shoppingCartMapper.selectById(cartItem.getCartId());
            if (cart == null || !userId.equals(cart.getUserId())) {
                throw new BusinessException(403, "购物车项不属于当前用户");
            }
        }

        // Group cart items by shopId
        Map<Long, List<CartItemDO>> itemsByShop = cartItems.stream()
                .collect(Collectors.groupingBy(CartItemDO::getShopId));

        List<OrderResponse> createdOrders = new ArrayList<>();

        for (Map.Entry<Long, List<CartItemDO>> entry : itemsByShop.entrySet()) {
            Long shopId = entry.getKey();
            List<CartItemDO> shopCartItems = entry.getValue();

            // Get shop name
            ShopDO shop = shopMapper.selectById(shopId);
            String shopName = shop != null ? shop.getShopName() : "";

            // Calculate total amount and build order items
            BigDecimal totalAmount = BigDecimal.ZERO;
            List<OrderItemDO> orderItems = new ArrayList<>();

            for (CartItemDO cartItem : shopCartItems) {
                ProductDO product = productMapper.selectById(cartItem.getProductId());
                if (product == null) {
                    throw new BusinessException(400, "商品不存在: " + cartItem.getProductId());
                }
                if (product.getStock() == null || cartItem.getQuantity() == null
                        || product.getStock() < cartItem.getQuantity()) {
                    throw new BusinessException(400, "商品库存不足: " + product.getName());
                }

                BigDecimal price = product.getPrice();
                BigDecimal subtotal = price.multiply(BigDecimal.valueOf(cartItem.getQuantity()));
                totalAmount = totalAmount.add(subtotal);

                OrderItemDO orderItem = new OrderItemDO();
                orderItem.setProductId(product.getId());
                orderItem.setProductName(product.getName());
                orderItem.setProductImage(product.getMainImage());
                orderItem.setSkuId(cartItem.getSkuId());
                orderItem.setSkuName(product.getSubtitle());
                orderItem.setPrice(price);
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setSubtotal(subtotal);
                orderItems.add(orderItem);

                // 原子扣减库存并增加销量（防止并发超卖）
                int rows = productMapper.deductStock(product.getId(), cartItem.getQuantity());
                if (rows == 0) {
                    throw new BusinessException(400, "商品库存不足: " + product.getName());
                }

                // 原子扣减SKU库存（防止并发超卖）
                if (cartItem.getSkuId() != null) {
                    int skuRows = productSkuMapper.deductStock(cartItem.getSkuId(), cartItem.getQuantity());
                    if (skuRows == 0) {
                        throw new BusinessException(400, "商品SKU库存不足: " + product.getName());
                    }
                }
            }

            BigDecimal discountAmount = BigDecimal.ZERO;
            BigDecimal payAmount = totalAmount.add(DEFAULT_SHIPPING_FEE).subtract(discountAmount);

            // Create order
            OrderDO order = new OrderDO();
            order.setOrderNo(generateOrderNo());
            order.setUserId(userId);
            order.setShopId(shopId);
            order.setTotalAmount(totalAmount);
            order.setShippingFee(DEFAULT_SHIPPING_FEE);
            order.setDiscountAmount(discountAmount);
            order.setPayAmount(payAmount);
            order.setStatus(ORDER_STATUS_CREATED);
            order.setReceiverName(address.getReceiverName());
            order.setReceiverPhone(address.getReceiverPhone());
            order.setReceiverAddress(fullAddress);
            order.setRemark(remark);
            order.setAutoConfirm(0);

            orderMapper.insert(order);

            // Save order items
            for (OrderItemDO orderItem : orderItems) {
                orderItem.setOrderId(order.getId());
                orderItemMapper.insert(orderItem);
            }

            // Publish event
            eventPublisher.publishEvent(new OrderCreatedEvent(order.getOrderNo(), userId, shopId, totalAmount));

            // Build response
            createdOrders.add(convertToOrderResponse(order, shopName, orderItems));
        }

        // Remove purchased items from cart
        cartItemMapper.deleteBatchIds(cartItemIds);

        return createdOrders;
    }

    /**
     * 查询用户订单列表（支持分页和按状态筛选）
     * @param userId userId
     * @param page page
     * @param size size
     * @param status status
     * @return 返回结果
     */
    public PageResponse<OrderListItemVO> getUserOrders(Long userId, int page, int size, String status) {
        Page<OrderDO> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<OrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDO::getUserId, userId);
        if (status != null && !status.isEmpty()) {
            wrapper.eq(OrderDO::getStatus, status);
        }
        wrapper.orderByDesc(OrderDO::getCreatedAt);

        IPage<OrderDO> result = orderMapper.selectPage(pageObj, wrapper);

        List<OrderListItemVO> list = result.getRecords().stream()
                .map(order -> convertToOrderListItemVO(order))
                .collect(Collectors.toList());

        return PageResponse.of(list, result.getTotal(), page, size);
    }

    /**
     * 查询订单详情
     * @param orderNo orderNo
     * @return 返回结果
     */
    public OrderDetailVO getOrderDetail(String orderNo) {
        OrderDO order = orderByNo(orderNo);
        if (order == null) {
            throw new OrderException.OrderNotFoundException("订单不存在: " + orderNo);
        }
        return convertToOrderDetailVO(order);
    }

    /**
     * 查询用户订单详情（含权限校验）
     * @param orderNo orderNo
     * @param userId userId
     * @return 返回结果
     */
    public OrderDetailVO getOrderDetailForUser(String orderNo, Long userId) {
        OrderDO order = orderByNo(orderNo);
        if (order == null) {
            throw new OrderException.OrderNotFoundException("订单不存在: " + orderNo);
        }
        if (!userId.equals(order.getUserId())) {
            throw new BusinessException(403, "无权查看此订单");
        }
        return convertToOrderDetailVO(order);
    }

    /**
     * 查询商家订单详情（含权限校验）
     * @param orderNo orderNo
     * @param shopId shopId
     * @return 返回结果
     */
    public OrderDetailVO getOrderDetailForShop(String orderNo, Long shopId) {
        OrderDO order = orderByNo(orderNo);
        if (order == null) {
            throw new OrderException.OrderNotFoundException("订单不存在: " + orderNo);
        }
        if (!shopId.equals(order.getShopId())) {
            throw new BusinessException(403, "无权查看此订单");
        }
        return convertToOrderDetailVO(order);
    }

    /**
     * 取消订单
     * @param userId userId
     * @param orderNo orderNo
     */
    @Transactional
    public void cancelOrder(Long userId, String orderNo) {
        OrderDO order = orderByNo(orderNo);
        if (order == null) {
            throw new OrderException.OrderNotFoundException("订单不存在: " + orderNo);
        }
        if (!userId.equals(order.getUserId())) {
            throw new BusinessException(403, "无权操作此订单");
        }
        if (!ORDER_STATUS_CREATED.equals(order.getStatus())) {
            throw new OrderException.OrderCannotCancelException("只能取消待支付的订单");
        }

        order.setStatus(ORDER_STATUS_CANCELLED);
        order.setCancelledAt(LocalDateTime.now());
        orderMapper.updateById(order);

        // Restore product stock
        LambdaQueryWrapper<OrderItemDO> itemWrapper = new LambdaQueryWrapper<>();
        itemWrapper.eq(OrderItemDO::getOrderId, order.getId());
        List<OrderItemDO> items = orderItemMapper.selectList(itemWrapper);

        for (OrderItemDO item : items) {
            ProductDO product = productMapper.selectById(item.getProductId());
            if (product != null) {
                product.setStock(product.getStock() + item.getQuantity());
                product.setSales(Math.max(0, product.getSales() - item.getQuantity()));
                productMapper.updateById(product);
            }
            // 恢复SKU库存（下单时扣减了SKU库存，取消时需要恢复）
            if (item.getSkuId() != null && item.getQuantity() != null) {
                ProductSkuDO sku = productSkuMapper.selectById(item.getSkuId());
                if (sku != null) {
                    sku.setStock(sku.getStock() + item.getQuantity());
                    productSkuMapper.updateById(sku);
                }
            }
        }

        // Publish event
        eventPublisher.publishEvent(new OrderCancelledEvent(orderNo, userId, order.getShopId()));
    }

    /**
     * 确认收货
     * @param userId userId
     * @param orderNo orderNo
     */
    @Transactional
    public void confirmReceive(Long userId, String orderNo) {
        OrderDO order = orderByNo(orderNo);
        if (order == null) {
            throw new OrderException.OrderNotFoundException("订单不存在: " + orderNo);
        }
        if (!userId.equals(order.getUserId())) {
            throw new BusinessException(403, "无权操作此订单");
        }
        if (!ORDER_STATUS_DELIVERED.equals(order.getStatus())) {
            throw new OrderException.OrderStatusException("只能确认已送达的订单");
        }

        order.setStatus(ORDER_STATUS_COMPLETED);
        order.setCompletedAt(LocalDateTime.now());
        orderMapper.updateById(order);

        // 购物返积分：每10元累计1积分，不足10元不计
        BigDecimal payAmount = order.getPayAmount() != null ? order.getPayAmount() : BigDecimal.ZERO;
        int earnedPoints = payAmount.divideToIntegralValue(BigDecimal.TEN).intValue();
        if (earnedPoints > 0) {
            pointsService.grantOrderRewardPoints(userId, order.getId(), orderNo, earnedPoints);
        }

        // 订单结算：金额入商户账户
        settlementService.settleOrder(order.getId(), orderNo, order.getShopId(), payAmount);
    }

    /**
     * 查询商家订单列表
     * @param shopId shopId
     * @param page page
     * @param size size
     * @param status status
     * @param keyword keyword
     * @return 返回结果
     */
    public PageResponse<OrderListItemVO> getMerchantOrders(Long shopId, int page, int size, String status, String keyword) {
        Page<OrderDO> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<OrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDO::getShopId, shopId);
        if (status != null && !status.isEmpty()) {
            wrapper.eq(OrderDO::getStatus, status);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(OrderDO::getOrderNo, keyword)
                    .or().like(OrderDO::getReceiverName, keyword));
        }
        wrapper.orderByDesc(OrderDO::getCreatedAt);

        IPage<OrderDO> result = orderMapper.selectPage(pageObj, wrapper);

        List<OrderListItemVO> list = result.getRecords().stream()
                .map(order -> convertToOrderListItemVO(order))
                .collect(Collectors.toList());

        return PageResponse.of(list, result.getTotal(), page, size);
    }

    /**
     * 查询全平台订单列表（管理员）
     * @param page page
     * @param size size
     * @param status status
     * @param shopId shopId
     * @param keyword keyword
     * @return 返回结果
     */
    public PageResponse<OrderListItemVO> getAdminOrders(int page, int size, String status, Long shopId, String keyword) {
        Page<OrderDO> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<OrderDO> wrapper = new LambdaQueryWrapper<>();
        if (status != null && !status.isEmpty()) {
            wrapper.eq(OrderDO::getStatus, status);
        }
        if (shopId != null) {
            wrapper.eq(OrderDO::getShopId, shopId);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(OrderDO::getOrderNo, keyword)
                    .or().like(OrderDO::getReceiverName, keyword));
        }
        wrapper.orderByDesc(OrderDO::getCreatedAt);

        IPage<OrderDO> result = orderMapper.selectPage(pageObj, wrapper);

        List<OrderListItemVO> list = result.getRecords().stream()
                .map(order -> convertToOrderListItemVO(order))
                .collect(Collectors.toList());

        return PageResponse.of(list, result.getTotal(), page, size);
    }

    /**
     * 校验订单归属当前用户
     * @param orderNo orderNo
     * @param userId userId
     */
    public void validateOrderOwnership(String orderNo, Long userId) {
        OrderDO order = orderByNo(orderNo);
        if (order != null && !userId.equals(order.getUserId())) {
            throw new BusinessException(403, "无权操作此订单");
        }
    }

    /**
     * 查询待发货订单分页（商家物流管理）
     * @param shopId shopId
     * @param page page
     * @param size size
     * @return 返回结果
     */
    public PageResponse<OrderListItemVO> getPendingShipmentOrders(Long shopId, int page, int size) {
        Page<OrderDO> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<OrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDO::getShopId, shopId);
        wrapper.eq(OrderDO::getStatus, "PAID");
        wrapper.orderByAsc(OrderDO::getCreatedAt);

        IPage<OrderDO> result = orderMapper.selectPage(pageObj, wrapper);

        List<OrderListItemVO> list = result.getRecords().stream()
                .map(order -> convertToOrderListItemVO(order))
                .collect(Collectors.toList());

        return PageResponse.of(list, result.getTotal(), page, size);
    }

    // ========== Private helper methods ==========

    private OrderDO orderByNo(String orderNo) {
        LambdaQueryWrapper<OrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDO::getOrderNo, orderNo);
        return orderMapper.selectOne(wrapper);
    }

    private String generateOrderNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = randomAlphanumeric(4);
        return "SD" + timestamp + random;
    }

    private String randomAlphanumeric(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        java.util.concurrent.ThreadLocalRandom random = java.util.concurrent.ThreadLocalRandom.current();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
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

    private String getShopName(Long shopId) {
        if (shopId == null) return "";
        ShopDO shop = shopMapper.selectById(shopId);
        return shop != null ? shop.getShopName() : "";
    }

    private OrderResponse convertToOrderResponse(OrderDO order, String shopName, List<OrderItemDO> orderItems) {
        OrderResponse resp = new OrderResponse();
        resp.setOrderId(order.getId());
        resp.setOrderNo(order.getOrderNo());
        resp.setStatus(order.getStatus());
        resp.setTotalAmount(order.getTotalAmount());
        resp.setShippingFee(order.getShippingFee());
        resp.setDiscountAmount(order.getDiscountAmount());
        resp.setPayAmount(order.getPayAmount());
        resp.setShopId(order.getShopId());
        resp.setShopName(shopName);
        resp.setCreatedAt(order.getCreatedAt());
        resp.setPaidAt(order.getPaidAt());
        resp.setShippedAt(order.getShippedAt());

        List<OrderItemVO> itemVOs = new ArrayList<>();
        for (OrderItemDO item : orderItems) {
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
        resp.setItems(itemVOs);
        return resp;
    }

    private OrderListItemVO convertToOrderListItemVO(OrderDO order) {
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

    private OrderDetailVO convertToOrderDetailVO(OrderDO order) {
        OrderDetailVO vo = new OrderDetailVO();
        vo.setOrderId(order.getId());
        vo.setOrderNo(order.getOrderNo());
        vo.setStatus(order.getStatus());
        vo.setTotalAmount(order.getTotalAmount());
        vo.setShippingFee(order.getShippingFee());
        vo.setDiscountAmount(order.getDiscountAmount());
        vo.setPayAmount(order.getPayAmount());
        vo.setShopId(order.getShopId());
        vo.setShopName(getShopName(order.getShopId()));
        vo.setCreatedAt(order.getCreatedAt());
        vo.setPaidAt(order.getPaidAt());
        vo.setShippedAt(order.getShippedAt());
        vo.setDeliveredAt(order.getDeliveredAt());
        vo.setCompletedAt(order.getCompletedAt());
        vo.setCancelledAt(order.getCancelledAt());
        vo.setReceiverName(order.getReceiverName());
        vo.setReceiverPhone(order.getReceiverPhone());
        vo.setReceiverAddress(order.getReceiverAddress());
        vo.setRemark(order.getRemark());
        vo.setItems(getOrderItemVOs(order.getId()));

        // Payment info
        LambdaQueryWrapper<PaymentDO> payWrapper = new LambdaQueryWrapper<>();
        payWrapper.eq(PaymentDO::getOrderNo, order.getOrderNo());
        PaymentDO payment = paymentMapper.selectOne(payWrapper);
        if (payment != null) {
            PaymentInfoVO payVO = new PaymentInfoVO();
            payVO.setPaymentNo(payment.getPaymentNo());
            payVO.setPaymentMethod(payment.getPaymentMethod());
            payVO.setAmount(payment.getAmount());
            payVO.setStatus(payment.getStatus());
            payVO.setPaidAt(payment.getPaidAt());
            vo.setPayment(payVO);
        }

        // Logistics info
        LambdaQueryWrapper<ShipmentDO> shipWrapper = new LambdaQueryWrapper<>();
        shipWrapper.eq(ShipmentDO::getOrderId, order.getId());
        ShipmentDO shipment = shipmentMapper.selectOne(shipWrapper);
        if (shipment != null) {
            LogisticsInfoVO logVO = new LogisticsInfoVO();
            logVO.setTrackingNo(shipment.getShipmentNo());
            logVO.setLogisticsCompany(shipment.getLogisticsCompany());
            logVO.setStatus(shipment.getStatus());
            vo.setLogistics(logVO);
        }

        // Refund info
        RefundVO refund = refundService.getRefundByOrderNo(order.getOrderNo());
        if (refund != null) {
            vo.setRefund(refund);
        }

        return vo;
    }
}
