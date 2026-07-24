package com.sim.shopping.application.auth;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sim.shopping.domain.common.exception.UserException;
import com.sim.shopping.infrastructure.persistence.entity.MerchantDO;
import com.sim.shopping.infrastructure.persistence.entity.UserDO;
import com.sim.shopping.infrastructure.persistence.mapper.MerchantMapper;
import com.sim.shopping.infrastructure.persistence.mapper.UserMapper;
import com.sim.shopping.infrastructure.common.SystemConstants;
import com.sim.shopping.infrastructure.security.JwtTokenProvider;
import com.sim.shopping.infrastructure.security.SecurityUser;
import com.sim.shopping.interfaces.dto.auth.LoginRequest;
import com.sim.shopping.interfaces.dto.auth.RegisterRequest;
import com.sim.shopping.interfaces.dto.auth.TokenResponse;
import com.sim.shopping.interfaces.dto.auth.UserInfoResponse;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 认证服务，处理用户注册、登录、Token生成和刷新，以及Token黑名单管理
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserMapper userMapper;
    private final MerchantMapper merchantMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    public AuthService(UserMapper userMapper,
                       MerchantMapper merchantMapper,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider) {
        this.userMapper = userMapper;
        this.merchantMapper = merchantMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 用户注册
     * @param request request
     * @return 返回结果
     */
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
        user.setRole(SystemConstants.ROLE_USER);
        user.setStatus(SystemConstants.STATUS_ACTIVE);
        user.setPoints(0);
        user.setGender(0);
        userMapper.insert(user);

        // Generate tokens
        String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getUsername(), user.getRole());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId(), user.getUsername());

        return buildTokenResponse(user, accessToken, refreshToken);
    }

    /**
     * 用户登录
     * @param request request
     * @return 返回结果
     */
    public TokenResponse login(LoginRequest request) {
        UserDO user = userMapper.selectOne(
                Wrappers.<UserDO>lambdaQuery()
                        .eq(UserDO::getUsername, request.getUsername())
                        .last("LIMIT 1")
        );
        if (user == null) {
            throw new UserException.UserNotFoundException("用户不存在: " + request.getUsername());
        }

        if (!SystemConstants.STATUS_ACTIVE.equals(user.getStatus())) {
            throw new UserException.UserDisabledException("账号已被禁用");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserException.PasswordErrorException("密码错误");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getUsername(), user.getRole());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId(), user.getUsername());

        return buildTokenResponse(user, accessToken, refreshToken);
    }

    /**
     * 用户登出，将当前Access Token加入Redis黑名单（无Redis时降级为仅清除上下文）
     * @param accessToken 当前请求的Access Token（不含Bearer前缀）
     */
    public void logout(String accessToken) {
        if (redisTemplate != null && accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
            long expiration = jwtTokenProvider.getAccessTokenExpiration();
            redisTemplate.opsForValue().set(
                    "jwt:blacklist:" + accessToken,
                    "revoked",
                    expiration,
                    TimeUnit.MILLISECONDS
            );
            log.info("Token已加入黑名单");
        } else if (redisTemplate == null) {
            log.debug("Redis不可用，Token黑名单功能降级");
        }
        SecurityContextHolder.clearContext();
    }

    /**
     * 判断Token是否在黑名单中（无Redis时始终返回false）
     * @param token token
     * @return 如果在黑名单返回true
     */
    public boolean isTokenBlacklisted(String token) {
        if (redisTemplate == null) {
            return false;
        }
        return Boolean.TRUE.equals(redisTemplate.hasKey("jwt:blacklist:" + token));
    }

    /**
     * 刷新Token
     * @param refreshToken refreshToken
     * @return 返回结果
     */
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

        if (!SystemConstants.STATUS_ACTIVE.equals(user.getStatus())) {
            throw new UserException.UserDisabledException("账号已被禁用");
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getUsername(), user.getRole());
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user.getId(), user.getUsername());

        return buildTokenResponse(user, newAccessToken, newRefreshToken);
    }

    /**
     * 获取用户信息
     * @return 返回结果
     */
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
        if (SystemConstants.ROLE_MERCHANT.equalsIgnoreCase(user.getRole())) {
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

    /**
     * 构建Token响应对象，统一封装用户信息、Token及过期时间
     * @param user 用户实体
     * @param accessToken Access Token
     * @param refreshToken Refresh Token
     * @return Token响应DTO
     */
    private TokenResponse buildTokenResponse(UserDO user, String accessToken, String refreshToken) {
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
}
