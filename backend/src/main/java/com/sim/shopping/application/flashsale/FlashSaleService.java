package com.sim.shopping.application.flashsale;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.infrastructure.persistence.entity.FlashSaleDO;
import com.sim.shopping.infrastructure.persistence.entity.OrderDO;
import com.sim.shopping.infrastructure.persistence.entity.OrderItemDO;
import com.sim.shopping.infrastructure.persistence.entity.ProductDO;
import com.sim.shopping.infrastructure.persistence.entity.UserAddressDO;
import com.sim.shopping.infrastructure.persistence.mapper.FlashSaleMapper;
import com.sim.shopping.infrastructure.persistence.mapper.OrderItemMapper;
import com.sim.shopping.infrastructure.persistence.mapper.OrderMapper;
import com.sim.shopping.infrastructure.persistence.mapper.ProductMapper;
import com.sim.shopping.infrastructure.persistence.mapper.UserAddressMapper;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.flashsale.FlashSaleDetailResponse;
import com.sim.shopping.interfaces.dto.flashsale.FlashSaleOrderResponse;
import com.sim.shopping.interfaces.dto.flashsale.FlashSaleResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sim.shopping.infrastructure.common.OrderConstants;
import com.sim.shopping.infrastructure.common.OrderNoGenerator;
import com.sim.shopping.infrastructure.common.SystemConstants;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 秒杀服务，处理秒杀活动的创建、商品管理和库存扣减
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Service
public class FlashSaleService {

    private static final String STATUS_INACTIVE = SystemConstants.STATUS_INACTIVE;
    private static final String STATUS_ENDED = SystemConstants.STATUS_ENDED;

    private final FlashSaleMapper flashSaleMapper;
    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final UserAddressMapper userAddressMapper;

    public FlashSaleService(FlashSaleMapper flashSaleMapper,
                            ProductMapper productMapper,
                            OrderMapper orderMapper,
                            OrderItemMapper orderItemMapper,
                            UserAddressMapper userAddressMapper) {
        this.flashSaleMapper = flashSaleMapper;
        this.productMapper = productMapper;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.userAddressMapper = userAddressMapper;
    }

    /**
     * 获取Active Flash Sales
     * @param page page
     * @param size size
     * @return 返回结果
     */
    public PageResponse<FlashSaleResponse> getActiveFlashSales(int page, int size) {
        if (page < 1) {
            page = 1;
        }
        if (size < 1) {
            size = 10;
        }

        LocalDateTime now = LocalDateTime.now();
        List<FlashSaleDO> activeSales = flashSaleMapper.selectActiveFlashSales(SystemConstants.STATUS_ACTIVE, now);

        int total = activeSales.size();
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, total);

        List<FlashSaleDO> pageList;
        if (fromIndex >= total) {
            pageList = new ArrayList<>();
        } else {
            pageList = activeSales.subList(fromIndex, toIndex);
        }

        List<FlashSaleResponse> responses = pageList.stream()
                .map(this::convertToFlashSaleResponse)
                .collect(Collectors.toList());

        return PageResponse.of(responses, total, page, size);
    }

    /**
     * 获取Flash Sale Detail
     * @param saleId saleId
     * @return 返回结果
     */
    public FlashSaleDetailResponse getFlashSaleDetail(Long saleId) {
        if (saleId == null) {
            throw new BusinessException(400, "秒杀活动ID不能为空");
        }
        FlashSaleDO flashSale = flashSaleMapper.selectFlashSaleWithProduct(saleId);
        if (flashSale == null) {
            throw new BusinessException(404, "秒杀活动不存在");
        }
        return convertToFlashSaleDetailResponse(flashSale);
    }

    /**
     * 创建Flash Sale Order
     * @param userId userId
     * @param saleId saleId
     * @param addressId addressId
     * @param quantity quantity
     * @return 返回结果
     */
    @Transactional(rollbackFor = Exception.class)
    public FlashSaleOrderResponse createFlashSaleOrder(Long userId, Long saleId, Long addressId, Integer quantity) {
        if (userId == null) {
            throw new BusinessException(401, "用户未登录");
        }
        if (saleId == null) {
            throw new BusinessException(400, "秒杀活动ID不能为空");
        }
        if (addressId == null) {
            throw new BusinessException(400, "收货地址不能为空");
        }
        if (quantity == null || quantity < 1) {
            throw new BusinessException(400, "购买数量必须大于0");
        }

        FlashSaleDO flashSale = flashSaleMapper.selectById(saleId);
        if (flashSale == null) {
            throw new BusinessException(404, "秒杀活动不存在");
        }

        LocalDateTime now = LocalDateTime.now();
        if (flashSale.getStartTime() != null && now.isBefore(flashSale.getStartTime())) {
            throw new BusinessException(400, "秒杀活动尚未开始");
        }
        if (flashSale.getEndTime() != null && now.isAfter(flashSale.getEndTime())) {
            throw new BusinessException(400, "秒杀活动已结束");
        }
        if (!SystemConstants.STATUS_ACTIVE.equals(flashSale.getStatus())) {
            throw new BusinessException(400, "秒杀活动未在进行中");
        }

        if (flashSale.getStock() == null || flashSale.getStock() < quantity) {
            throw new BusinessException(400, "库存不足");
        }

        if (flashSale.getLimitPerUser() != null && flashSale.getLimitPerUser() > 0) {
            if (quantity > flashSale.getLimitPerUser()) {
                throw new BusinessException(400, "超出每人限购数量：最多购买 " + flashSale.getLimitPerUser() + " 件");
            }
            checkUserPurchaseLimit(userId, flashSale, quantity);
        }

        ProductDO product = productMapper.selectById(flashSale.getProductId());
        if (product == null) {
            throw new BusinessException(404, "关联商品不存在");
        }
        if (product.getStock() == null || product.getStock() < quantity) {
            throw new BusinessException(400, "商品库存不足");
        }

        UserAddressDO address = userAddressMapper.selectById(addressId);
        if (address == null || !userId.equals(address.getUserId())) {
            throw new BusinessException(400, "收货地址不存在或不属于当前用户");
        }

        String fullAddress = "";
        if (address.getProvince() != null) {
            fullAddress += address.getProvince();
        }
        if (address.getCity() != null) {
            fullAddress += address.getCity();
        }
        if (address.getDistrict() != null) {
            fullAddress += address.getDistrict();
        }
        if (address.getDetailAddress() != null) {
            fullAddress += address.getDetailAddress();
        }

        BigDecimal flashPrice = flashSale.getFlashPrice() != null ? flashSale.getFlashPrice() : BigDecimal.ZERO;
        BigDecimal totalAmount = flashPrice.multiply(BigDecimal.valueOf(quantity));
        BigDecimal discountAmount = BigDecimal.ZERO;
        if (flashSale.getOriginalPrice() != null) {
            discountAmount = flashSale.getOriginalPrice().subtract(flashPrice).multiply(BigDecimal.valueOf(quantity));
        }
        BigDecimal payAmount = totalAmount.add(OrderConstants.DEFAULT_SHIPPING_FEE);

        // 原子扣减秒杀库存（防止并发超卖）
        int flashRows = flashSaleMapper.deductStock(flashSale.getId(), quantity);
        if (flashRows == 0) {
            throw new BusinessException(400, "秒杀商品库存不足，抢购失败");
        }

        // 原子扣减商品库存（防止并发超卖）
        int productRows = productMapper.deductStock(product.getId(), quantity);
        if (productRows == 0) {
            throw new BusinessException(400, "商品库存不足");
        }

        // Create simplified order
        OrderDO order = new OrderDO();
        order.setOrderNo(OrderNoGenerator.generateFlashSaleOrderNo());
        order.setUserId(userId);
        order.setShopId(product.getShopId());
        order.setTotalAmount(totalAmount);
        order.setShippingFee(OrderConstants.DEFAULT_SHIPPING_FEE);
        order.setDiscountAmount(discountAmount);
        order.setPayAmount(payAmount);
        order.setStatus(OrderConstants.ORDER_STATUS_CREATED);
        order.setReceiverName(address.getReceiverName());
        order.setReceiverPhone(address.getReceiverPhone());
        order.setReceiverAddress(fullAddress);
        order.setRemark("秒杀订单");
        order.setAutoConfirm(0);

        orderMapper.insert(order);

        OrderItemDO orderItem = new OrderItemDO();
        orderItem.setOrderId(order.getId());
        orderItem.setProductId(product.getId());
        orderItem.setProductName(product.getName());
        orderItem.setProductImage(product.getMainImage());
        orderItem.setPrice(flashPrice);
        orderItem.setQuantity(quantity);
        orderItem.setSubtotal(totalAmount);
        orderItemMapper.insert(orderItem);

        return convertToFlashSaleOrderResponse(order, saleId, flashPrice);
    }

    /**
     * 检查用户购买限制（已知限制：读-判断-写非原子操作，并发下可能突破限购）。
     * 库存层面已通过 FlashSaleMapper.deductStock 的 WHERE stock >= #{quantity} 条件保证不会超卖。
     * 限购检查的原子化需依赖分布式锁或数据库唯一索引，后续版本优化。
     */
    private void checkUserPurchaseLimit(Long userId, FlashSaleDO flashSale, Integer quantity) {
        int limitPerUser = flashSale.getLimitPerUser() != null ? flashSale.getLimitPerUser() : 1;

        // 按 saleId 对应的商品 ID 过滤，避免跨活动限购误计
        LambdaQueryWrapper<OrderDO> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.eq(OrderDO::getUserId, userId)
                .likeRight(OrderDO::getOrderNo, OrderConstants.ORDER_PREFIX_FLASH_SALE);
        List<OrderDO> flashSaleOrders = orderMapper.selectList(orderWrapper);

        if (flashSaleOrders.isEmpty()) {
            return;
        }

        List<Long> orderIds = flashSaleOrders.stream()
                .map(OrderDO::getId)
                .collect(Collectors.toList());

        LambdaQueryWrapper<OrderItemDO> itemWrapper = new LambdaQueryWrapper<>();
        itemWrapper.in(OrderItemDO::getOrderId, orderIds)
                .eq(OrderItemDO::getProductId, flashSale.getProductId());
        long purchasedCount = orderItemMapper.selectCount(itemWrapper);

        if (purchasedCount + quantity > limitPerUser) {
            throw new BusinessException(400, "超出限购数量，每人限购 " + limitPerUser + " 件");
        }
    }

    private FlashSaleResponse convertToFlashSaleResponse(FlashSaleDO sale) {
        FlashSaleResponse resp = new FlashSaleResponse();
        resp.setSaleId(sale.getId());
        resp.setProductId(sale.getProductId());
        // 从商品表获取名称和图片（数据库表缺少 product_name/product_image 字段）
        ProductDO product = productMapper.selectById(sale.getProductId());
        if (product != null) {
            resp.setProductName(product.getName());
            resp.setProductImage(product.getMainImage());
        }
        resp.setOriginalPrice(sale.getOriginalPrice());
        resp.setFlashPrice(sale.getFlashPrice());
        resp.setStock(sale.getStock());
        resp.setSoldCount(sale.getSoldCount());
        resp.setStartTime(sale.getStartTime());
        resp.setEndTime(sale.getEndTime());
        resp.setLimitPerUser(sale.getLimitPerUser());
        resp.setStatus(sale.getStatus());
        return resp;
    }

    private FlashSaleDetailResponse convertToFlashSaleDetailResponse(FlashSaleDO sale) {
        FlashSaleDetailResponse resp = new FlashSaleDetailResponse();
        resp.setSaleId(sale.getId());
        resp.setProductId(sale.getProductId());
        ProductDO product = productMapper.selectById(sale.getProductId());
        if (product != null) {
            resp.setProductName(product.getName());
            resp.setProductImage(product.getMainImage());
        }
        resp.setOriginalPrice(sale.getOriginalPrice());
        resp.setFlashPrice(sale.getFlashPrice());
        resp.setStock(sale.getStock());
        resp.setSoldCount(sale.getSoldCount());
        resp.setStartTime(sale.getStartTime());
        resp.setEndTime(sale.getEndTime());
        resp.setLimitPerUser(sale.getLimitPerUser());
        resp.setStatus(sale.getStatus());
        return resp;
    }

    private FlashSaleOrderResponse convertToFlashSaleOrderResponse(OrderDO order, Long saleId, BigDecimal flashPrice) {
        FlashSaleOrderResponse resp = new FlashSaleOrderResponse();
        resp.setOrderId(order.getId());
        resp.setOrderNo(order.getOrderNo());
        resp.setTotalAmount(order.getTotalAmount());
        resp.setShippingFee(order.getShippingFee());
        resp.setDiscountAmount(order.getDiscountAmount());
        resp.setPayAmount(order.getPayAmount());
        resp.setSaleId(saleId);
        resp.setFlashPrice(flashPrice);
        return resp;
    }
}
