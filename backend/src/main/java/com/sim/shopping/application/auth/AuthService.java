package com.sim.shopping.application.auth;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sim.shopping.domain.common.exception.UserException;
import com.sim.shopping.infrastructure.persistence.entity.MerchantDO;
import com.sim.shopping.infrastructure.persistence.entity.UserDO;
import com.sim.shopping.infrastructure.persistence.mapper.MerchantMapper;
import com.sim.shopping.infrastructure.persistence.mapper.UserMapper;
import com.sim.shopping.infrastructure.security.JwtTokenProvider;
import com.sim.shopping.infrastructure.security.SecurityUser;
import com.sim.shopping.interfaces.dto.auth.LoginRequest;
import com.sim.shopping.interfaces.dto.auth.RegisterRequest;
import com.sim.shopping.interfaces.dto.auth.TokenResponse;
import com.sim.shopping.interfaces.dto.auth.UserInfoResponse;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserMapper userMapper;
    private final MerchantMapper merchantMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserMapper userMapper,
                       MerchantMapper merchantMapper,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider) {
        this.userMapper = userMapper;
        this.merchantMapper = merchantMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse register(RegisterRequest request) {
        // Check username uniqueness
        Long existingCount = userMapper.selectCount(
                Wrappers.<UserDO>lambdaQuery().eq(UserDO::getUsername, request.getUsername())
        );
        if (existingCount > 0) {
            throw new UserException.UserAlreadyExistsException("用户名已存在: " + request.getUsername());
        }

        // Create user
        UserDO user = new UserDO();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname());
        user.setRole("USER");
        user.setStatus("ACTIVE");
        user.setPoints(0);
        user.setGender(0);
        userMapper.insert(user);

        // Generate tokens
        String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getUsername(), user.getRole());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId(), user.getUsername());

        return new TokenResponse(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getAvatar(),
                user.getRole(),
                accessToken,
                jwtTokenProvider.getAccessTokenExpiration() / 1000,
                refreshToken
        );
    }

    public TokenResponse login(LoginRequest request) {
        UserDO user = userMapper.selectOne(
                Wrappers.<UserDO>lambdaQuery()
                        .eq(UserDO::getUsername, request.getUsername())
                        .last("LIMIT 1")
        );
        if (user == null) {
            throw new UserException.UserNotFoundException("用户不存在: " + request.getUsername());
        }

        if (!"ACTIVE".equals(user.getStatus())) {
            throw new UserException.UserDisabledException("账号已被禁用");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserException.PasswordErrorException("密码错误");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getUsername(), user.getRole());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId(), user.getUsername());

        return new TokenResponse(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getAvatar(),
                user.getRole(),
                accessToken,
                jwtTokenProvider.getAccessTokenExpiration() / 1000,
                refreshToken
        );
    }

    public void logout() {
        // Stateless JWT — client discards token
        // Could add token blacklist via Redis in the future
        SecurityContextHolder.clearContext();
    }

    public TokenResponse refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new UserException.TokenInvalidException("refreshToken无效");
        }

        Claims claims = jwtTokenProvider.parseToken(refreshToken);
        String tokenType = claims.get("tokenType", String.class);
        if (!"refresh".equals(tokenType)) {
            throw new UserException.TokenInvalidException("非refreshToken类型");
        }

        Long userId = Long.parseLong(claims.getSubject());
        String username = claims.get("username", String.class);

        UserDO user = userMapper.selectById(userId);
        if (user == null) {
            throw new UserException.UserNotFoundException("用户不存在");
        }

        if (!"ACTIVE".equals(user.getStatus())) {
            throw new UserException.UserDisabledException("账号已被禁用");
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getUsername(), user.getRole());
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user.getId(), user.getUsername());

        return new TokenResponse(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getAvatar(),
                user.getRole(),
                newAccessToken,
                jwtTokenProvider.getAccessTokenExpiration() / 1000,
                newRefreshToken
        );
    }

    public UserInfoResponse getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof SecurityUser)) {
            throw new UserException.UserNotFoundException("未登录或登录已过期");
        }

        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        UserDO user = userMapper.selectById(securityUser.getUserId());
        if (user == null) {
            throw new UserException.UserNotFoundException("用户不存在");
        }

        UserInfoResponse response = new UserInfoResponse();
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setNickname(user.getNickname());
        response.setAvatar(user.getAvatar());
        response.setGender(user.getGender());
        response.setPhone(user.getPhone());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setPoints(user.getPoints());

        // If user is a merchant, find merchantId
        if ("MERCHANT".equalsIgnoreCase(user.getRole())) {
            MerchantDO merchant = merchantMapper.selectOne(
                    Wrappers.<MerchantDO>lambdaQuery()
                            .eq(MerchantDO::getUserId, user.getId())
                            .last("LIMIT 1")
            );
            if (merchant != null) {
                response.setMerchantId(merchant.getId());
            }
        }

        return response;
    }
}
