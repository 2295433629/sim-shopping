package com.sim.shopping.application.coupon;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.infrastructure.persistence.entity.CouponDO;
import com.sim.shopping.infrastructure.persistence.mapper.CouponMapper;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.coupon.CouponResponse;
import com.sim.shopping.interfaces.dto.coupon.CouponStatisticsVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponService {

    private final CouponMapper couponMapper;

    private static final String COUPON_STATUS_ACTIVE = "ACTIVE";
    private static final String COUPON_STATUS_INACTIVE = "INACTIVE";

    public CouponService(CouponMapper couponMapper) {
        this.couponMapper = couponMapper;
    }

    @Transactional
    public CouponResponse createCoupon(CouponDO coupon) {
        // 校验券码唯一性
        LambdaQueryWrapper<CouponDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CouponDO::getCouponCode, coupon.getCouponCode());
        Long count = couponMapper.selectCount(wrapper);
        if (count != null && count > 0) {
            throw new BusinessException(400, "优惠券码已存在");
        }

        coupon.setClaimedQuantity(0);
        coupon.setUsedQuantity(0);
        if (coupon.getStatus() == null) {
            coupon.setStatus(COUPON_STATUS_ACTIVE);
        }
        couponMapper.insert(coupon);
        return convertToResponse(coupon);
    }

    @Transactional
    public CouponResponse updateCoupon(Long id, CouponDO coupon) {
        CouponDO existing = couponMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(400, "优惠券不存在");
        }

        // 如果修改了券码，校验唯一性
        if (StringUtils.hasText(coupon.getCouponCode())
                && !coupon.getCouponCode().equals(existing.getCouponCode())) {
            LambdaQueryWrapper<CouponDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(CouponDO::getCouponCode, coupon.getCouponCode());
            Long count = couponMapper.selectCount(wrapper);
            if (count != null && count > 0) {
                throw new BusinessException(400, "优惠券码已存在");
            }
        }

        coupon.setId(id);
        // 不允许通过更新接口修改已领取/已使用数量
        coupon.setClaimedQuantity(null);
        coupon.setUsedQuantity(null);
        couponMapper.updateById(coupon);
        return convertToResponse(couponMapper.selectById(id));
    }

    @Transactional
    public void deleteCoupon(Long id) {
        CouponDO existing = couponMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(400, "优惠券不存在");
        }
        couponMapper.deleteById(id);
    }

    public CouponResponse getCouponDetail(Long id) {
        CouponDO coupon = couponMapper.selectById(id);
        if (coupon == null) {
            throw new BusinessException(400, "优惠券不存在");
        }
        return convertToResponse(coupon);
    }

    public PageResponse<CouponResponse> getCouponList(int page, int size, String keyword, String status) {
        Page<CouponDO> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<CouponDO> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(CouponDO::getCouponCode, keyword)
                    .or().like(CouponDO::getCouponName, keyword));
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(CouponDO::getStatus, status);
        }
        wrapper.orderByDesc(CouponDO::getCreatedAt);

        IPage<CouponDO> result = couponMapper.selectPage(pageObj, wrapper);
        List<CouponResponse> list = result.getRecords().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return PageResponse.of(list, result.getTotal(), page, size);
    }

    public List<CouponResponse> getPublicAvailableCoupons() {
        LocalDateTime now = LocalDateTime.now();
        LambdaQueryWrapper<CouponDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CouponDO::getStatus, COUPON_STATUS_ACTIVE)
                .le(CouponDO::getValidStartTime, now)
                .ge(CouponDO::getValidEndTime, now)
                .orderByDesc(CouponDO::getCreatedAt);

        List<CouponDO> coupons = couponMapper.selectList(wrapper);
        return coupons.stream()
                .filter(c -> c.getTotalQuantity() == null
                        || c.getClaimedQuantity() == null
                        || c.getClaimedQuantity() < c.getTotalQuantity())
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public CouponStatisticsVO getCouponStatistics() {
        LocalDateTime now = LocalDateTime.now();
        CouponStatisticsVO vo = new CouponStatisticsVO();

        // 总优惠券数量
        vo.setTotalCoupons(couponMapper.selectCount(new LambdaQueryWrapper<>()));

        // 已领取总数
        LambdaQueryWrapper<CouponDO> claimedWrapper = new LambdaQueryWrapper<>();
        claimedWrapper.gt(CouponDO::getClaimedQuantity, 0);
        vo.setTotalClaimed(couponMapper.selectCount(claimedWrapper));

        // 已使用总数
        LambdaQueryWrapper<CouponDO> usedWrapper = new LambdaQueryWrapper<>();
        usedWrapper.gt(CouponDO::getUsedQuantity, 0);
        vo.setTotalUsed(couponMapper.selectCount(usedWrapper));

        // 进行中（有效且状态为ACTIVE）
        LambdaQueryWrapper<CouponDO> activeWrapper = new LambdaQueryWrapper<>();
        activeWrapper.eq(CouponDO::getStatus, COUPON_STATUS_ACTIVE)
                .le(CouponDO::getValidStartTime, now)
                .ge(CouponDO::getValidEndTime, now);
        vo.setActiveCoupons(couponMapper.selectCount(activeWrapper));

        // 已过期
        LambdaQueryWrapper<CouponDO> expiredWrapper = new LambdaQueryWrapper<>();
        expiredWrapper.lt(CouponDO::getValidEndTime, now);
        vo.setExpiredCoupons(couponMapper.selectCount(expiredWrapper));

        return vo;
    }

    // ========== Internal helpers ==========

    CouponResponse convertToResponse(CouponDO coupon) {
        if (coupon == null) {
            return null;
        }
        CouponResponse resp = new CouponResponse();
        resp.setId(coupon.getId());
        resp.setCouponCode(coupon.getCouponCode());
        resp.setCouponName(coupon.getCouponName());
        resp.setCouponType(coupon.getCouponType());
        resp.setDiscountValue(coupon.getDiscountValue());
        resp.setMinSpend(coupon.getMinSpend());
        resp.setTotalQuantity(coupon.getTotalQuantity());
        resp.setClaimedQuantity(coupon.getClaimedQuantity());
        resp.setUsedQuantity(coupon.getUsedQuantity());
        resp.setRemainingQuantity(
                coupon.getTotalQuantity() != null && coupon.getClaimedQuantity() != null
                        ? Math.max(0, coupon.getTotalQuantity() - coupon.getClaimedQuantity())
                        : null);
        resp.setValidStartTime(coupon.getValidStartTime());
        resp.setValidEndTime(coupon.getValidEndTime());
        resp.setApplicableScope(coupon.getApplicableScope());
        resp.setApplicableIds(coupon.getApplicableIds());
        resp.setStatus(coupon.getStatus());
        resp.setCreatedAt(coupon.getCreatedAt());
        resp.setUpdatedAt(coupon.getUpdatedAt());
        return resp;
    }
}
