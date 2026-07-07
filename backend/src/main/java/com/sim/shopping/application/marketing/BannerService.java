package com.sim.shopping.application.marketing;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.infrastructure.persistence.entity.BannerDO;
import com.sim.shopping.infrastructure.persistence.mapper.BannerMapper;
import com.sim.shopping.interfaces.dto.banner.BannerResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BannerService {

    private final BannerMapper bannerMapper;

    private static final String BANNER_STATUS_ACTIVE = "ACTIVE";

    public BannerService(BannerMapper bannerMapper) {
        this.bannerMapper = bannerMapper;
    }

    public List<BannerResponse> getActiveBanners() {
        LocalDateTime now = LocalDateTime.now();
        LambdaQueryWrapper<BannerDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BannerDO::getStatus, BANNER_STATUS_ACTIVE)
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

    @Transactional
    public BannerResponse createBanner(BannerDO banner) {
        bannerMapper.insert(banner);
        return convertToResponse(banner);
    }

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
