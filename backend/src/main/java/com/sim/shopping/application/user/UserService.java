package com.sim.shopping.application.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sim.shopping.domain.common.exception.UserException;
import com.sim.shopping.infrastructure.persistence.entity.BrowseHistoryDO;
import com.sim.shopping.infrastructure.persistence.entity.ProductDO;
import com.sim.shopping.infrastructure.persistence.entity.SearchHistoryDO;
import com.sim.shopping.infrastructure.persistence.entity.UserAddressDO;
import com.sim.shopping.infrastructure.persistence.entity.UserDO;
import com.sim.shopping.infrastructure.persistence.mapper.BrowseHistoryMapper;
import com.sim.shopping.infrastructure.persistence.mapper.ProductMapper;
import com.sim.shopping.infrastructure.persistence.mapper.SearchHistoryMapper;
import com.sim.shopping.infrastructure.persistence.mapper.UserAddressMapper;
import com.sim.shopping.infrastructure.persistence.mapper.UserMapper;
import com.sim.shopping.interfaces.dto.user.AddressRequest;
import com.sim.shopping.interfaces.dto.user.AddressResponse;
import com.sim.shopping.interfaces.dto.user.BrowseHistoryResponse;
import com.sim.shopping.interfaces.dto.user.ChangePasswordRequest;
import com.sim.shopping.interfaces.dto.user.SearchHistoryResponse;
import com.sim.shopping.interfaces.dto.user.UpdateProfileRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final UserAddressMapper userAddressMapper;
    private final SearchHistoryMapper searchHistoryMapper;
    private final BrowseHistoryMapper browseHistoryMapper;
    private final ProductMapper productMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserMapper userMapper,
                       UserAddressMapper userAddressMapper,
                       SearchHistoryMapper searchHistoryMapper,
                       BrowseHistoryMapper browseHistoryMapper,
                       ProductMapper productMapper,
                       PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.userAddressMapper = userAddressMapper;
        this.searchHistoryMapper = searchHistoryMapper;
        this.browseHistoryMapper = browseHistoryMapper;
        this.productMapper = productMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void updateProfile(Long userId, UpdateProfileRequest req) {
        UserDO user = userMapper.selectById(userId);
        if (user == null) {
            throw new UserException.UserNotFoundException("用户不存在");
        }
        user.setNickname(req.getNickname());
        user.setAvatar(req.getAvatar());
        user.setGender(req.getGender());
        user.setPhone(req.getPhone());
        user.setEmail(req.getEmail());
        userMapper.updateById(user);
    }

    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest req) {
        UserDO user = userMapper.selectById(userId);
        if (user == null) {
            throw new UserException.UserNotFoundException("用户不存在");
        }
        if (!passwordEncoder.matches(req.getOldPassword(), user.getPassword())) {
            throw new UserException.PasswordErrorException("旧密码不正确");
        }
        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        userMapper.updateById(user);
    }

    public List<AddressResponse> getAddressList(Long userId) {
        LambdaQueryWrapper<UserAddressDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAddressDO::getUserId, userId)
               .orderByDesc(UserAddressDO::getIsDefault)
               .orderByDesc(UserAddressDO::getCreatedAt);
        List<UserAddressDO> list = userAddressMapper.selectList(wrapper);
        return list.stream().map(this::toAddressResponse).collect(Collectors.toList());
    }

    @Transactional
    public AddressResponse createAddress(Long userId, AddressRequest req) {
        if (req.getIsDefault() != null && req.getIsDefault() == 1) {
            clearDefaultAddresses(userId);
        }
        UserAddressDO address = new UserAddressDO();
        address.setUserId(userId);
        address.setReceiverName(req.getReceiverName());
        address.setReceiverPhone(req.getReceiverPhone());
        address.setProvince(req.getProvince());
        address.setCity(req.getCity());
        address.setDistrict(req.getDistrict());
        address.setDetailAddress(req.getDetailAddress());
        address.setIsDefault(req.getIsDefault() != null ? req.getIsDefault() : 0);
        userAddressMapper.insert(address);
        return toAddressResponse(address);
    }

    @Transactional
    public AddressResponse updateAddress(Long userId, Long addressId, AddressRequest req) {
        UserAddressDO address = userAddressMapper.selectById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new UserException.UserNotFoundException("地址不存在");
        }
        if (req.getIsDefault() != null && req.getIsDefault() == 1) {
            clearDefaultAddresses(userId);
        }
        address.setReceiverName(req.getReceiverName());
        address.setReceiverPhone(req.getReceiverPhone());
        address.setProvince(req.getProvince());
        address.setCity(req.getCity());
        address.setDistrict(req.getDistrict());
        address.setDetailAddress(req.getDetailAddress());
        if (req.getIsDefault() != null) {
            address.setIsDefault(req.getIsDefault());
        }
        userAddressMapper.updateById(address);
        return toAddressResponse(address);
    }

    @Transactional
    public void deleteAddress(Long userId, Long addressId) {
        UserAddressDO address = userAddressMapper.selectById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new UserException.UserNotFoundException("地址不存在");
        }
        userAddressMapper.deleteById(addressId);
    }

    @Transactional
    public void setDefaultAddress(Long userId, Long addressId) {
        UserAddressDO address = userAddressMapper.selectById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new UserException.UserNotFoundException("地址不存在");
        }
        clearDefaultAddresses(userId);
        address.setIsDefault(1);
        userAddressMapper.updateById(address);
    }

    public List<SearchHistoryResponse> getSearchHistory(Long userId) {
        LambdaQueryWrapper<SearchHistoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SearchHistoryDO::getUserId, userId)
               .orderByDesc(SearchHistoryDO::getSearchedAt)
               .last("LIMIT 50");
        List<SearchHistoryDO> list = searchHistoryMapper.selectList(wrapper);
        return list.stream().map(this::toSearchHistoryResponse).collect(Collectors.toList());
    }

    @Transactional
    public void clearSearchHistory(Long userId) {
        LambdaQueryWrapper<SearchHistoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SearchHistoryDO::getUserId, userId);
        searchHistoryMapper.delete(wrapper);
    }

    public List<BrowseHistoryResponse> getBrowseHistory(Long userId) {
        LambdaQueryWrapper<BrowseHistoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BrowseHistoryDO::getUserId, userId)
               .orderByDesc(BrowseHistoryDO::getCreatedAt)
               .last("LIMIT 50");
        List<BrowseHistoryDO> list = browseHistoryMapper.selectList(wrapper);
        return list.stream().map(this::toBrowseHistoryResponse).collect(Collectors.toList());
    }

    @Transactional
    public void clearBrowseHistory(Long userId) {
        LambdaQueryWrapper<BrowseHistoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BrowseHistoryDO::getUserId, userId);
        browseHistoryMapper.delete(wrapper);
    }

    private void clearDefaultAddresses(Long userId) {
        LambdaQueryWrapper<UserAddressDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAddressDO::getUserId, userId)
               .eq(UserAddressDO::getIsDefault, 1);
        List<UserAddressDO> defaults = userAddressMapper.selectList(wrapper);
        for (UserAddressDO addr : defaults) {
            addr.setIsDefault(0);
            userAddressMapper.updateById(addr);
        }
    }

    private AddressResponse toAddressResponse(UserAddressDO addr) {
        AddressResponse resp = new AddressResponse();
        resp.setId(addr.getId());
        resp.setReceiverName(addr.getReceiverName());
        resp.setReceiverPhone(addr.getReceiverPhone());
        resp.setProvince(addr.getProvince());
        resp.setCity(addr.getCity());
        resp.setDistrict(addr.getDistrict());
        resp.setDetailAddress(addr.getDetailAddress());
        resp.setIsDefault(addr.getIsDefault());
        resp.setCreatedAt(addr.getCreatedAt());
        resp.setUpdatedAt(addr.getUpdatedAt());
        return resp;
    }

    private SearchHistoryResponse toSearchHistoryResponse(SearchHistoryDO hist) {
        SearchHistoryResponse resp = new SearchHistoryResponse();
        resp.setId(hist.getId());
        resp.setKeyword(hist.getKeyword());
        resp.setSearchedAt(hist.getSearchedAt());
        return resp;
    }

    private BrowseHistoryResponse toBrowseHistoryResponse(BrowseHistoryDO bh) {
        BrowseHistoryResponse resp = new BrowseHistoryResponse();
        resp.setId(bh.getId());
        resp.setProductId(bh.getProductId());
        ProductDO product = productMapper.selectById(bh.getProductId());
        if (product != null) {
            resp.setProductName(product.getName());
            resp.setProductImage(product.getMainImage());
            resp.setPrice(product.getPrice());
        }
        resp.setViewedAt(bh.getCreatedAt());
        return resp;
    }
}
