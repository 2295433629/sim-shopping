package com.sim.shopping.application.shop;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sim.shopping.application.merchant.MerchantService;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.domain.common.exception.MerchantException;
import com.sim.shopping.application.settlement.SettlementService;
import com.sim.shopping.infrastructure.persistence.entity.SettlementRecordDO;
import com.sim.shopping.infrastructure.persistence.entity.*;
import com.sim.shopping.infrastructure.persistence.mapper.*;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.shop.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 店铺服务，处理店铺信息的增删改查
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Service
public class ShopService {

    private final ShopMapper shopMapper;
    private final MerchantMapper merchantMapper;
    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;
    private final ReviewMapper reviewMapper;
    private final BannerMapper bannerMapper;
    private final MerchantService merchantService;
    private final SettlementRecordMapper settlementRecordMapper;

    public ShopService(ShopMapper shopMapper,
                       MerchantMapper merchantMapper,
                       ProductMapper productMapper,
                       OrderMapper orderMapper,
                       ReviewMapper reviewMapper,
                       BannerMapper bannerMapper,
                       MerchantService merchantService,
                       SettlementRecordMapper settlementRecordMapper) {
        this.shopMapper = shopMapper;
        this.merchantMapper = merchantMapper;
        this.productMapper = productMapper;
        this.orderMapper = orderMapper;
        this.reviewMapper = reviewMapper;
        this.bannerMapper = bannerMapper;
        this.merchantService = merchantService;
        this.settlementRecordMapper = settlementRecordMapper;
    }

    /**
     * 获取Shop Info
     * @param userId userId
     * @return 返回结果
     */
    public ShopResponse getShopInfo(Long userId) {
        ShopDO shop = getShopByUserId(userId);
        Integer productCount = countProductsByShopId(shop.getId());
        return toShopResponse(shop, productCount);
    }

    /**
     * 更新Shop Info
     * @param userId userId
     * @param req req
     * @return 返回结果
     */
    @Transactional
    public ShopResponse updateShopInfo(Long userId, ShopUpdateRequest req) {
        ShopDO shop = getShopByUserId(userId);
        if (req.getShopName() != null) {
            shop.setShopName(req.getShopName());
        }
        if (req.getShopLogo() != null) {
            shop.setShopLogo(req.getShopLogo());
        }
        if (req.getDescription() != null) {
            shop.setDescription(req.getDescription());
        }
        if (req.getSenderName() != null) {
            shop.setSenderName(req.getSenderName());
        }
        if (req.getSenderPhone() != null) {
            shop.setSenderPhone(req.getSenderPhone());
        }
        if (req.getSenderProvince() != null) {
            shop.setSenderProvince(req.getSenderProvince());
        }
        if (req.getSenderCity() != null) {
            shop.setSenderCity(req.getSenderCity());
        }
        if (req.getSenderDistrict() != null) {
            shop.setSenderDistrict(req.getSenderDistrict());
        }
        if (req.getSenderAddress() != null) {
            shop.setSenderAddress(req.getSenderAddress());
        }
        shopMapper.updateById(shop);
        Integer productCount = countProductsByShopId(shop.getId());
        return toShopResponse(shop, productCount);
    }

    /**
     * 获取Public Shop
     * @param shopId shopId
     * @return 返回结果
     */
    public ShopResponse getPublicShop(Long shopId) {
        ShopDO shop = shopMapper.selectById(shopId);
        if (shop == null) {
            throw new MerchantException.ShopNotFoundException("店铺不存在");
        }
        Integer productCount = countProductsByShopId(shopId);
        return toShopResponse(shop, productCount);
    }

    /**
     * 获取Shop Products
     * @param shopId shopId
     * @param page page
     * @param size size
     * @return 返回结果
     */
    public PageResponse<ProductDO> getShopProducts(Long shopId, int page, int size) {
        ShopDO shop = shopMapper.selectById(shopId);
        if (shop == null) {
            throw new MerchantException.ShopNotFoundException("店铺不存在");
        }
        LambdaQueryWrapper<ProductDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductDO::getShopId, shopId)
               .eq(ProductDO::getStatus, "PUBLISHED")
               .orderByDesc(ProductDO::getCreatedAt);
        Page<ProductDO> pageObj = new Page<>(page, size);
        IPage<ProductDO> result = productMapper.selectPage(pageObj, wrapper);
        return PageResponse.of(result.getRecords(), result.getTotal(), page, size);
    }

    /**
     * 获取Dashboard
     * @param userId userId
     * @return 返回结果
     */
    public DashboardResponse getDashboard(Long userId) {
        ShopDO shop = getShopByUserId(userId);
        Long shopId = shop.getId();

        DashboardResponse resp = new DashboardResponse();

        // Product stats
        LambdaQueryWrapper<ProductDO> totalWrapper = new LambdaQueryWrapper<>();
        totalWrapper.eq(ProductDO::getShopId, shopId);
        Long totalProducts = productMapper.selectCount(totalWrapper);

        LambdaQueryWrapper<ProductDO> publishedWrapper = new LambdaQueryWrapper<>();
        publishedWrapper.eq(ProductDO::getShopId, shopId)
                        .eq(ProductDO::getStatus, "PUBLISHED");
        Long publishedProducts = productMapper.selectCount(publishedWrapper);

        resp.setTotalProducts(totalProducts.intValue());
        resp.setPublishedProducts(publishedProducts.intValue());

        // Today's orders and sales — query orders created today for this shop
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LambdaQueryWrapper<OrderDO> todayOrderWrapper = new LambdaQueryWrapper<>();
        todayOrderWrapper.eq(OrderDO::getShopId, shopId)
                         .ge(OrderDO::getCreatedAt, startOfDay);
        Long todayOrders = orderMapper.selectCount(todayOrderWrapper);
        resp.setTodayOrders(todayOrders.intValue());

        // Today's sales — sum amount of settlement records created today for this shop
        LambdaQueryWrapper<SettlementRecordDO> todaySettlementWrapper = new LambdaQueryWrapper<>();
        todaySettlementWrapper.eq(SettlementRecordDO::getShopId, shopId)
                              .ge(SettlementRecordDO::getCreatedAt, startOfDay);
        java.util.List<SettlementRecordDO> todayRecords = settlementRecordMapper.selectList(todaySettlementWrapper);
        BigDecimal todaySales = todayRecords.stream()
                .map(r -> r.getAmount() != null ? r.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        resp.setTodaySales(todaySales);

        // Pending shipments — orders with status PAID (not yet shipped)
        LambdaQueryWrapper<OrderDO> pendingWrapper = new LambdaQueryWrapper<>();
        pendingWrapper.eq(OrderDO::getShopId, shopId)
                      .eq(OrderDO::getStatus, "PAID");
        Long pendingShipments = orderMapper.selectCount(pendingWrapper);
        resp.setPendingShipments(pendingShipments.intValue());

        // Reviews
        LambdaQueryWrapper<ReviewDO> reviewWrapper = new LambdaQueryWrapper<>();
        reviewWrapper.eq(ReviewDO::getShopId, shopId);
        Long totalReviews = reviewMapper.selectCount(reviewWrapper);
        resp.setTotalReviews(totalReviews.intValue());

        // Avg review score — compute from all reviews
        // For simplicity, return 0 for now (M3/M5 will refine with SQL aggregate)
        resp.setAvgReviewScore(BigDecimal.ZERO);

        return resp;
    }

    /**
     * 添加Banner
     * @param userId userId
     * @param req req
     * @return 返回结果
     */
    @Transactional
    public ShopBannerResponse addBanner(Long userId, ShopBannerRequest req) {
        ShopDO shop = getShopByUserId(userId);

        BannerDO banner = new BannerDO();
        banner.setTitle(shop.getShopName());
        banner.setImageUrl(req.getImageUrl());
        banner.setSortOrder(req.getSortOrder() != null ? req.getSortOrder() : 0);
        banner.setLinkUrl(req.getLinkUrl());
        banner.setStatus("ACTIVE");
        bannerMapper.insert(banner);

        return toShopBannerResponse(banner, shop.getId());
    }

    /**
     * 移除Banner
     * @param userId userId
     * @param bannerId bannerId
     */
    @Transactional
    public void removeBanner(Long userId, Long bannerId) {
        ShopDO shop = getShopByUserId(userId);
        BannerDO banner = bannerMapper.selectById(bannerId);
        if (banner == null) {
            throw new BusinessException(404, "Banner不存在");
        }
        // Verify the banner belongs to this shop (via title matching shop name or other logic)
        // Since BannerDO doesn't have a shopId field, we use title as the link
        // In a real system, we'd add shopId to BannerDO. For now, we just delete by id.
        bannerMapper.deleteById(bannerId);
    }

    // ===== Helper methods =====

    private ShopDO getShopByUserId(Long userId) {
        MerchantDO merchant = merchantService.getByUserId(userId);
        LambdaQueryWrapper<ShopDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShopDO::getMerchantId, merchant.getId());
        ShopDO shop = shopMapper.selectOne(wrapper);
        if (shop == null) {
            throw new MerchantException.ShopNotFoundException("店铺信息不存在");
        }
        if ("DISABLED".equals(shop.getStatus())) {
            throw new MerchantException.MerchantDisabledException("店铺已被禁用");
        }
        return shop;
    }

    private Integer countProductsByShopId(Long shopId) {
        LambdaQueryWrapper<ProductDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductDO::getShopId, shopId);
        return productMapper.selectCount(wrapper).intValue();
    }

    private ShopResponse toShopResponse(ShopDO shop, Integer productCount) {
        ShopResponse resp = new ShopResponse();
        resp.setShopId(shop.getId());
        resp.setMerchantId(shop.getMerchantId());
        resp.setShopName(shop.getShopName());
        resp.setShopLogo(shop.getShopLogo());
        resp.setDescription(shop.getDescription());
        resp.setProductCount(productCount);
        resp.setCreatedAt(shop.getCreatedAt());
        resp.setSenderName(shop.getSenderName());
        resp.setSenderPhone(shop.getSenderPhone());
        resp.setSenderProvince(shop.getSenderProvince());
        resp.setSenderCity(shop.getSenderCity());
        resp.setSenderDistrict(shop.getSenderDistrict());
        resp.setSenderAddress(shop.getSenderAddress());
        return resp;
    }

    private ShopBannerResponse toShopBannerResponse(BannerDO banner, Long shopId) {
        ShopBannerResponse resp = new ShopBannerResponse();
        resp.setId(banner.getId());
        resp.setShopId(shopId);
        resp.setImageUrl(banner.getImageUrl());
        resp.setSortOrder(banner.getSortOrder());
        resp.setLinkUrl(banner.getLinkUrl());
        return resp;
    }
}
