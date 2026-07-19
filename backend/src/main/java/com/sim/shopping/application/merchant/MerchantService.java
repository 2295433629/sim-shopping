package com.sim.shopping.application.merchant;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.domain.common.exception.MerchantException;
import com.sim.shopping.infrastructure.persistence.entity.MerchantDO;
import com.sim.shopping.infrastructure.persistence.entity.ShopDO;
import com.sim.shopping.infrastructure.persistence.mapper.MerchantMapper;
import com.sim.shopping.infrastructure.persistence.mapper.ShopMapper;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.merchant.MerchantApplyRequest;
import com.sim.shopping.interfaces.dto.merchant.MerchantInfoResponse;
import com.sim.shopping.interfaces.dto.merchant.MerchantListResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Merchant服务，处理相关业务逻辑
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Service
public class MerchantService {

    private final MerchantMapper merchantMapper;
    private final ShopMapper shopMapper;

    public MerchantService(MerchantMapper merchantMapper, ShopMapper shopMapper) {
        this.merchantMapper = merchantMapper;
        this.shopMapper = shopMapper;
    }

    /**
     * apply
     * @param userId userId
     * @param req req
     * @return 返回结果
     */
    @Transactional
    public MerchantInfoResponse apply(Long userId, MerchantApplyRequest req) {
        // Check if user already has a merchant record
        LambdaQueryWrapper<MerchantDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MerchantDO::getUserId, userId);
        MerchantDO existing = merchantMapper.selectOne(wrapper);
        if (existing != null) {
            if ("ACTIVE".equals(existing.getStatus())) {
                throw new MerchantException.MerchantAlreadyExistsException("您已是商家，无需重复申请");
            }
            if ("PENDING".equals(existing.getStatus())) {
                throw new MerchantException.MerchantAlreadyExistsException("您的入驻申请正在审核中");
            }
            // REJECTED or DISABLED — allow re-apply by updating existing record
            existing.setMerchantName(req.getMerchantName());
            existing.setContactPhone(req.getContactPhone());
            existing.setContactEmail(req.getContactEmail());
            existing.setBusinessLicense(req.getBusinessLicense());
            existing.setStatus("PENDING");
            merchantMapper.updateById(existing);
            return toMerchantInfoResponse(existing, null);
        }

        MerchantDO merchant = new MerchantDO();
        merchant.setUserId(userId);
        merchant.setMerchantName(req.getMerchantName());
        merchant.setContactPhone(req.getContactPhone());
        merchant.setContactEmail(req.getContactEmail());
        merchant.setBusinessLicense(req.getBusinessLicense());
        merchant.setStatus("PENDING");
        merchantMapper.insert(merchant);
        return toMerchantInfoResponse(merchant, null);
    }

    /**
     * 获取Merchant Info
     * @param userId userId
     * @return 返回结果
     */
    public MerchantInfoResponse getMerchantInfo(Long userId) {
        MerchantDO merchant = getByUserId(userId);
        ShopDO shop = getShopByMerchantId(merchant.getId());
        return toMerchantInfoResponse(merchant, shop);
    }

    /**
     * 更新Merchant Info
     * @param userId userId
     * @param req req
     * @return 返回结果
     */
    @Transactional
    public MerchantInfoResponse updateMerchantInfo(Long userId, MerchantApplyRequest req) {
        MerchantDO merchant = getByUserId(userId);
        merchant.setMerchantName(req.getMerchantName());
        merchant.setContactPhone(req.getContactPhone());
        merchant.setContactEmail(req.getContactEmail());
        merchant.setBusinessLicense(req.getBusinessLicense());
        merchantMapper.updateById(merchant);

        ShopDO shop = getShopByMerchantId(merchant.getId());
        return toMerchantInfoResponse(merchant, shop);
    }

    /**
     * 获取Pending Merchants
     * @param page page
     * @param size size
     * @return 返回结果
     */
    public PageResponse<MerchantListResponse> getPendingMerchants(int page, int size) {
        LambdaQueryWrapper<MerchantDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MerchantDO::getStatus, "PENDING")
               .orderByDesc(MerchantDO::getCreatedAt);
        return queryMerchantPage(page, size, wrapper);
    }

    /**
     * 获取Merchant List
     * @param page page
     * @param size size
     * @param status status
     * @param keyword keyword
     * @return 返回结果
     */
    public PageResponse<MerchantListResponse> getMerchantList(int page, int size, String status, String keyword) {
        LambdaQueryWrapper<MerchantDO> wrapper = new LambdaQueryWrapper<>();
        if (status != null && !status.isEmpty()) {
            wrapper.eq(MerchantDO::getStatus, status);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(MerchantDO::getMerchantName, keyword)
                    .or().like(MerchantDO::getContactPhone, keyword));
        }
        wrapper.orderByDesc(MerchantDO::getCreatedAt);
        return queryMerchantPage(page, size, wrapper);
    }

    /**
     * 获取Merchant Detail
     * @param merchantId merchantId
     * @return 返回结果
     */
    public MerchantInfoResponse getMerchantDetail(Long merchantId) {
        MerchantDO merchant = merchantMapper.selectById(merchantId);
        if (merchant == null) {
            throw new MerchantException.MerchantNotFoundException("商家不存在");
        }
        ShopDO shop = getShopByMerchantId(merchant.getId());
        return toMerchantInfoResponse(merchant, shop);
    }

    /**
     * approve Merchant
     * @param merchantId merchantId
     * @param adminId adminId
     */
    @Transactional
    public void approveMerchant(Long merchantId, Long adminId) {
        MerchantDO merchant = merchantMapper.selectById(merchantId);
        if (merchant == null) {
            throw new MerchantException.MerchantNotFoundException("商家不存在");
        }
        if (!"PENDING".equals(merchant.getStatus())) {
            throw new BusinessException(400, "该商家不是待审核状态");
        }
        merchant.setStatus("ACTIVE");
        merchant.setApprovedBy(adminId);
        merchant.setApprovedAt(LocalDateTime.now());
        merchantMapper.updateById(merchant);

        // Auto-create ShopDO
        ShopDO shop = new ShopDO();
        shop.setMerchantId(merchant.getId());
        shop.setShopName(merchant.getMerchantName());
        shop.setStatus("ACTIVE");
        shopMapper.insert(shop);
    }

    /**
     * reject Merchant
     * @param merchantId merchantId
     * @param adminId adminId
     * @param reason reason
     */
    @Transactional
    public void rejectMerchant(Long merchantId, Long adminId, String reason) {
        MerchantDO merchant = merchantMapper.selectById(merchantId);
        if (merchant == null) {
            throw new MerchantException.MerchantNotFoundException("商家不存在");
        }
        if (!"PENDING".equals(merchant.getStatus())) {
            throw new BusinessException(400, "该商家不是待审核状态");
        }
        merchant.setStatus("REJECTED");
        merchant.setApprovedBy(adminId);
        merchant.setApprovedAt(LocalDateTime.now());
        merchantMapper.updateById(merchant);
    }

    /**
     * 禁用Merchant
     * @param merchantId merchantId
     */
    @Transactional
    public void disableMerchant(Long merchantId) {
        MerchantDO merchant = merchantMapper.selectById(merchantId);
        if (merchant == null) {
            throw new MerchantException.MerchantNotFoundException("商家不存在");
        }
        if (!"ACTIVE".equals(merchant.getStatus())) {
            throw new BusinessException(400, "该商家不是启用状态");
        }
        merchant.setStatus("DISABLED");
        merchantMapper.updateById(merchant);

        // Also disable the shop
        LambdaQueryWrapper<ShopDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShopDO::getMerchantId, merchantId);
        ShopDO shop = shopMapper.selectOne(wrapper);
        if (shop != null) {
            shop.setStatus("DISABLED");
            shopMapper.updateById(shop);
        }
    }

    /**
     * 启用Merchant
     * @param merchantId merchantId
     */
    @Transactional
    public void enableMerchant(Long merchantId) {
        MerchantDO merchant = merchantMapper.selectById(merchantId);
        if (merchant == null) {
            throw new MerchantException.MerchantNotFoundException("商家不存在");
        }
        if (!"DISABLED".equals(merchant.getStatus())) {
            throw new BusinessException(400, "该商家不是禁用状态");
        }
        merchant.setStatus("ACTIVE");
        merchantMapper.updateById(merchant);

        // Also enable the shop
        LambdaQueryWrapper<ShopDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShopDO::getMerchantId, merchantId);
        ShopDO shop = shopMapper.selectOne(wrapper);
        if (shop != null) {
            shop.setStatus("ACTIVE");
            shopMapper.updateById(shop);
        }
    }

    /**
     * 根据当前用户ID获取商家ID
     */
    public Long getMerchantIdByUserId(Long userId) {
        MerchantDO merchant = merchantMapper.selectOne(
            new LambdaQueryWrapper<MerchantDO>().eq(MerchantDO::getUserId, userId));
        if (merchant == null) {
            throw new MerchantException.MerchantNotFoundException("商家信息不存在");
        }
        return merchant.getId();
    }

    /**
     * 根据当前用户ID获取店铺ID
     */
    public Long getShopIdByUserId(Long userId) {
        Long merchantId = getMerchantIdByUserId(userId);
        ShopDO shop = shopMapper.selectOne(
            new LambdaQueryWrapper<ShopDO>().eq(ShopDO::getMerchantId, merchantId));
        if (shop == null) {
            throw new BusinessException(403, "店铺不存在");
        }
        return shop.getId();
    }

    /**
     * 根据当前用户ID获取店铺信息
     */
    public ShopDO getShopByUserId(Long userId) {
        Long merchantId = getMerchantIdByUserId(userId);
        ShopDO shop = shopMapper.selectOne(
            new LambdaQueryWrapper<ShopDO>().eq(ShopDO::getMerchantId, merchantId));
        if (shop == null) {
            throw new BusinessException(403, "店铺不存在");
        }
        return shop;
    }

    // ===== Helper methods =====

    /**
     * 获取By User Id
     * @param userId userId
     * @return 返回结果
     */
    public MerchantDO getByUserId(Long userId) {
        LambdaQueryWrapper<MerchantDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MerchantDO::getUserId, userId);
        MerchantDO merchant = merchantMapper.selectOne(wrapper);
        if (merchant == null) {
            throw new MerchantException.MerchantNotFoundException("商家信息不存在，请先申请入驻");
        }
        return merchant;
    }

    private ShopDO getShopByMerchantId(Long merchantId) {
        LambdaQueryWrapper<ShopDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShopDO::getMerchantId, merchantId);
        return shopMapper.selectOne(wrapper);
    }

    private PageResponse<MerchantListResponse> queryMerchantPage(int page, int size, LambdaQueryWrapper<MerchantDO> wrapper) {
        Page<MerchantDO> pageObj = new Page<>(page, size);
        IPage<MerchantDO> result = merchantMapper.selectPage(pageObj, wrapper);
        List<MerchantListResponse> list = result.getRecords().stream()
                .map(this::toMerchantListResponse)
                .collect(Collectors.toList());
        return PageResponse.of(list, result.getTotal(), page, size);
    }

    private MerchantListResponse toMerchantListResponse(MerchantDO m) {
        MerchantListResponse resp = new MerchantListResponse();
        resp.setMerchantId(m.getId());
        resp.setUserId(m.getUserId());
        resp.setMerchantName(m.getMerchantName());
        resp.setContactPhone(m.getContactPhone());
        resp.setStatus(m.getStatus());
        resp.setCreatedAt(m.getCreatedAt());
        return resp;
    }

    private MerchantInfoResponse toMerchantInfoResponse(MerchantDO m, ShopDO shop) {
        MerchantInfoResponse resp = new MerchantInfoResponse();
        resp.setMerchantId(m.getId());
        resp.setMerchantName(m.getMerchantName());
        resp.setContactPhone(m.getContactPhone());
        resp.setContactEmail(m.getContactEmail());
        resp.setStatus(m.getStatus());
        if (shop != null) {
            resp.setShopId(shop.getId());
            resp.setShopName(shop.getShopName());
        }
        return resp;
    }
}
