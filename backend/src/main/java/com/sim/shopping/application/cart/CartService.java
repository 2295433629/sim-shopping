package com.sim.shopping.application.cart;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.infrastructure.cache.CacheProvider;
import com.sim.shopping.infrastructure.cache.CacheProviderFactory;
import com.sim.shopping.infrastructure.persistence.entity.*;
import com.sim.shopping.infrastructure.persistence.mapper.*;
import com.sim.shopping.interfaces.dto.cart.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final ShoppingCartMapper cartMapper;
    private final CartItemMapper cartItemMapper;
    private final ProductMapper productMapper;
    private final ProductSkuMapper productSkuMapper;
    private final ShopMapper shopMapper;
    private final CacheProvider cacheProvider;

    private static final String CART_CACHE_PREFIX = "cart:user:";
    private static final Duration CART_CACHE_TTL = Duration.ofDays(7);

    public CartService(ShoppingCartMapper cartMapper,
                       CartItemMapper cartItemMapper,
                       ProductMapper productMapper,
                       ProductSkuMapper productSkuMapper,
                       ShopMapper shopMapper,
                       CacheProviderFactory cacheProviderFactory) {
        this.cartMapper = cartMapper;
        this.cartItemMapper = cartItemMapper;
        this.productMapper = productMapper;
        this.productSkuMapper = productSkuMapper;
        this.shopMapper = shopMapper;
        this.cacheProvider = cacheProviderFactory.getCacheProvider();
    }

    public CartResponse getCart(Long userId) {
        ShoppingCartDO cart = getOrCreateCart(userId);
        List<CartItemDO> cartItems = getCartItems(cart.getId());

        CartResponse response = buildCartResponse(cart, cartItems);
        updateCache(userId, response);
        return response;
    }

    @Transactional
    public CartResponse addToCart(Long userId, Long productId, Long skuId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new BusinessException(400, "商品数量必须大于0");
        }

        ProductDO product = productMapper.selectById(productId);
        if (product == null || !"PUBLISHED".equals(product.getStatus())) {
            throw new BusinessException(404, "商品不存在或已下架");
        }

        if (skuId != null && skuId > 0) {
            ProductSkuDO sku = productSkuMapper.selectById(skuId);
            if (sku == null || !productId.equals(sku.getProductId())) {
                throw new BusinessException(404, "商品规格不存在");
            }
        } else {
            skuId = null;
        }

        ShoppingCartDO cart = getOrCreateCart(userId);
        Long shopId = product.getShopId();

        // Check if same product+sku already in cart
        LambdaQueryWrapper<CartItemDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CartItemDO::getCartId, cart.getId());
        wrapper.eq(CartItemDO::getProductId, productId);
        if (skuId != null) {
            wrapper.eq(CartItemDO::getSkuId, skuId);
        } else {
            wrapper.isNull(CartItemDO::getSkuId);
        }
        CartItemDO existingItem = cartItemMapper.selectOne(wrapper);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            if (existingItem.getQuantity() > 99) {
                throw new BusinessException(400, "单个商品数量不能超过99件");
            }
            cartItemMapper.updateById(existingItem);
        } else {
            CartItemDO newItem = new CartItemDO();
            newItem.setCartId(cart.getId());
            newItem.setProductId(productId);
            newItem.setShopId(shopId);
            newItem.setSkuId(skuId);
            newItem.setQuantity(quantity);
            newItem.setSelected(1);
            cartItemMapper.insert(newItem);
        }

        return getCart(userId);
    }

    @Transactional
    public CartResponse updateCartItem(Long userId, Long cartItemId, Integer quantity, Integer selected) {
        ShoppingCartDO cart = getOrCreateCart(userId);
        CartItemDO item = cartItemMapper.selectById(cartItemId);
        if (item == null || !cart.getId().equals(item.getCartId())) {
            throw new BusinessException(404, "购物车商品不存在");
        }

        if (quantity != null) {
            if (quantity <= 0) {
                throw new BusinessException(400, "商品数量必须大于0");
            }
            item.setQuantity(quantity);
        }
        if (selected != null) {
            item.setSelected(selected);
        }
        cartItemMapper.updateById(item);
        return getCart(userId);
    }

    @Transactional
    public CartResponse removeCartItem(Long userId, Long cartItemId) {
        ShoppingCartDO cart = getOrCreateCart(userId);
        CartItemDO item = cartItemMapper.selectById(cartItemId);
        if (item == null || !cart.getId().equals(item.getCartId())) {
            throw new BusinessException(404, "购物车商品不存在");
        }
        cartItemMapper.deleteById(cartItemId);
        return getCart(userId);
    }

    @Transactional
    public void clearCart(Long userId) {
        ShoppingCartDO cart = getOrCreateCart(userId);
        LambdaQueryWrapper<CartItemDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CartItemDO::getCartId, cart.getId());
        cartItemMapper.delete(wrapper);
        evictCache(userId);
    }

    @Transactional
    public CartResponse selectAll(Long userId, Integer selected) {
        ShoppingCartDO cart = getOrCreateCart(userId);
        LambdaUpdateWrapper<CartItemDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(CartItemDO::getCartId, cart.getId());
        wrapper.set(CartItemDO::getSelected, selected);
        cartItemMapper.update(null, wrapper);
        return getCart(userId);
    }

    // ==================== Private Helpers ====================

    private ShoppingCartDO getOrCreateCart(Long userId) {
        LambdaQueryWrapper<ShoppingCartDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCartDO::getUserId, userId);
        ShoppingCartDO cart = cartMapper.selectOne(wrapper);
        if (cart == null) {
            cart = new ShoppingCartDO();
            cart.setUserId(userId);
            cart.setShopId(0L);
            cartMapper.insert(cart);
        }
        return cart;
    }

    private List<CartItemDO> getCartItems(Long cartId) {
        LambdaQueryWrapper<CartItemDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CartItemDO::getCartId, cartId);
        wrapper.orderByDesc(CartItemDO::getCreatedAt);
        return cartItemMapper.selectList(wrapper);
    }

    private CartResponse buildCartResponse(ShoppingCartDO cart, List<CartItemDO> cartItems) {
        CartResponse response = new CartResponse();
        response.setCartId(cart.getId());

        if (cartItems.isEmpty()) {
            response.setShopGroups(Collections.emptyList());
            response.setTotalAmount(BigDecimal.ZERO);
            response.setSelectedAmount(BigDecimal.ZERO);
            return response;
        }

        // Group items by shopId
        Map<Long, List<CartItemDO>> itemsByShop = cartItems.stream()
                .collect(Collectors.groupingBy(CartItemDO::getShopId));

        List<ShopGroupVO> shopGroups = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal selectedAmount = BigDecimal.ZERO;

        for (Map.Entry<Long, List<CartItemDO>> entry : itemsByShop.entrySet()) {
            Long shopId = entry.getKey();
            List<CartItemDO> items = entry.getValue();

            ShopGroupVO shopGroup = new ShopGroupVO();
            shopGroup.setShopId(shopId);

            // Get shop name
            ShopDO shop = shopMapper.selectById(shopId);
            if (shop != null) {
                shopGroup.setShopName(shop.getShopName());
            } else {
                shopGroup.setShopName("未知店铺");
            }

            List<CartItemVO> itemVOs = new ArrayList<>();
            for (CartItemDO item : items) {
                CartItemVO vo = new CartItemVO();
                vo.setCartItemId(item.getId());
                vo.setProductId(item.getProductId());
                vo.setSkuId(item.getSkuId());
                vo.setQuantity(item.getQuantity());
                vo.setSelected(item.getSelected());

                // Get product info
                ProductDO product = productMapper.selectById(item.getProductId());
                if (product != null) {
                    vo.setProductName(product.getName());
                    vo.setProductImage(product.getMainImage());
                    vo.setStock(product.getStock());
                    vo.setAvailable("PUBLISHED".equals(product.getStatus()) && product.getStock() > 0);
                } else {
                    vo.setProductName("商品已删除");
                    vo.setProductImage(null);
                    vo.setStock(0);
                    vo.setAvailable(false);
                }

                // Get SKU info if skuId is present
                if (item.getSkuId() != null) {
                    ProductSkuDO sku = productSkuMapper.selectById(item.getSkuId());
                    if (sku != null) {
                        vo.setSkuName(sku.getSkuName());
                        vo.setPrice(sku.getPrice());
                        vo.setStock(sku.getStock());
                        vo.setAvailable(vo.getAvailable() && sku.getStock() > 0);
                    } else {
                        vo.setSkuName("规格已删除");
                        vo.setPrice(BigDecimal.ZERO);
                        vo.setAvailable(false);
                    }
                } else {
                    if (product != null) {
                        vo.setPrice(product.getPrice());
                    } else {
                        vo.setPrice(BigDecimal.ZERO);
                    }
                    vo.setSkuName(null);
                }

                // Calculate amounts
                if (vo.getPrice() != null && vo.getQuantity() != null) {
                    BigDecimal itemTotal = vo.getPrice().multiply(new BigDecimal(vo.getQuantity()));
                    totalAmount = totalAmount.add(itemTotal);
                    if (item.getSelected() != null && item.getSelected() == 1) {
                        selectedAmount = selectedAmount.add(itemTotal);
                    }
                }

                itemVOs.add(vo);
            }

            shopGroup.setItems(itemVOs);
            shopGroups.add(shopGroup);
        }

        response.setShopGroups(shopGroups);
        response.setTotalAmount(totalAmount);
        response.setSelectedAmount(selectedAmount);
        return response;
    }

    private void updateCache(Long userId, CartResponse cartResponse) {
        try {
            cacheProvider.set(CART_CACHE_PREFIX + userId, cartResponse, CART_CACHE_TTL);
        } catch (Exception e) {
            // Cache failure should not affect business logic
        }
    }

    private void evictCache(Long userId) {
        try {
            cacheProvider.delete(CART_CACHE_PREFIX + userId);
        } catch (Exception e) {
            // Cache failure should not affect business logic
        }
    }
}
