package com.sim.shopping.application.marketing;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.infrastructure.common.SystemConstants;
import com.sim.shopping.infrastructure.persistence.entity.BannerDO;
import com.sim.shopping.infrastructure.persistence.mapper.BannerMapper;
import com.sim.shopping.interfaces.dto.banner.BannerResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Banner服务，处理首页轮播图的增删改查和排序
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Service
public class BannerService {

    private final BannerMapper bannerMapper;

    public BannerService(BannerMapper bannerMapper) {
        this.bannerMapper = bannerMapper;
    }

    /**
     * 获取所有Banner列表（管理员用，不限状态和有效期）
     * @return 返回结果
     */
    public List<BannerResponse> getAllBanners() {
        List<BannerDO> banners = bannerMapper.selectList(
            new LambdaQueryWrapper<BannerDO>().orderByAsc(BannerDO::getSortOrder));
        return banners.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 获取Active Banners
     * @return 返回结果
     */
    public List<BannerResponse> getActiveBanners() {
        LocalDateTime now = LocalDateTime.now();
        LambdaQueryWrapper<BannerDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BannerDO::getStatus, SystemConstants.STATUS_ACTIVE)
                .le(BannerDO::getStartTime, now)
                .ge(BannerDO::getEndTime, now)
                .orderByAsc(BannerDO::getSortOrder);

        List<BannerDO> banners = bannerMapper.selectList(wrapper);
        List<BannerResponse> list = new ArrayList<>();
        for (BannerDO banner : banners) {
            list.add(convertToResponse(banner));
        }
        return list;
    }

    /**
     * 创建Banner
     * @param banner banner
     * @return 返回结果
     */
    @Transactional
    public BannerResponse createBanner(BannerDO banner) {
        bannerMapper.insert(banner);
        return convertToResponse(banner);
    }

    /**
     * 更新Banner
     * @param id id
     * @param banner banner
     * @return 返回结果
     */
    @Transactional
    public BannerResponse updateBanner(Long id, BannerDO banner) {
        BannerDO existing = bannerMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(400, "Banner不存在");
        }
        banner.setId(id);
        bannerMapper.updateById(banner);
        return convertToResponse(bannerMapper.selectById(id));
    }

    /**
     * 删除Banner
     * @param id id
     */
    @Transactional
    public void deleteBanner(Long id) {
        BannerDO existing = bannerMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(400, "Banner不存在");
        }
        bannerMapper.deleteById(id);
    }

    private BannerResponse convertToResponse(BannerDO banner) {
        BannerResponse resp = new BannerResponse();
        resp.setId(banner.getId());
        resp.setTitle(banner.getTitle());
        resp.setImageUrl(banner.getImageUrl());
        resp.setLinkUrl(banner.getLinkUrl());
        resp.setSortOrder(banner.getSortOrder());
        resp.setStartTime(banner.getStartTime());
        resp.setEndTime(banner.getEndTime());
        resp.setStatus(banner.getStatus());
        return resp;
    }
}
