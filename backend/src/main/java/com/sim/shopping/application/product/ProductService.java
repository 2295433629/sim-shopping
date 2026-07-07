package com.sim.shopping.application.product;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.domain.common.exception.ProductException;
import com.sim.shopping.infrastructure.persistence.entity.*;
import com.sim.shopping.infrastructure.persistence.mapper.*;
import com.sim.shopping.infrastructure.security.SecurityUtils;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.product.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductMapper productMapper;
    private final ProductImageMapper productImageMapper;
    private final ProductSkuMapper productSkuMapper;
    private final CategoryMapper categoryMapper;
    private final MerchantMapper merchantMapper;
    private final ShopMapper shopMapper;
    private final BrowseHistoryMapper browseHistoryMapper;
    private final SearchHistoryMapper searchHistoryMapper;
    private final ObjectMapper objectMapper;

    public ProductService(ProductMapper productMapper,
                          ProductImageMapper productImageMapper,
                          ProductSkuMapper productSkuMapper,
                          CategoryMapper categoryMapper,
                          MerchantMapper merchantMapper,
                          ShopMapper shopMapper,
                          BrowseHistoryMapper browseHistoryMapper,
                          SearchHistoryMapper searchHistoryMapper,
                          ObjectMapper objectMapper) {
        this.productMapper = productMapper;
        this.productImageMapper = productImageMapper;
        this.productSkuMapper = productSkuMapper;
        this.categoryMapper = categoryMapper;
        this.merchantMapper = merchantMapper;
        this.shopMapper = shopMapper;
        this.browseHistoryMapper = browseHistoryMapper;
        this.searchHistoryMapper = searchHistoryMapper;
        this.objectMapper = objectMapper;
    }

    // ==================== Merchant Operations ====================

    @Transactional
    public Long createProduct(Long merchantId, ProductCreateRequest req) {
        Long shopId = getShopIdByMerchant(merchantId);

        ProductDO product = new ProductDO();
        product.setShopId(shopId);
        product.setCategoryId(req.getCategoryId());
        product.setBrandId(req.getBrandId());
        product.setName(req.getName());
        product.setSubtitle(req.getSubtitle());
        product.setDescription(req.getDescription());
        product.setMainImage(req.getMainImage());
        product.setPrice(req.getPrice());
        product.setOriginalPrice(req.getOriginalPrice());
        product.setStock(req.getStock() != null ? req.getStock() : 0);
        product.setSales(0);
        product.setViewCount(0);
        product.setReviewCount(0);
        product.setAvgReviewScore(BigDecimal.ZERO);
        product.setFavoriteCount(0);
        product.setIsNew(1);
        product.setIsRecommended(0);
        product.setStatus(req.isPublish() ? "PUBLISHED" : "DRAFT");
        if (req.isPublish()) {
            product.setPublishTime(LocalDateTime.now());
        }
        productMapper.insert(product);

        // save images
        if (req.getImages() != null && !req.getImages().isEmpty()) {
            int sortOrder = 0;
            for (String url : req.getImages()) {
                ProductImageDO img = new ProductImageDO();
                img.setProductId(product.getId());
                img.setImageUrl(url);
                img.setSortOrder(sortOrder++);
                img.setImageType(0);
                productImageMapper.insert(img);
            }
        }

        // save skus
        if (req.getSkus() != null && !req.getSkus().isEmpty()) {
            for (SkuRequest skuReq : req.getSkus()) {
                ProductSkuDO sku = new ProductSkuDO();
                sku.setProductId(product.getId());
                sku.setSkuName(skuReq.getSkuName());
                sku.setPrice(skuReq.getPrice());
                sku.setStock(skuReq.getStock());
                sku.setSkuCode("SKU-" + product.getId() + "-" + System.currentTimeMillis());
                try {
                    sku.setAttributes(skuReq.getAttributes() != null
                            ? objectMapper.writeValueAsString(skuReq.getAttributes()) : "{}");
                } catch (Exception e) {
                    sku.setAttributes("{}");
                }
                productSkuMapper.insert(sku);
            }
        }

        return product.getId();
    }

    @Transactional
    public void updateProduct(Long merchantId, Long productId, ProductUpdateRequest req) {
        Long shopId = getShopIdByMerchant(merchantId);
        ProductDO product = getProductAndCheckOwnership(productId, shopId);

        if (req.getName() != null) product.setName(req.getName());
        if (req.getSubtitle() != null) product.setSubtitle(req.getSubtitle());
        if (req.getDescription() != null) product.setDescription(req.getDescription());
        if (req.getCategoryId() != null) product.setCategoryId(req.getCategoryId());
        if (req.getBrandId() != null) product.setBrandId(req.getBrandId());
        if (req.getMainImage() != null) product.setMainImage(req.getMainImage());
        if (req.getPrice() != null) product.setPrice(req.getPrice());
        if (req.getOriginalPrice() != null) product.setOriginalPrice(req.getOriginalPrice());
        if (req.getStock() != null) product.setStock(req.getStock());
        productMapper.updateById(product);

        // update images: delete old, insert new
        if (req.getImages() != null) {
            LambdaQueryWrapper<ProductImageDO> imgWrapper = new LambdaQueryWrapper<>();
            imgWrapper.eq(ProductImageDO::getProductId, productId);
            productImageMapper.delete(imgWrapper);

            int sortOrder = 0;
            for (String url : req.getImages()) {
                ProductImageDO img = new ProductImageDO();
                img.setProductId(productId);
                img.setImageUrl(url);
                img.setSortOrder(sortOrder++);
                img.setImageType(0);
                productImageMapper.insert(img);
            }
        }

        // update skus: delete old, insert new
        if (req.getSkus() != null) {
            LambdaQueryWrapper<ProductSkuDO> skuWrapper = new LambdaQueryWrapper<>();
            skuWrapper.eq(ProductSkuDO::getProductId, productId);
            productSkuMapper.delete(skuWrapper);

            for (SkuRequest skuReq : req.getSkus()) {
                ProductSkuDO sku = new ProductSkuDO();
                sku.setProductId(productId);
                sku.setSkuName(skuReq.getSkuName());
                sku.setPrice(skuReq.getPrice());
                sku.setStock(skuReq.getStock());
                sku.setSkuCode("SKU-" + productId + "-" + System.currentTimeMillis());
                try {
                    sku.setAttributes(skuReq.getAttributes() != null
                            ? objectMapper.writeValueAsString(skuReq.getAttributes()) : "{}");
                } catch (Exception e) {
                    sku.setAttributes("{}");
                }
                productSkuMapper.insert(sku);
            }
        }
    }

    @Transactional
    public void publishProduct(Long merchantId, Long productId) {
        Long shopId = getShopIdByMerchant(merchantId);
        ProductDO product = getProductAndCheckOwnership(productId, shopId);
        if (!("DRAFT".equals(product.getStatus()) || "OFFLINE".equals(product.getStatus()))) {
            throw new BusinessException(400, "只有草稿或已下架状态的商品才能上架");
        }
        product.setStatus("PUBLISHED");
        product.setPublishTime(LocalDateTime.now());
        productMapper.updateById(product);
    }

    @Transactional
    public void offlineProduct(Long merchantId, Long productId) {
        Long shopId = getShopIdByMerchant(merchantId);
        ProductDO product = getProductAndCheckOwnership(productId, shopId);
        if (!"PUBLISHED".equals(product.getStatus())) {
            throw new BusinessException(400, "只有已上架的商品才能下架");
        }
        product.setStatus("OFFLINE");
        productMapper.updateById(product);
    }

    @Transactional
    public void deleteProduct(Long merchantId, Long productId) {
        Long shopId = getShopIdByMerchant(merchantId);
        ProductDO product = getProductAndCheckOwnership(productId, shopId);
        if (!"DRAFT".equals(product.getStatus())) {
            throw new BusinessException(400, "只有草稿状态的商品才能删除");
        }
        // delete images
        LambdaQueryWrapper<ProductImageDO> imgWrapper = new LambdaQueryWrapper<>();
        imgWrapper.eq(ProductImageDO::getProductId, productId);
        productImageMapper.delete(imgWrapper);
        // delete skus
        LambdaQueryWrapper<ProductSkuDO> skuWrapper = new LambdaQueryWrapper<>();
        skuWrapper.eq(ProductSkuDO::getProductId, productId);
        productSkuMapper.delete(skuWrapper);
        // delete product
        productMapper.deleteById(productId);
    }

    public PageResponse<ProductDetailVO> getMerchantProducts(Long merchantId, int page, int size, String status, String keyword) {
        Long shopId = getShopIdByMerchant(merchantId);
        Page<ProductDO> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<ProductDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductDO::getShopId, shopId);
        if (status != null && !status.isEmpty()) {
            wrapper.eq(ProductDO::getStatus, status);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(ProductDO::getName, keyword);
        }
        wrapper.orderByDesc(ProductDO::getCreatedAt);
        IPage<ProductDO> productPage = productMapper.selectPage(pageParam, wrapper);

        List<ProductDetailVO> list = productPage.getRecords().stream()
                .map(this::toDetailVO)
                .collect(Collectors.toList());

        return PageResponse.of(list, productPage.getTotal(), page, size);
    }

    public ProductDetailVO getMerchantProductDetail(Long merchantId, Long productId) {
        Long shopId = getShopIdByMerchant(merchantId);
        ProductDO product = getProductAndCheckOwnership(productId, shopId);
        return toDetailVO(product);
    }

    // ==================== Public Operations ====================

    public PageResponse<ProductCardVO> getPublicProducts(int page, int size, Long categoryId, String keyword,
                                                          BigDecimal minPrice, BigDecimal maxPrice, String sort) {
        Page<ProductDO> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<ProductDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductDO::getStatus, "PUBLISHED");
        if (categoryId != null) {
            wrapper.eq(ProductDO::getCategoryId, categoryId);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(ProductDO::getName, keyword);
        }
        if (minPrice != null) {
            wrapper.ge(ProductDO::getPrice, minPrice);
        }
        if (maxPrice != null) {
            wrapper.le(ProductDO::getPrice, maxPrice);
        }
        applySort(wrapper, sort);
        IPage<ProductDO> productPage = productMapper.selectPage(pageParam, wrapper);

        List<ProductCardVO> list = productPage.getRecords().stream()
                .map(this::toCardVO)
                .collect(Collectors.toList());

        return PageResponse.of(list, productPage.getTotal(), page, size);
    }

    @Transactional
    public ProductDetailVO getPublicProductDetail(Long productId) {
        ProductDO product = productMapper.selectById(productId);
        if (product == null || !"PUBLISHED".equals(product.getStatus())) {
            throw new ProductException.ProductNotFoundException("商品不存在或已下架");
        }

        // viewCount + 1
        LambdaUpdateWrapper<ProductDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ProductDO::getId, productId)
                     .setSql("view_count = view_count + 1");
        productMapper.update(null, updateWrapper);

        // record browse history
        try {
            Long userId = SecurityUtils.getCurrentUserId();
            BrowseHistoryDO history = new BrowseHistoryDO();
            history.setUserId(userId);
            history.setProductId(productId);
            history.setViewedAt(LocalDateTime.now());
            browseHistoryMapper.insert(history);
        } catch (Exception e) {
            // anonymous user, skip history
        }

        // re-fetch to get updated viewCount
        product = productMapper.selectById(productId);
        return toDetailVO(product);
    }

    @Transactional
    public PageResponse<ProductCardVO> searchProducts(String keyword, int page, int size, Long categoryId,
                                                       BigDecimal minPrice, BigDecimal maxPrice, String sort) {
        // record search history
        try {
            Long userId = SecurityUtils.getCurrentUserId();
            SearchHistoryDO sh = new SearchHistoryDO();
            sh.setUserId(userId);
            sh.setKeyword(keyword);
            sh.setSearchedAt(LocalDateTime.now());
            searchHistoryMapper.insert(sh);
        } catch (Exception e) {
            // anonymous user, skip
        }

        Page<ProductDO> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<ProductDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductDO::getStatus, "PUBLISHED");
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(ProductDO::getName, keyword)
                    .or().like(ProductDO::getSubtitle, keyword)
                    .or().like(ProductDO::getDescription, keyword));
        }
        if (categoryId != null) {
            wrapper.eq(ProductDO::getCategoryId, categoryId);
        }
        if (minPrice != null) {
            wrapper.ge(ProductDO::getPrice, minPrice);
        }
        if (maxPrice != null) {
            wrapper.le(ProductDO::getPrice, maxPrice);
        }
        applySort(wrapper, sort);
        IPage<ProductDO> productPage = productMapper.selectPage(pageParam, wrapper);

        List<ProductCardVO> list = productPage.getRecords().stream()
                .map(this::toCardVO)
                .collect(Collectors.toList());

        return PageResponse.of(list, productPage.getTotal(), page, size);
    }

    public List<ProductCardVO> getRecommendProducts(int size) {
        LambdaQueryWrapper<ProductDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductDO::getStatus, "PUBLISHED")
               .orderByDesc(ProductDO::getSales)
               .last("LIMIT " + size);
        List<ProductDO> products = productMapper.selectList(wrapper);
        return products.stream().map(this::toCardVO).collect(Collectors.toList());
    }

    public List<ProductCardVO> getHotProducts(int size) {
        LambdaQueryWrapper<ProductDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductDO::getStatus, "PUBLISHED")
               .orderByDesc(ProductDO::getViewCount)
               .last("LIMIT " + size);
        List<ProductDO> products = productMapper.selectList(wrapper);
        return products.stream().map(this::toCardVO).collect(Collectors.toList());
    }

    public List<ProductCardVO> getNewProducts(int size) {
        LambdaQueryWrapper<ProductDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductDO::getStatus, "PUBLISHED")
               .orderByDesc(ProductDO::getCreatedAt)
               .last("LIMIT " + size);
        List<ProductDO> products = productMapper.selectList(wrapper);
        return products.stream().map(this::toCardVO).collect(Collectors.toList());
    }

    public PageResponse<ProductCardVO> getProductsByShop(Long shopId, int page, int size, String sort) {
        Page<ProductDO> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<ProductDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductDO::getShopId, shopId)
               .eq(ProductDO::getStatus, "PUBLISHED");
        applySort(wrapper, sort);
        IPage<ProductDO> productPage = productMapper.selectPage(pageParam, wrapper);

        List<ProductCardVO> list = productPage.getRecords().stream()
                .map(this::toCardVO)
                .collect(Collectors.toList());

        return PageResponse.of(list, productPage.getTotal(), page, size);
    }

    // ==================== Admin Operations ====================

    public PageResponse<ProductDetailVO> getAdminProducts(int page, int size, String status, Long shopId, String keyword) {
        Page<ProductDO> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<ProductDO> wrapper = new LambdaQueryWrapper<>();
        if (status != null && !status.isEmpty()) {
            wrapper.eq(ProductDO::getStatus, status);
        }
        if (shopId != null) {
            wrapper.eq(ProductDO::getShopId, shopId);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(ProductDO::getName, keyword);
        }
        wrapper.orderByDesc(ProductDO::getCreatedAt);
        IPage<ProductDO> productPage = productMapper.selectPage(pageParam, wrapper);

        List<ProductDetailVO> list = productPage.getRecords().stream()
                .map(this::toDetailVO)
                .collect(Collectors.toList());

        return PageResponse.of(list, productPage.getTotal(), page, size);
    }

    public ProductDetailVO getAdminProductDetail(Long productId) {
        ProductDO product = productMapper.selectById(productId);
        if (product == null) {
            throw new ProductException.ProductNotFoundException("商品不存在");
        }
        return toDetailVO(product);
    }

    @Transactional
    public void forceOffline(Long productId) {
        ProductDO product = productMapper.selectById(productId);
        if (product == null) {
            throw new ProductException.ProductNotFoundException("商品不存在");
        }
        if (!"PUBLISHED".equals(product.getStatus())) {
            throw new BusinessException(400, "只有已上架的商品才能强制下架");
        }
        product.setStatus("OFFLINE");
        productMapper.updateById(product);
    }

    // ==================== Helpers ====================

    private Long getShopIdByMerchant(Long merchantId) {
        LambdaQueryWrapper<ShopDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShopDO::getMerchantId, merchantId);
        ShopDO shop = shopMapper.selectOne(wrapper);
        if (shop == null) {
            throw new BusinessException(403, "商家店铺不存在");
        }
        return shop.getId();
    }

    private ProductDO getProductAndCheckOwnership(Long productId, Long shopId) {
        ProductDO product = productMapper.selectById(productId);
        if (product == null) {
            throw new ProductException.ProductNotFoundException("商品不存在");
        }
        if (!product.getShopId().equals(shopId)) {
            throw new BusinessException(403, "无权操作此商品");
        }
        return product;
    }

    private void applySort(LambdaQueryWrapper<ProductDO> wrapper, String sort) {
        if (sort == null || sort.isEmpty()) {
            wrapper.orderByDesc(ProductDO::getCreatedAt);
            return;
        }
        switch (sort) {
            case "price_asc":
                wrapper.orderByAsc(ProductDO::getPrice);
                break;
            case "price_desc":
                wrapper.orderByDesc(ProductDO::getPrice);
                break;
            case "sales_desc":
                wrapper.orderByDesc(ProductDO::getSales);
                break;
            case "created_desc":
                wrapper.orderByDesc(ProductDO::getCreatedAt);
                break;
            default:
                wrapper.orderByDesc(ProductDO::getCreatedAt);
        }
    }

    private ProductCardVO toCardVO(ProductDO p) {
        ProductCardVO vo = new ProductCardVO();
        vo.setProductId(p.getId());
        vo.setName(p.getName());
        vo.setSubtitle(p.getSubtitle());
        vo.setMainImage(p.getMainImage());
        vo.setPrice(p.getPrice());
        vo.setOriginalPrice(p.getOriginalPrice());
        vo.setSales(p.getSales());
        vo.setShopId(p.getShopId());
        // fetch shop name
        if (p.getShopId() != null) {
            ShopDO shop = shopMapper.selectById(p.getShopId());
            if (shop != null) {
                vo.setShopName(shop.getShopName());
            }
        }
        return vo;
    }

    private ProductDetailVO toDetailVO(ProductDO p) {
        ProductDetailVO vo = new ProductDetailVO();
        vo.setProductId(p.getId());
        vo.setName(p.getName());
        vo.setSubtitle(p.getSubtitle());
        vo.setDescription(p.getDescription());
        vo.setMainImage(p.getMainImage());
        vo.setPrice(p.getPrice());
        vo.setOriginalPrice(p.getOriginalPrice());
        vo.setStock(p.getStock());
        vo.setSales(p.getSales());
        vo.setViewCount(p.getViewCount());
        vo.setCategoryId(p.getCategoryId());
        vo.setShopId(p.getShopId());

        // fetch images
        LambdaQueryWrapper<ProductImageDO> imgWrapper = new LambdaQueryWrapper<>();
        imgWrapper.eq(ProductImageDO::getProductId, p.getId())
                  .orderByAsc(ProductImageDO::getSortOrder);
        List<ProductImageDO> images = productImageMapper.selectList(imgWrapper);
        vo.setImages(images.stream().map(ProductImageDO::getImageUrl).collect(Collectors.toList()));

        // fetch skus
        LambdaQueryWrapper<ProductSkuDO> skuWrapper = new LambdaQueryWrapper<>();
        skuWrapper.eq(ProductSkuDO::getProductId, p.getId());
        List<ProductSkuDO> skus = productSkuMapper.selectList(skuWrapper);
        List<SkuVO> skuVOs = skus.stream().map(s -> {
            SkuVO sv = new SkuVO();
            sv.setSkuId(s.getId());
            sv.setSkuName(s.getSkuName());
            sv.setPrice(s.getPrice());
            sv.setStock(s.getStock());
            return sv;
        }).collect(Collectors.toList());
        vo.setSkus(skuVOs);

        // category name
        if (p.getCategoryId() != null) {
            CategoryDO cat = categoryMapper.selectById(p.getCategoryId());
            if (cat != null) {
                vo.setCategoryName(cat.getName());
            }
        }

        // shop name
        if (p.getShopId() != null) {
            ShopDO shop = shopMapper.selectById(p.getShopId());
            if (shop != null) {
                vo.setShopName(shop.getShopName());
            }
        }

        // review summary
        ReviewSummaryVO rs = new ReviewSummaryVO();
        rs.setAvgRating(p.getAvgReviewScore() != null ? p.getAvgReviewScore() : BigDecimal.ZERO);
        rs.setReviewCount(p.getReviewCount() != null ? p.getReviewCount() : 0);
        rs.setGoodRate(BigDecimal.ZERO);
        vo.setReviewSummary(rs);

        return vo;
    }
}
