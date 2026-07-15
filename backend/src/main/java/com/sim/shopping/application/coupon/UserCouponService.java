package com.sim.shopping.application.coupon;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.infrastructure.persistence.entity.CouponDO;
import com.sim.shopping.infrastructure.persistence.entity.UserCouponDO;
import com.sim.shopping.infrastructure.persistence.mapper.CouponMapper;
import com.sim.shopping.infrastructure.persistence.mapper.UserCouponMapper;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.coupon.UserCouponResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserCouponService {

    private final UserCouponMapper userCouponMapper;
    private final CouponMapper couponMapper;

    private static final String COUPON_STATUS_ACTIVE = "ACTIVE";
    private static final String USER_COUPON_STATUS_CLAIMED = "CLAIMED";
    private static final String USER_COUPON_STATUS_USED = "USED";
    private static final String USER_COUPON_STATUS_EXPIRED = "EXPIRED";

    public UserCouponService(UserCouponMapper userCouponMapper, CouponMapper couponMapper) {
        this.userCouponMapper = userCouponMapper;
        this.couponMapper = couponMapper;
    }

    @Transactional
    public UserCouponResponse claimCoupon(Long userId, Long couponId) {
        if (couponId == null) {
            throw new BusinessException(400, "优惠券ID不能为空");
        }

        // 1. 查询优惠券
        CouponDO coupon = couponMapper.selectById(couponId);
        if (coupon == null) {
            throw new BusinessException(400, "优惠券不存在");
        }

        // 2. 检查状态
        if (!COUPON_STATUS_ACTIVE.equals(coupon.getStatus())) {
            throw new BusinessException(400, "优惠券已失效");
        }

        // 3. 检查有效期
        LocalDateTime now = LocalDateTime.now();
        if (coupon.getValidStartTime() != null && now.isBefore(coupon.getValidStartTime())) {
            throw new BusinessException(400, "优惠券未到领取时间");
        }
        if (coupon.getValidEndTime() != null && now.isAfter(coupon.getValidEndTime())) {
            throw new BusinessException(400, "优惠券已过期");
        }

        // 4. 检查是否已领完
        if (coupon.getTotalQuantity() != null && coupon.getClaimedQuantity() != null
                && coupon.getClaimedQuantity() >= coupon.getTotalQuantity()) {
            throw new BusinessException(400, "优惠券已领完");
        }

        // 5. 检查用户是否已领过
        LambdaQueryWrapper<UserCouponDO> userCouponWrapper = new LambdaQueryWrapper<>();
        userCouponWrapper.eq(UserCouponDO::getUserId, userId)
                .eq(UserCouponDO::getCouponId, coupon.getId());
        Long claimedCount = userCouponMapper.selectCount(userCouponWrapper);
        if (claimedCount != null && claimedCount > 0) {
            throw new BusinessException(400, "您已领取过该优惠券");
        }

        // 6. 原子更新已领取数量
        int rows = couponMapper.update(null,
                new LambdaUpdateWrapper<CouponDO>()
                        .eq(CouponDO::getId, coupon.getId())
                        .apply("total_quantity > claimed_quantity")
                        .setSql("claimed_quantity = claimed_quantity + 1"));
        if (rows == 0) {
            throw new BusinessException(400, "领取优惠券失败，请重试");
        }

        // 7. 插入用户优惠券记录
        UserCouponDO userCoupon = new UserCouponDO();
        userCoupon.setUserId(userId);
        userCoupon.setCouponId(coupon.getId());
        userCoupon.setClaimedAt(now);
        userCoupon.setStatus(USER_COUPON_STATUS_CLAIMED);
        userCouponMapper.insert(userCoupon);

        return convertToResponse(userCoupon, coupon);
    }

    public PageResponse<UserCouponResponse> getMyCoupons(Long userId, int page, int size, String status) {
        Page<UserCouponDO> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<UserCouponDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserCouponDO::getUserId, userId);
        if (StringUtils.hasText(status)) {
            wrapper.eq(UserCouponDO::getStatus, status);
        }
        wrapper.orderByDesc(UserCouponDO::getClaimedAt);

        IPage<UserCouponDO> result = userCouponMapper.selectPage(pageObj, wrapper);
        List<UserCouponResponse> list = result.getRecords().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return PageResponse.of(list, result.getTotal(), page, size);
    }

    public List<UserCouponResponse> getMyAvailableCoupons(Long userId) {
        LambdaQueryWrapper<UserCouponDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserCouponDO::getUserId, userId)
                .eq(UserCouponDO::getStatus, USER_COUPON_STATUS_CLAIMED)
                .orderByDesc(UserCouponDO::getClaimedAt);
        List<UserCouponDO> userCoupons = userCouponMapper.selectList(wrapper);

        LocalDateTime now = LocalDateTime.now();
        return userCoupons.stream()
                .map(this::convertToResponse)
                .filter(resp -> {
                    // 过滤掉已过期的
                    if (resp.getValidEndTime() == null) {
                        return true;
                    }
                    return !now.isAfter(resp.getValidEndTime());
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public UserCouponResponse useCoupon(Long userId, Long userCouponId, String orderNo) {
        if (!StringUtils.hasText(orderNo)) {
            throw new BusinessException(400, "订单号不能为空");
        }

        UserCouponDO userCoupon = userCouponMapper.selectById(userCouponId);
        if (userCoupon == null) {
            throw new BusinessException(400, "用户优惠券不存在");
        }
        if (!userCoupon.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权使用该优惠券");
        }
        if (!USER_COUPON_STATUS_CLAIMED.equals(userCoupon.getStatus())) {
            throw new BusinessException(400, "优惠券状态异常，无法使用");
        }

        CouponDO coupon = couponMapper.selectById(userCoupon.getCouponId());
        if (coupon != null) {
            LocalDateTime now = LocalDateTime.now();
            if (coupon.getValidEndTime() != null && now.isAfter(coupon.getValidEndTime())) {
                throw new BusinessException(400, "优惠券已过期");
            }
        }

        // 更新用户优惠券状态
        userCoupon.setStatus(USER_COUPON_STATUS_USED);
        userCoupon.setUsedAt(LocalDateTime.now());
        userCoupon.setOrderNo(orderNo);
        userCouponMapper.updateById(userCoupon);

        // 原子更新优惠券已使用数量
        couponMapper.update(null,
                new LambdaUpdateWrapper<CouponDO>()
                        .eq(CouponDO::getId, userCoupon.getCouponId())
                        .setSql("used_quantity = used_quantity + 1"));

        return convertToResponse(userCoupon, coupon);
    }

    // ========== Internal helpers ==========

    private UserCouponResponse convertToResponse(UserCouponDO userCoupon) {
        CouponDO coupon = couponMapper.selectById(userCoupon.getCouponId());
        return convertToResponse(userCoupon, coupon);
    }

    private UserCouponResponse convertToResponse(UserCouponDO userCoupon, CouponDO coupon) {
        if (userCoupon == null) {
            return null;
        }
        UserCouponResponse resp = new UserCouponResponse();
        resp.setId(userCoupon.getId());
        resp.setUserId(userCoupon.getUserId());
        resp.setCouponId(userCoupon.getCouponId());
        resp.setClaimedAt(userCoupon.getClaimedAt());
        resp.setUsedAt(userCoupon.getUsedAt());
        resp.setOrderNo(userCoupon.getOrderNo());
        resp.setStatus(userCoupon.getStatus());

        if (coupon != null) {
            resp.setCouponCode(coupon.getCouponCode());
            resp.setCouponName(coupon.getCouponName());
            resp.setCouponType(coupon.getCouponType());
            resp.setDiscountValue(coupon.getDiscountValue());
            resp.setMinSpend(coupon.getMinSpend());
            resp.setValidStartTime(coupon.getValidStartTime());
            resp.setValidEndTime(coupon.getValidEndTime());
            resp.setApplicableScope(coupon.getApplicableScope());
            resp.setApplicableIds(coupon.getApplicableIds());
        }
        return resp;
    }
}
