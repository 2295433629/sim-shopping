package com.sim.shopping.application.product;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.infrastructure.persistence.entity.FavoriteDO;
import com.sim.shopping.infrastructure.persistence.entity.ProductDO;
import com.sim.shopping.infrastructure.persistence.mapper.FavoriteMapper;
import com.sim.shopping.infrastructure.persistence.mapper.ProductMapper;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.product.ProductCardVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 收藏服务，处理用户商品收藏的添加、取消和查询
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Service
public class FavoriteService {

    private final FavoriteMapper favoriteMapper;
    private final ProductMapper productMapper;

    public FavoriteService(FavoriteMapper favoriteMapper, ProductMapper productMapper) {
        this.favoriteMapper = favoriteMapper;
        this.productMapper = productMapper;
    }

    /**
     * 收藏商品
     * @param userId userId
     * @param productId productId
     */
    @Transactional
    public void addFavorite(Long userId, Long productId) {
        ProductDO product = productMapper.selectById(productId);
        if (product == null) {
            throw new BusinessException(404, "商品不存在");
        }
        LambdaQueryWrapper<FavoriteDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FavoriteDO::getUserId, userId)
               .eq(FavoriteDO::getProductId, productId);
        Long count = favoriteMapper.selectCount(wrapper);
        if (count > 0) {
            return;
        }
        FavoriteDO fav = new FavoriteDO();
        fav.setUserId(userId);
        fav.setProductId(productId);
        favoriteMapper.insert(fav);
    }

    /**
     * 取消收藏
     * @param userId userId
     * @param productId productId
     */
    @Transactional
    public void removeFavorite(Long userId, Long productId) {
        LambdaQueryWrapper<FavoriteDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FavoriteDO::getUserId, userId)
               .eq(FavoriteDO::getProductId, productId);
        favoriteMapper.delete(wrapper);
    }

    /**
     * 获取Favorite List
     * @param userId userId
     * @param page page
     * @param size size
     * @return 返回结果
     */
    public PageResponse<ProductCardVO> getFavoriteList(Long userId, int page, int size) {
        Page<FavoriteDO> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<FavoriteDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FavoriteDO::getUserId, userId)
               .orderByDesc(FavoriteDO::getCreatedAt);
        IPage<FavoriteDO> favPage = favoriteMapper.selectPage(pageParam, wrapper);

        List<ProductCardVO> list = favPage.getRecords().stream()
                .map(fav -> {
                    ProductDO p = productMapper.selectById(fav.getProductId());
                    if (p == null) {
                        return null;
                    }
                    return toCardVO(p);
                })
                .filter(vo -> vo != null)
                .collect(Collectors.toList());

        return PageResponse.of(list, favPage.getTotal(), page, size);
    }

    /**
     * is Favorite
     * @param userId userId
     * @param productId productId
     * @return 返回结果
     */
    public boolean isFavorite(Long userId, Long productId) {
        LambdaQueryWrapper<FavoriteDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FavoriteDO::getUserId, userId)
               .eq(FavoriteDO::getProductId, productId);
        return favoriteMapper.selectCount(wrapper) > 0;
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
        return vo;
    }
}
